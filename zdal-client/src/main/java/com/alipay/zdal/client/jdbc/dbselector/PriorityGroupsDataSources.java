/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.client.jdbc.dbselector;

import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.alipay.zdal.client.jdbc.DBSelector.DataSourceTryer;
import com.alipay.zdal.client.jdbc.ZdalStatement.DB_OPERATION_TYPE;

public class PriorityGroupsDataSources {
    private static final Logger   logger              = Logger
                                                          .getLogger(PriorityGroupsDataSources.class);
    private final EquityDbManager equityDbManager;

    /**
     * 非阻塞锁，用于控制只允许一个业务线程去重试；
     */
    private final ReentrantLock   lock                = new ReentrantLock();

    /**
     * 数据源是否可用，默认可用
     */
    private volatile boolean      isNotAvailable      = false;
    /**
     * 上次重试时间
     */
    private volatile long         lastRetryTime       = 0;
    /**
     * 异常次数
     */
    private volatile int          exceptionTimes      = 0;
    /**
     * 第一次捕获异常的时间，单位毫秒
     */
    private volatile long         firstExceptionTime  = 0;
    /**
     * 重试故障db的时间间隔，默认值设为2s,单位毫秒
     */
    private final int             retryBadDbInterval  = 2000;

    /**
     * 单位时间段，默认为1分钟，用于统计时间段内某个db抛异常的次数，单位毫秒
     */
    private final int             timeInterval        = 60000;
    /**
     * 单位时间内允许异常的次数，如果超过这个值便将数据源置为不可用
     */
    private final int             allowExceptionTimes = 20;

    PriorityGroupsDataSources(EquityDbManager equityDbManager) {
        this.equityDbManager = equityDbManager;
    }

    /**
     * 按优先级进行分组，比如p0，p1等，如果某个优先级内的数据源均不可用了，会抛出NoMoreDataSourceException
     * 然后根据单位时间内抛出该异常的次数是否超过某个阈值来决定是否将该优先级分组置为不可用，即isNotAvailable=true
     * @param <T>
     * @param failedDataSources
     * @param tryer
     * @param times
     * @param operationType
     * @param i
     * @param args
     * @return
     * @throws SQLException
     */
    public <T> T tryExecute(Map<DataSource, SQLException> failedDataSources,
                            DataSourceTryer<T> tryer, int times, DB_OPERATION_TYPE operationType,
                            int i, Object... args) throws SQLException {
        T t = null;
        //如果不可用
        if (isNotAvailable) {
            t = tryOnFailedPriorityGroupDataSource(failedDataSources, tryer, times, operationType,
                i, args);
        } else {
            t = tryOnAvailablePriorityGroupDataSource(failedDataSources, tryer, times,
                operationType, i, args);
        }
        return t;
    }

