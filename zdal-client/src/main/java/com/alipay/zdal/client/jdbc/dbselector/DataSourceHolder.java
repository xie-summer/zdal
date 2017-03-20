/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.client.jdbc.dbselector;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.alipay.zdal.client.jdbc.DBSelector.DataSourceTryer;
import com.alipay.zdal.client.jdbc.ZdalStatement.DB_OPERATION_TYPE;
import com.alipay.zdal.client.util.ExceptionUtils;
import com.alipay.zdal.common.Constants;
import com.alipay.zdal.common.WeightRandom;
import com.alipay.zdal.common.exception.sqlexceptionwrapper.ZdalCommunicationException;
import com.alipay.zdal.common.jdbc.sorter.ExceptionSorter;
import com.alipay.zdal.common.jdbc.sorter.MySQLExceptionSorter;
import com.alipay.zdal.common.jdbc.sorter.OracleExceptionSorter;

/**
 * 增加了DataSourceHolder静态类，主要用于故障数据源的剔除与恢复
 * @author zhaofeng.wang
 * @version $Id: DataSourceHolder.java,v 0.1 2011-4-14 下午04:00:55 zhaofeng.wang Exp $
 */
public class DataSourceHolder {

    private static final Logger logger              = Logger
                                                        .getLogger(Constants.CONFIG_LOG_NAME_LOGNAME);

    private final DataSource    ds;

    /**
     * 非阻塞锁，用于控制只允许一个业务线程去重试；
     */
    private final ReentrantLock lock                = new ReentrantLock();

    /**
     * 数据源是否可用，默认可用
     */
    private volatile boolean    isNotAvailable      = false;
    /**
     * 上次重试时间
     */
    private volatile long       lastRetryTime       = 0;
    /**
     * 异常次数
     */
    private volatile int        exceptionTimes      = 0;
    /**
     * 第一次捕获异常的时间，单位毫秒
     */
    private volatile long       firstExceptionTime  = 0;
    /**
     * 重试故障db的时间间隔，默认值设为2s,单位毫秒
     */
    private final int           retryBadDbInterval  = 2000;

    /**
     * 单位时间段，默认为1分钟，用于统计时间段内某个db抛异常的次数，单位毫秒
     */
    private final int           timeInterval        = 60000;
    /**
     * 单位时间内允许异常的次数，如果超过这个值便将数据源置为不可用
     */
    private final int           allowExceptionTimes = 10;

    /**
     * 构造函数
     * @param ds
     */
    public DataSourceHolder(DataSource ds) {
        this.ds = ds;
    }

    public DataSource getDs() {
        return ds;
    }

    /**
     * 在选中的数据源上进行操作，根据该数据源目前的可用性进行判断走哪个分支；二者的区别主要有以下两点：
     * （1）如果数据源目前不可用，只允许业务单线程重试；
     * （2）二者在处理异常的环节存在一定区别，主要是如果目前不可用，单线程尝试又失败时，就不用统计异常次数，而如果目前可用，发生异常时需要
     *      统计单位时间内的异常次数
     * @param <T>
     * @param operationType  读操作还是写操作
     * @param weightRandom    各个数据源的权重
     * @param dataSourceHolders 包装过的数据源
     * @param failedDataSources  失败的datasource
     * @param tryer 
     * @param exceptions
     * @param excludeKeys
     * @param exceptionSorter
     * @param name
     * @param args
     * @return
     * @throws SQLException
     */
    public <T> T tryOnSelectedDataSource(DB_OPERATION_TYPE operationType,
                                         WeightRandom weightRandom,
                                         Map<String, DataSourceHolder> dataSourceHolders,
                                         Map<DataSource, SQLException> failedDataSources,
                                         DataSourceTryer<T> tryer, List<SQLException> exceptions,
                                         List<String> excludeKeys, ExceptionSorter exceptionSorter,
                                         String name, Object... args) throws SQLException {
        T t = null;
        if (isNotAvailable) {
            t = tryOnFailedDataSource(failedDataSources, tryer, exceptions, excludeKeys,
                exceptionSorter, name, args);
        } else {
            t = tryOnAvailableDataSource(operationType, weightRandom, dataSourceHolders,
                failedDataSources, tryer, exceptions, excludeKeys, exceptionSorter, name, args);
        }
        return t;

    }

