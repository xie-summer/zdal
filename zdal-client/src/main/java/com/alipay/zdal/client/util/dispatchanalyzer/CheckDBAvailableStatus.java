/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.alipay.zdal.client.util.dispatchanalyzer;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import com.alipay.zdal.client.datasource.keyweight.ZdalDataSourceKeyWeightRandom;
import com.alipay.zdal.client.jdbc.AbstractZdalDataSource;
import com.alipay.zdal.client.jdbc.DBSelector;
import com.alipay.zdal.client.jdbc.ZdalRuntime;
import com.alipay.zdal.client.jdbc.dbselector.OneDBSelector;
import com.alipay.zdal.common.Constants;
import com.alipay.zdal.common.DBType;
import com.alipay.zdal.common.RuntimeConfigHolder;

/**
 * 
 * @author zhaofeng.wang
 * @version $Id: CheckDBAvailableStatus.java, v 0.1 2013-3-20 上午11:32:51 zhaofeng.wang Exp $
 */
public class CheckDBAvailableStatus {
    public static final Logger     logger             = Logger
                                                          .getLogger(Constants.CONFIG_LOG_NAME_LOGNAME);
    /**
     * Zdal封装的数据源 
     */
    private AbstractZdalDataSource targetDataSource;
    /** 
     * 轮训数据源状态的线程 
     */
    private Thread                 circulateThread    = null;

    /**
     * 默认的轮训时间，单位ms
     */
    private long                   waitTime           = 1000L;

    /**
     * 通过Future方式获取检查连接结果的超时时长，如果超过该值就抛出timeOutException，单位ms
     */
    private long                   timeOutLength      = 500L;
    /**
     * 最多可以自动剔除的数据库的个数，默认为-1
     */
    private int                    closeDBLimitNumber = -1;

    /**
     * 线程池的最小值
     */
    private int                    corePoolSize       = 1;
    /**
     * 线程池的最大值
     */
    private int                    maximumPoolSize    = 10;
    /**
     * 线程池队列长度
     */
    private int                    workQueueSize      = 100;
    /**
     * 线程池，采用抛弃最旧的任务的策略，防止一直将队列堵死
     */
    private ExecutorService        checkDBStatusExecutor;
    /**
     * 是否使用异步提交方式
     */
    private boolean                isUseFutureMode    = true;