    /**
     * 在不可用的优先级上进行操作
     * 只允许单线程进入到该优先级进行重试，如果尝试成功则将该优先级置为可用；如果失败就抛出异常，外围捕获住异常即会尝试下一个优先级；
     * 如果其他线程不符合重试状态，则不进行重试，直接抛出异常，外围捕获异常，随即尝试下一个优先级；
     * 
     * @param <T>
     * @param failedDataSources
     * @param tryer
     * @param times
     * @param operationType
     * @param i
     * @param args
     * @return
     * @throws SQLException 
     * @throws SQLException 
     */
    public <T> T tryOnFailedPriorityGroupDataSource(
                                                    Map<DataSource, SQLException> failedDataSources,
                                                    DataSourceTryer<T> tryer, int times,
                                                    DB_OPERATION_TYPE operationType, int i,
                                                    Object... args) throws SQLException {
        T t = null;
        boolean toTry = System.currentTimeMillis() - lastRetryTime > retryBadDbInterval;
        //每间隔两秒，只会有一个业务线程继续使用这个优先级的数据源。
        if (toTry && lock.tryLock()) {
            try {
                logger.warn("线程" + Thread.currentThread().getName() + "在"
                            + getCurrentDateTime(null) + "进入优先级" + i + "的单线程重试状态！");
                Long beginTime = System.currentTimeMillis();
                t = equityDbManager
                    .tryExecute(failedDataSources, tryer, times, operationType, args);
                logger.warn("单线程" + Thread.currentThread().getName() + "去获取该优先级p" + i + "成功，耗时为："
                            + (System.currentTimeMillis() - beginTime));
                this.isNotAvailable = false;
                this.exceptionTimes = 0;
                logger.warn("数据源优先级p" + i + "在" + getCurrentDateTime(null) + "已经恢复，标记为可用！");
            } catch (NoMoreDataSourceException e) {
                logger.error("单线程重试优先级 p" + i + "失败，现在去寻找其下一个优先级的数据源！", e);
                throw e;
            } finally {
                lastRetryTime = System.currentTimeMillis();
                lock.unlock();
            }
        } else {
            //不符合尝试该优先级的条件，直接抛出异常，继续外围for循环去操作下一个优先级
            logger.warn("优先级p" + i + "正在被其他线程访问，或者不符合2s的时间间隔！");
            throw new NoMoreDataSourceException("No more dataSource for p" + i);
        }
        return t;
    }

    /**
     * 在可用的优先级上进行操作，
     * 如果该优先级报出异常，则进行统计单位时间内的异常次数，如果超过指定的阈值，则将该优先级置为不可用
     * 统计完异常次数后将异常抛出，外围会捕获该异常，随即选择下一个优先级的数据源进行操作
     * @param <T>
     * @param failedDataSources
     * @param tryer
     * @param times
     * @param operationType
     * @param i
     * @param args
     * @return
     * @throws SQLException
     */
    public <T> T tryOnAvailablePriorityGroupDataSource(
                                                       Map<DataSource, SQLException> failedDataSources,
                                                       DataSourceTryer<T> tryer, int times,
                                                       DB_OPERATION_TYPE operationType, int i,
                                                       Object... args) throws SQLException {
        T t = null;
        try {
            t = equityDbManager.tryExecute(failedDataSources, tryer, times, operationType, args);
        } catch (NoMoreDataSourceException e) {
            calcFailedDSExceptionTimes(i);
            throw e;
        } catch (IllegalStateException e) {//如果在本组中找不到，就抛出NoMoreDataSourceException异常，便于从下一个EquityDbManager中继续执行.
            throw new NoMoreDataSourceException(e.getMessage());
        }
        return t;
    }

    /**
     * 统计优先级pi的异常次数
     * 小于指定时间间隔则累加异常次数，如果异常次数超过某个阈值就该优先级将置为不可用；否则清零重新计数
     * @param i
     */
    public synchronized void calcFailedDSExceptionTimes(int i) {
        //统计该分组内的单位时间内异常的次数
        if (this.exceptionTimes == 0) {
            this.firstExceptionTime = System.currentTimeMillis();
        }
        long currentTime = System.currentTimeMillis();
        if (currentTime - this.firstExceptionTime <= timeInterval) {
            ++exceptionTimes;
            logger.error("优先级p" + i + "单位时间内第" + exceptionTimes + "次异常，当前时间："
                         + getCurrentDateTime(currentTime) + "，首次异常时间："
                         + getCurrentDateTime(firstExceptionTime) + "，时间间隔为："
                         + (currentTime - firstExceptionTime) + "ms.");
            if (exceptionTimes >= allowExceptionTimes) {
                this.isNotAvailable = true;
                logger.error("优先级p" + i + "在时间" + getCurrentDateTime(null) + "被踢出！");
            }
        } else {
            logger.warn("统计异常次数超过单位时间间隔,上次单位时间间隔内异常次数为" + exceptionTimes + "次,现在开始重新计数！");
            this.exceptionTimes = 0;
        }
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

    public EquityDbManager getEquityDbManager() {
        return equityDbManager;
    }
}