    /**
     * 如果数据源已经被标记为不可用，则只允许一个线程进行尝试，尝试成功直接标记为可用，否则排除掉该数据源，去寻找其他的可用数据源；
     * 如果已经有业务线程在尝试该数据源（即不符合时间间隔或者拿不到锁），那么其他线程直接排除掉该数据源，去寻找其他的数据源
     * @param <T>
     * @param failedDataSources
     * @param tryer
     * @param exceptions
     * @param excludeKeys
     * @param exceptionSorter
     * @param name
     * @param args
     * @return
     * @throws SQLException
     */
    public <T> T tryOnFailedDataSource(Map<DataSource, SQLException> failedDataSources,
                                       DataSourceTryer<T> tryer, List<SQLException> exceptions,
                                       List<String> excludeKeys, ExceptionSorter exceptionSorter,
                                       String name, Object... args) throws SQLException {
        T t = null;
        boolean toTry = System.currentTimeMillis() - lastRetryTime > retryBadDbInterval;
        //每间隔两秒，只会有一个业务线程继续使用这个数据源。
        if (toTry && lock.tryLock()) {
            try {
                logger.warn("线程" + Thread.currentThread().getName() + "在"
                            + getCurrentDateTime(null) + "进入数据源" + name + "的单线程重试状态！");
                //做一次真正的数据库连接，如果不成功就表示数据源还不可用，直接返回；
                boolean isSussessful = tryToConnectDataBase(exceptionSorter, name);
                if (!isSussessful) {
                    excludeKeys.add(name);
                    return t;
                }
                Long beginTime = System.currentTimeMillis();
                t = tryer.tryOnDataSource(ds, name, args);
                logger.warn("单线程" + Thread.currentThread().getName() + "去获取该数据源连接" + name
                            + "成功，耗时为：" + (System.currentTimeMillis() - beginTime));
                //如果线程执行成功则标记为可用，将该数据源标记为可用
                isNotAvailable = false;
                exceptionTimes = 0;
                logger.warn("数据源" + name + "在" + getCurrentDateTime(null) + "已经恢复，标记为可用！");
            } catch (SQLException e) {
                exceptions.add(e);
                boolean isFatal = exceptionSorter.isExceptionFatal(e);
                if (!isFatal || failedDataSources == null) {
                    logger.warn("异常错误码: " + e.getErrorCode() + " 不是数据库不可用异常或不要求重试，直接抛出.isFatal= "
                                + isFatal, e);
                    return tryer.onSQLException(exceptions, exceptionSorter, args);
                }
                excludeKeys.add(name);
                logger.warn("单线程" + Thread.currentThread().getName() + "尝试故障数据源" + name
                            + "时失败，现在去寻找其他可用的数据源！");
            } finally {
                lastRetryTime = System.currentTimeMillis();
                lock.unlock();
            }
        } else {
            //不符合尝试该数据源的条件，继续外围for循环去找寻其他的数据源
            logger.warn("线程" + Thread.currentThread().getName() + "在" + getCurrentDateTime(null)
                        + "时间打算尝试故障数据源" + name + "时失败，其他线程正在访问或者不符合2s的时间间隔！");
            excludeKeys.add(name);
        }
        return t;
    }