    /**
     * 异步线程轮循
     */
    void runCirculateThread() {
        //启动轮询线程池
        checkDBStatusExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, 0L,
            TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(workQueueSize),
            new ThreadPoolExecutor.DiscardOldestPolicy());
        //启动异步线程
        circulateThread = new Thread(new Runnable() {
            public void run() {
                // 等待waitTime秒
                while (true) {
                    try {
                        Thread.sleep(waitTime);
                        //如果发生异常，就catch住，防止线程异常退出
                        circulateDBStatus();
                    } catch (Exception e) {
                        logger.error("Circulate db status error!", e);
                    }
                    //轮训数据库，获取其状态,如果状态未发生变更，则不重置
                }
            }
        });
        circulateThread.start();
    }

    /**
     * 循环扫描所有db的状态，然后更新
     */
    private synchronized void circulateDBStatus() {
        //先获取所有的纬度，然后依次遍历
        Map<String, ZdalDataSourceKeyWeightRandom> keyWeightMapConfig = getTargetDataSource()
            .getKeyWeightMapConfig();
        if (keyWeightMapConfig == null || keyWeightMapConfig.size() == 0) {
            throw new IllegalArgumentException("The keyWeightMapConfig is empty! ");
        }
        boolean isDBStatusChanged = false;
        //外围循环，遍历每一个全活策略的组
        for (Map.Entry<String, ZdalDataSourceKeyWeightRandom> entry : keyWeightMapConfig.entrySet()) {
            boolean changeFlag = false;
            String group_key = entry.getKey().trim();
            ZdalDataSourceKeyWeightRandom tDataSourceKeyWeightRandom = entry.getValue();
            // Map<String, Integer> dbWeightKeysAndWeights = tDataSourceKeyWeightRandom
            //   .getWeightConfig();
            String[] dbWeightKeys = tDataSourceKeyWeightRandom.getDBWeightKeys();
            int[] weightValues = tDataSourceKeyWeightRandom.getDBWeightValues();

            //String[] newDBWeightKeys = new String[dbWeightKeys.length];
            int[] newWeightValues = new int[weightValues.length];

            //Map<String, Integer> weightKeysAndWeights = new HashMap<String, Integer>();
            //内层循环，遍历组内的每一个数据源，检查其权重
            int number = 0;
            //int dbNumberInGroup = dbWeightKeysAndWeights.size();
            int dbNumberInGroup = dbWeightKeys.length;

            for (int i = 0; i < dbNumberInGroup; i++) {
                String dbKey = dbWeightKeys[i];
                Integer weightValue = 10;
                boolean isAvailable = isAvailableDB(dbKey);
                //检查状态变更，包括 不可用-》可用， 以及 可用-》不可用 两种状态
                if (isAvailable) {
                    //不可用-》可用
                    if (weightValues[i] == 0) {
                        changeFlag = true;
                        logger.warn("The db status will change from NOT to YES,groupName="
                                    + group_key + ",dbName=" + dbKey);
                    } else {
                        if (logger.isDebugEnabled()) {
                            logger.debug("The db is Ok, groupName=" + group_key + ",dbName="
                                         + dbKey);
                        }
                    }
                } else {// 可用-》不可用
                    if (weightValues[i] > 0) {
                        if (++number <= getCloseDBLimitNumber(dbNumberInGroup)) {
                            changeFlag = true;
                            weightValue = 0;
                            logger.warn("The db status will change from YES to NOT,dbName=" + dbKey
                                        + ",the total number of " + group_key + " is "
                                        + dbNumberInGroup + ",and the failed number is " + number);
                        } else {
                            logger.warn("The db status can't change from YES to NOT,dbName="
                                        + dbKey + ",the total number of " + group_key + " is "
                                        + dbNumberInGroup + ",and the failed number is " + number);
                        }
                    } else {
                        weightValue = 0;
                        number++;
                    }
                }
                newWeightValues[i] = weightValue;
                //weightKeysAndWeights.put(dbKey, weightValue);
            }
            //如果发生了状态变更；
            if (changeFlag) {
                isDBStatusChanged = true;
                // String[] weightKeys = weightKeysAndWeights.keySet().toArray(new String[0]);
                // int[] weights = new int[weightKeysAndWeights.size()];
                //for (int i = 0; i < weights.length; i++) {
                //   weights[i] = weightKeysAndWeights.get(weightKeys[i]);
                // }
                ZdalDataSourceKeyWeightRandom TDataSourceKeyWeightRandom = new ZdalDataSourceKeyWeightRandom(
                    dbWeightKeys, newWeightValues);
                keyWeightMapConfig.put(group_key, TDataSourceKeyWeightRandom);
            }
        }
        if (isDBStatusChanged) {
            logger.warn("The db status have changed, it is:" + dbKeyWeightToString());
        }

    }

    /**
     * 利用future的方式来测试db的可用性
     * 
     * @param dbKey
     * @return
     */
    private boolean isAvailableDB(final String dbKey) {
        //如果不使用异步检测超时方式
        if (!isUseFutureMode()) {
            return checkDBAvailalbeStatusUtil(dbKey);
        }
        //提交执行任务
        Future<Boolean> future = checkDBStatusExecutor.submit(new Callable<Boolean>() {
            public Boolean call() throws Exception {
                try {
                    return checkDBAvailalbeStatusUtil(dbKey);
                } catch (Exception e) {
                    logger.error("Check db status failed,threadName="
                                 + Thread.currentThread().getName() + ",dbKey=" + dbKey, e);
                    return false;
                }
            }
        });
        //获取执行结果
        try {
            return future.get(timeOutLength, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            boolean result = future.cancel(true);
            logger.error("获取数据库状态超时:threadName=" + Thread.currentThread().getName() + ",dbKey="
                         + dbKey + ",cacleResult=" + result + ",workQueueCapacity="
                         + getWorkQueueCapacity() + ",poolSize=" + this.getPoolSize()
                         + ",activeThreadCount=" + this.getActiveCount() + ",largestPoolSize="
                         + this.getLargestPoolSize(), e);
            return false;
        } finally {
            if (logger.isDebugEnabled()) {
                logger.debug("---------------------------ok---------------------------------");
            }
        }
    }

    /**
     * 判定数据源是否可用<br>  checkDataBaseStatus  isAvailableDB
     * @param dbKey  数据源标识<br>
     * @return  是否可用<br>
     */
    private boolean checkDBAvailalbeStatusUtil(final String dbKey) {
        boolean isAvailable = true;

        RuntimeConfigHolder<ZdalRuntime> runtimeConfigHolder = getTargetDataSource()
            .getRuntimeConfigHolder();
        DBSelector ds = runtimeConfigHolder.get().dbSelectors.get(dbKey);
        if (ds == null || !(ds instanceof OneDBSelector)) {
            throw new IllegalArgumentException("DBSelector type error,dbKey=" + dbKey);
        }
        DBType dbType = ((OneDBSelector) ds).getDbType();
        if (dbType == null) {
            throw new IllegalArgumentException("The dbType can't be null,please set the property!");
        }
        JdbcTemplate jdbcTemplate = new JdbcTemplate(((OneDBSelector) ds).getDb());

        String sql = null;
        switch (dbType) {
            case MYSQL:
                sql = "select 'x' ";
                break;
            case ORACLE:
                sql = "select * from dual";
                break;
            default:
                throw new IllegalArgumentException("数据库类型出错误，请检查配置！");
        }
        try {
            jdbcTemplate.queryForList(sql);
        } catch (Exception e) {
            logger.error("连接该db出错，ThreadName=" + Thread.currentThread().getName() + ",dbKey="
                         + dbKey + ",sql=" + sql, e);
            isAvailable = false;
        }
        return isAvailable;
    }

    /**
     * 获取所有db的状态
     * 
     * @return
     */
    private String dbKeyWeightToString() {
        Map<String, ZdalDataSourceKeyWeightRandom> keyWeightMapConfig = getTargetDataSource()
            .getKeyWeightMapConfig();
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, ZdalDataSourceKeyWeightRandom> entry : keyWeightMapConfig.entrySet()) {
            String groupKey = entry.getKey();
            sb.append(groupKey + ":{ ");
            ZdalDataSourceKeyWeightRandom tDataSourceKeyWeightRandom = entry.getValue();
            String[] dbWeightKeys = tDataSourceKeyWeightRandom.getDBWeightKeys();
            int[] weightValues = tDataSourceKeyWeightRandom.getDBWeightValues();
            for (int i = 0; i < dbWeightKeys.length; i++) {
                String dbKey = dbWeightKeys[i];
                if (weightValues[i] > 0) {
                    sb.append(dbKey + ":Y ");
                } else {
                    sb.append(dbKey + ":N ");
                }
            }
            sb.append("} ");
        }
        return sb.toString();
    }

    /**
     * Returns the number of elements that this queue can ideally (in
     * the absence of memory or resource constraints) accept without
     * blocking. This is always equal to the initial capacity of this queue
     * less the current <tt>size</tt> of this queue.
     * <p>Note that you <em>cannot</em> always tell if
     * an attempt to <tt>add</tt> an element will succeed by
     * inspecting <tt>remainingCapacity</tt> because it may be the
     * case that a waiting consumer is ready to <tt>take</tt> an
     * element out of an otherwise full queue.
     */
    private int getWorkQueueCapacity() {
        ThreadPoolExecutor tpe = (ThreadPoolExecutor) checkDBStatusExecutor;
        LinkedBlockingQueue<Runnable> lq = (LinkedBlockingQueue<Runnable>) (tpe.getQueue());
        return lq.remainingCapacity();
    }

    /**
     * Returns the current number of threads in the pool.
     *
     * @return the number of threads
     */
    private int getPoolSize() {
        ThreadPoolExecutor tpe = (ThreadPoolExecutor) checkDBStatusExecutor;
        return tpe.getPoolSize();
    }

    /**
     * Returns the approximate number of threads that are actively
     * executing tasks.
     *
     * @return the number of threads
     */
    private int getActiveCount() {
        ThreadPoolExecutor tpe = (ThreadPoolExecutor) checkDBStatusExecutor;
        return tpe.getActiveCount();
    }

    /**
     * Returns the largest number of threads that have ever
     * simultaneously been in the pool.
     *
     * @return the number of threads
     */
    private int getLargestPoolSize() {
        ThreadPoolExecutor tpe = (ThreadPoolExecutor) checkDBStatusExecutor;
        return tpe.getLargestPoolSize();
    }

    /**
     * Returns the approximate total number of tasks that have been
     * scheduled for execution. Because the states of tasks and
     * threads may change dynamically during computation, the returned
     * value is only an approximation, but one that does not ever
     * decrease across successive calls.
     *
     * @return the number of tasks
     */
    @SuppressWarnings("unused")
    private long getTaskCount() {
        ThreadPoolExecutor tpe = (ThreadPoolExecutor) checkDBStatusExecutor;
        return tpe.getTaskCount();
    }

    /**
     * 打印参数
     * @see java.lang.Object#toString()
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("isUseFutureMode=" + this.isUseFutureMode + ",waitTime=" + this.waitTime
                  + ",timeOutLength=" + this.timeOutLength + ",closeDBLimitNumber="
                  + this.closeDBLimitNumber + ",corePoolSize=" + this.corePoolSize
                  + ",maxinumPoolSize=" + this.maximumPoolSize + ",workQueueSize="
                  + this.workQueueSize);
        return sb.toString();
    }

    /**
    * Setter method for property <tt>targetDataSource</tt>.
    * 
    * @param targetDataSource value to be assigned to property targetDataSource
    */
    public void setTargetDataSource(AbstractZdalDataSource targetDataSource) {
        this.targetDataSource = targetDataSource;
    }

    /**
     * Getter method for property <tt>targetDataSource</tt>.
     * 
     * @return property value of targetDataSource
     */
    public AbstractZdalDataSource getTargetDataSource() {
        return targetDataSource;
    }

    /**
     * Getter method for property <tt>waitTime</tt>.
     * 
     * @return property value of waitTime
     */
    public long getWaitTime() {
        return waitTime;
    }

    /**
     * Setter method for property <tt>waitTime</tt>.
     * 
     * @param waitTime value to be assigned to property waitTime
     */
    public void setWaitTime(long waitTime) {
        this.waitTime = waitTime;
    }

    /**
     * Getter method for property <tt>timeOutLength</tt>.
     * 
     * @return property value of timeOutLength
     */
    public long getTimeOutLength() {
        return timeOutLength;
    }

    /**
     * Setter method for property <tt>timeOutLength</tt>.
     * 
     * @param timeOutLength value to be assigned to property timeOutLength
     */
    public void setTimeOutLength(long timeOutLength) {
        this.timeOutLength = timeOutLength;
    }

    /**
     * Getter method for property <tt>closeDBLimitNumber</tt>.
     * 
     * @return property value of closeDBLimitNumber
     */
    public int getCloseDBLimitNumber(int dbNumberInGroup) {
        if (closeDBLimitNumber == -1) {
            return dbNumberInGroup / 2;
        }
        return closeDBLimitNumber;
    }

    /**
     * Setter method for property <tt>closeDBLimitNumber</tt>.
     * 
     * @param closeDBLimitNumber value to be assigned to property closeDBLimitNumber
     */
    public void setCloseDBLimitNumber(int closeDBLimitNumber) {
        this.closeDBLimitNumber = closeDBLimitNumber;
    }

    /**
     * Getter method for property <tt>corePoolSize</tt>.
     * 
     * @return property value of corePoolSize
     */
    public int getCorePoolSize() {
        return corePoolSize;
    }

    /**
     * Setter method for property <tt>corePoolSize</tt>.
     * 
     * @param corePoolSize value to be assigned to property corePoolSize
     */
    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    /**
     * Getter method for property <tt>maximumPoolSize</tt>.
     * 
     * @return property value of maximumPoolSize
     */
    public int getMaximumPoolSize() {
        return maximumPoolSize;
    }

    /**
     * Setter method for property <tt>maximumPoolSize</tt>.
     * 
     * @param maximumPoolSize value to be assigned to property maximumPoolSize
     */
    public void setMaximumPoolSize(int maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }

    /**
     * Getter method for property <tt>workQueueSize</tt>.
     * 
     * @return property value of workQueueSize
     */
    public int getWorkQueueSize() {
        return workQueueSize;
    }

    /**
     * Setter method for property <tt>workQueueSize</tt>.
     * 
     * @param workQueueSize value to be assigned to property workQueueSize
     */
    public void setWorkQueueSize(int workQueueSize) {
        this.workQueueSize = workQueueSize;
    }

    /**
     * Getter method for property <tt>isUseFutureMode</tt>.
     * 
     * @return property value of isUseFutureMode
     */
    public boolean isUseFutureMode() {
        return isUseFutureMode;
    }

    /**
     * Setter method for property <tt>isUseFutureMode</tt>.
     * 
     * @param isUseFutureMode value to be assigned to property isUseFutureMode
     */
    public void setIsUseFutureMode(boolean isUseFutureMode) {
        this.isUseFutureMode = isUseFutureMode;
    }

}