    /**
     * 在单线程重试的时候，取到连接后真正和数据库建立连接；
     */
    public boolean tryToConnectDataBase(ExceptionSorter exceptionSorter, String name) {
        Long beginTime = System.currentTimeMillis();
        boolean isSussessful = true;
        String sql = null;
        if (exceptionSorter instanceof MySQLExceptionSorter) {
            sql = "select 'x' ";
        } else if (exceptionSorter instanceof OracleExceptionSorter) {
            sql = "select * from dual";
        } else {
            logger.error("数据库类型出错误，请检查配置！");
            isSussessful = false;
            return isSussessful;
        }
        Connection con = null;
        Statement stmt = null;
        try {
            con = ds.getConnection();
            logger.warn("Get the connection success,time="
                        + getCurrentDateTime(System.currentTimeMillis()));
            stmt = con.createStatement();
            logger.warn("Create the statement success,time="
                        + getCurrentDateTime(System.currentTimeMillis()));
            stmt.executeQuery(sql);
            logger.warn("单线程" + Thread.currentThread().getName() + "利用sql=" + sql + "真正做校验连接该数据源"
                        + name + "成功，耗时为:" + (System.currentTimeMillis() - beginTime));
        } catch (SQLException e) {
            logger.error("单线程" + Thread.currentThread().getName() + "利用sql=" + sql + "真正做校验连接该数据源"
                         + name + "失败,耗时为:" + (System.currentTimeMillis() - beginTime) + "ms", e);
            isSussessful = false;
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                logger.warn("close the resource has an error", e);
            }
        }
        return isSussessful;
    }

    /**
     * 如果该数据源可用，直接在该数据源上进行操作，如果发生异常，则在catch代码块里进行相应的处理，处理流程基本与tryOnFailedDataSource方法类似，
     * 唯一不同的是这个时候需要进行异常次数的累加，采用的方式是，如果单位时间内发生异常的次数超过事先设置的阈值，并且目前读数据源个数
     * 多余一个，则将该数据源置为不可用，如果是写库，即使只有一个，也会置为不可用
     * @param <T>
     * @param failedDataSources
     * @param tryer
     * @param exceptions
     * @param excludeKeys
     * @param exceptionSorter
     * @param name
     * @param args
     * @return
     * @throws SQLException
     */
    public <T> T tryOnAvailableDataSource(DB_OPERATION_TYPE operationType,
                                          WeightRandom weightRandom,
                                          Map<String, DataSourceHolder> dataSourceHolders,
                                          Map<DataSource, SQLException> failedDataSources,
                                          DataSourceTryer<T> tryer, List<SQLException> exceptions,
                                          List<String> excludeKeys,
                                          ExceptionSorter exceptionSorter, String name,
                                          Object... args) throws SQLException {
        T t = null;
        try {
            t = tryer.tryOnDataSource(ds, name, args);
        } catch (SQLException e) {
            exceptions.add(e);
            boolean isFatal = exceptionSorter.isExceptionFatal(e);
            if (!isFatal || failedDataSources == null) {
                logger.warn("异常错误码: " + e.getErrorCode() + " 不是数据库不可用异常或不要求重试，直接抛出.isFatal= "
                            + isFatal, e);
                return tryer.onSQLException(exceptions, exceptionSorter, args);
            }
            logger.error("线程" + Thread.currentThread().getName()
                         + "在访问dataSourceHolder数据源发生故障，name=" + name, e);
            excludeKeys.add(name);
            //此处将故障数据源排除，采用的策略是根据单位时间内连接失败的次数如果超过某个阈值，当数据源个数仅剩余一个时，将根据数据源类型决定是否踢出； 
            calcFailedDSExceptionTimes(name, operationType, weightRandom, dataSourceHolders);
        }
        return t;

    }

    /**
     * 外部传入两个变量，分别是各个数据源的权重weightRandom，以及包装过的数据源dataSourceHolders，
     * 两个属性一起用于统计当前可用的数据源个数时使用
     * 判定一个数据源可用的前提条件是：权重大于0，且isNotAvailable的值为false；
     */

    /**
     * 计算当前数据源的异常次数
     * （1）统计当前可用的数据源个数，根据operationType来判断是写库还是读库；
     *      如果是读库，目前如只剩余一个可用数据源，不做任何处理；如果是写库，则没有此限制；
     * （2）在可踢出的情况下，如果单位时间（暂设为1分钟）内异常次数超过某个阈值（暂设为20次），则将该数据源标记为不可用；
     * @param name
     * @param operationType
     * @param weightRandom
     * @param dataSourceHolders
     */
    public synchronized void calcFailedDSExceptionTimes(
                                                        String name,
                                                        DB_OPERATION_TYPE operationType,
                                                        WeightRandom weightRandom,
                                                        Map<String, DataSourceHolder> dataSourceHolders) {
        //如果该数据源已经标记为不可用，则直接返回
        if (isNotAvailable) {
            logger.warn("数据源" + name + "已经不可用了，不用再统计异常次数了！");
            return;
        }
        //统计当前可用的数据源个数；
        int availableDSNumber = 0;
        for (Map.Entry<String, DataSourceHolder> entry : dataSourceHolders.entrySet()) {
            int weight = weightRandom.getWeightByKey(entry.getKey());
            //数据源的权重大于0，并且该数据源可用
            if ((weight > 0) && !(entry.getValue().isNotAvailable)) {
                availableDSNumber++;
            }
            logger.warn("The weight of  key=" + entry.getKey() + ",isNotAvailable="
                        + isNotAvailable);
        }

        logger.warn("目前可用的数据源的个数为" + availableDSNumber + ",operationType=" + operationType
                    + ",name=" + name);
        if (availableDSNumber <= 1 && (operationType == DB_OPERATION_TYPE.READ_FROM_DB)) {
            logger.error("读数据源" + name + "已经发生异常，但目前可用的读数据源个数仅剩余" + availableDSNumber + "个，故不踢除！");
            return;
        }

        if (this.exceptionTimes == 0) {
            this.firstExceptionTime = System.currentTimeMillis();
        }
        long currentTime = System.currentTimeMillis();
        //小于指定时间间隔则累加异常次数，如果异常次数超过某个阈值就将该数据源置为不可用；否则清零重新计数
        if (currentTime - this.firstExceptionTime <= timeInterval) {
            ++exceptionTimes;
            logger.error("数据源" + name + "单位时间内第" + exceptionTimes + "次异常，当前时间："
                         + getCurrentDateTime(currentTime) + "，首次异常时间："
                         + getCurrentDateTime(firstExceptionTime) + "，时间间隔为："
                         + (currentTime - firstExceptionTime) + "ms.");
            if (exceptionTimes >= allowExceptionTimes) {
                this.isNotAvailable = true;
                logger.error("数据源" + name + "在时间" + getCurrentDateTime(null) + "被踢出，目前"
                             + operationType + "类型可用的数据源个数为：" + (availableDSNumber - 1));
            }
        } else {
            logger.warn("统计异常次数超过单位时间间隔,上次单位时间间隔内异常次数为" + exceptionTimes + "次,现在开始重新计数！");
            this.exceptionTimes = 0;

        }
    }

    /**
     * 将异常进行处理后抛出；
     * @param exceptions
     * @param exceptionSorter
     * @throws SQLException
     */
    public void throwZdalCommnicationException(List<SQLException> exceptions,
                                               ExceptionSorter exceptionSorter) throws SQLException {

        int size = exceptions.size();
        if (size <= 0) {
            throw new IllegalArgumentException("should not be here!");
        } else {
            //正常情况下的处理
            int lastElementIndex = size - 1;
            //取最后一个exception.判断是否是数据库不可用异常.如果是，去掉最后一个异常，并将头异常包装为ZdalCommunicationException抛出
            SQLException lastSQLException = exceptions.get(lastElementIndex);
            if (exceptionSorter.isExceptionFatal(lastSQLException)) {
                exceptions.remove(lastElementIndex);
                exceptions.add(0, new ZdalCommunicationException("zdal communicationException:",
                    lastSQLException));
            }
        }
        ExceptionUtils.throwSQLException(exceptions, null, null);

    }

    /**
     * 获取当前的时间的格式化字符串
     */
    public String getCurrentDateTime(Long time) {
        java.util.Date now;
        if (time != null) {
            now = new java.util.Date(time);
        } else {
            now = new java.util.Date();
        }
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(now);
    }

    //    /**
    //     * 关闭statement
    //     * 
    //     * @param stmt
    //     */
    //    private void closeStatement(Statement stmt) {
    //        if (stmt != null) {
    //            try {
    //                stmt.close();
    //            } catch (SQLException e) {
    //                logger.warn("Could not close JDBC Statement", e);
    //            } catch (Throwable e) {
    //                logger.warn("Unexpected exception on closing JDBC Statement", e);
    //            }
    //        }
    //    }
    //
    //    /**
    //     * 关闭连接
    //     * 
    //     * @param conn
    //     */
    //    private void closeConnection(Connection conn) {
    //        if (conn != null) {
    //            try {
    //                conn.close();
    //            } catch (SQLException e) {
    //                logger.warn("Could not close JDBC Connection", e);
    //            } catch (Throwable e) {
    //                logger.warn("Unexpected exception on closing JDBC Connection", e);
    //            }
    //        }
    //    }

}
