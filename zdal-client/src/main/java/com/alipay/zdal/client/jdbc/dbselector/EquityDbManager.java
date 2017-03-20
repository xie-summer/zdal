/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.client.jdbc.dbselector;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.alipay.zdal.client.ThreadLocalString;
import com.alipay.zdal.client.jdbc.ZdalStatement.DB_OPERATION_TYPE;
import com.alipay.zdal.client.util.ThreadLocalMap;
import com.alipay.zdal.common.Constants;
import com.alipay.zdal.common.OperationDBType;
import com.alipay.zdal.common.RuntimeConfigHolder;
import com.alipay.zdal.common.WeightRandom;
import com.alipay.zdal.common.lang.StringUtil;

/**
 * 对等数据库管理器
 * 可以是读对等：如多个读库，每个库的数据完全相同。对等读取
 * 可以是写对等：如日志库，每个库数据不同，一条数据写入哪个库都可以。对等写入
 * 
 * 支持动态推送权重，动态加减库
 * 
 * 
 * @param <T> JdbcTemplate 或则 DataSource
 * 
 * TODO 从DataSource对象本身判断是不是同一个库，不再保留这种配置信息：Map<String, Map<String, Object>>
 */
public class EquityDbManager extends AbstractDBSelector {
    private static final Logger logger = Logger.getLogger(EquityDbManager.class);

    /**
     * 运行期会动态改变的状态。保持不变对象，只能重建，不能修改。
     */
    private static class DbRuntime {
        public final Map<String, DataSource>       dataSources;      //最终结果
        public final Map<String, DataSourceHolder> dataSourceHolders; //包装过的datasource集合,主要包括是否可读等属性

        public final WeightRandom                  weightRandom;

        public DbRuntime(Map<String, DataSource> dataSources, WeightRandom weightRandom) {
            this.dataSources = Collections.unmodifiableMap(dataSources);

            this.weightRandom = weightRandom;

            this.dataSourceHolders = getDataSourceHolders(dataSources);

        }

        /**
         * 得到包装过的datasource集合；
         * @param dataSources
         * @return
         */
        public Map<String, DataSourceHolder> getDataSourceHolders(
                                                                  Map<String, DataSource> dataSources) {
            Map<String, DataSourceHolder> map = new HashMap<String, DataSourceHolder>(dataSources
                .size());
            for (Map.Entry<String, DataSource> entry : dataSources.entrySet()) {
                map.put(entry.getKey(), new DataSourceHolder(entry.getValue()));

            }
            return Collections.unmodifiableMap(map);
        }

        public DataSource select() {
            return this.dataSources.get(this.weightRandom.select(null));
        }
    }

    private final RuntimeConfigHolder<DbRuntime> dbHolder = new RuntimeConfigHolder<DbRuntime>();

    /**
     * key:日志库键值，单独推送权重时使用
     *     日志库中是properties文件中自定义的key, 在分库分表读重试中是dbSelectId_index
     * value：日志库对应的JdbcTemplate
     */
    private Map<String, DataSource>              initDataSources;                                //最终结果
    //    /**
    //     * key: 一个log数据源键值
    //     * value:数据源配置Properties对应的Map
    //     *    key：数据源需要的参数如driver,username..
    //     *    value:参数的值
    //     */
    //    private Map<String, DataSourceConfig>        initDataSourceConfigs;
    private String                               dataSourceConfigFile;

    public EquityDbManager(String id) {
        super(id);
    }

    public EquityDbManager(String id, Map<String, DataSource> initDataSources) {
        super(id);
        this.initDataSources = initDataSources;
        try {
            this.init();
        } catch (IOException e) {
            logger.error("Should not happen!!", e); //因为是在解析dataSourceConfigFile才会报IOException
        }
    }

    public EquityDbManager(String id, Map<String, DataSource> initDataSources,
                           Map<String, Integer> weights) {
        this(id, initDataSources);
        if (weights != null) {
            setWeightRandom(new WeightRandom(weights));
        }
    }

    /**
     * 各个配置优先级：1.dataSources 2.dataSourceConfigs 3.dataSourceConfigFile 4.订阅
     */
    public void init() throws IOException {
        //初始化数据源
        if (this.initDataSources != null) {
            //如果直接配置了syncLogDataSources，则直接使用，不支持订阅和动态修改
            WeightRandom weightRandom = new WeightRandom(this.initDataSources.keySet().toArray(
                new String[0]));
            this.dbHolder.set(new DbRuntime(this.initDataSources, weightRandom));
        }
        //        else if (this.initDataSourceConfigs != null) {
        //            //如果直接配置了syncLogDataSourceConfigs，则直接使用，不支持订阅和动态修改
        //            //initDataSources(this.initDataSourceConfigs);
        //        }
        else if (this.dataSourceConfigFile != null) {
            //如果直接配置了dataSourceConfigFile，则解析文件，不支持订阅和动态修改
            Properties p = new Properties();
            if (dataSourceConfigFile.startsWith("/")) {
                dataSourceConfigFile = StringUtil.substringAfter(dataSourceConfigFile, "/");
            }
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(
                dataSourceConfigFile);
            if (null == inputStream) {
                throw new IllegalArgumentException("dataSource配置文件不存在: " + dataSourceConfigFile);
            }
            p.load(inputStream);
            //initDataSources(parseDataSourceConfig(p));
        } else {

        }

    }

    /**
     * 根据权重，随机返回一个DataSource
     * @return
     */
    public DataSource select() {
        return this.dbHolder.get().select();
    }

    //TODO 考虑接口是否缩小为只返回DataSource[]
    public Map<String, DataSource> getDataSources() {
        return this.dbHolder.get().dataSources;
    }

    public Map<String, Integer> getWeights() {
        return this.dbHolder.get().weightRandom.getWeightConfig();
    }

    /**
     * 在所管理的数据库上重试执行一个回调操作。失败了根据权重选下一个库重试
     * 以根据权重选择到的DataSource，和用户传入的自用参数args，重试调用DataSourceTryer的tryOnDataSource方法
     * @param failedDataSources 已知的失败DS及其异常
     * @param args 透传到DataSourceTryer的tryOnDataSource方法中
     * @return null表示执行成功。否则表示重试次内执行失败，返回SQLException列表
     */
    public <T> T tryExecute(Map<DataSource, SQLException> failedDataSources,
                            DataSourceTryer<T> tryer, int times, DB_OPERATION_TYPE operationType,
                            Object... args) throws SQLException {
        List<SQLException> exceptions = new ArrayList<SQLException>(0);
        List<String> excludeKeys = new ArrayList<String>(0);
        DbRuntime dbrt = this.dbHolder.get();
        WeightRandom wr = dbrt.weightRandom;
        if (failedDataSources != null) {
            exceptions.addAll(failedDataSources.values());
            times = times - failedDataSources.size(); //扣除已经失败掉的重试次数
            for (SQLException e : failedDataSources.values()) {
                if (!exceptionSorter.isExceptionFatal(e)) {
                    //有一个异常（其实是最后加入的异常，因map无法知道顺序，只能遍历）不是数据库不可用异常，则抛出
                    //是不是应该在发现非数据库fatal之后就立刻抛出，而不是放到failedDataSources这个map里?(guangxia)
                    return tryer.onSQLException(exceptions, exceptionSorter, args);
                }
            }
        }
        String name = null;
        //如果指定了某个读库
        Integer dbIndex = (Integer) ThreadLocalMap.get(ThreadLocalString.DATABASE_INDEX);

        for (int i = 0; i < times; i++) {
            if (i == 0 && dbIndex != null) {
                //如果是指定了库读
                name = this.getId() + Constants.DBINDEX_DSKEY_CONN_CHAR + dbIndex;
            } else {
                //随机选库
                name = wr.select(excludeKeys);
            }
            if (name == null) {
                exceptions.add(new NoMoreDataSourceException("在执行sql的过程中，没有可用数据源了！"));
                logger.warn("在此次执行sql的过程中，数据源" + wr.getAllDbKeys() + "均不可用了！");
                break;
            }
            //获取到db的名字，然后缓存起来，业务会用到该名字
            Map<String, DataSource> map = new HashMap<String, DataSource>();
            //加上前缀，by冰魂 20130903
            map.put(getAppDsName() + "." + name, null);
            ThreadLocalMap.put(ThreadLocalString.GET_ID_AND_DATABASE, map);

            DataSourceHolder selectedDS = dbrt.dataSourceHolders.get(name);
            if (selectedDS == null) {
                //不应该出现的。初始化逻辑应该保证空的数据源(null)不会被加入DataSourceHolders
                throw new IllegalStateException("Can't find DataSource for name:" + name
                                                + "from dataSourceHolders!");
            }
            if (failedDataSources != null && failedDataSources.containsKey(selectedDS.getDs())) {
                excludeKeys.add(name);
                if (dbIndex == null) {
                    i--;
                }
                continue;
            }
            if (logger.isDebugEnabled()) {
                logger.debug("selected database name=" + name);
            }
            int size1 = excludeKeys.size();
            //如果该数据源已经被标识为不可用
            T t = selectedDS.tryOnSelectedDataSource(operationType, wr, dbrt.dataSourceHolders,
                failedDataSources, tryer, exceptions, excludeKeys, exceptionSorter, name, args);
            boolean isAddedIntoExcludeKeys = excludeKeys.size() - size1 > 0;
            if (isAddedIntoExcludeKeys) {
                continue;
            } else {
                return t;
            }
        }
        //return exceptions; //返回以方便业务记log, 不想tryExecute上多两个参数去填throwSQLException
        return tryer.onSQLException(exceptions, exceptionSorter, args);
    }

    public static interface DataSourceChangeListener {
        public void onDataSourceChanged(Map<String, DataSource> dataSources);
    }

    public void setWeight(Map<String, Integer> weightMap) {
        setWeightRandom(new WeightRandom(weightMap));
    }

    /**
     * 支持动态修改权重
     */
    synchronized boolean setWeightRandom(WeightRandom weightRandom) {
        if (weightRandom == null) {
            return false;
        }
        Map<String, Integer> newWeight = weightRandom.getWeightConfig();
        DbRuntime dbrt = EquityDbManager.this.dbHolder.get();
        for (String newkey : newWeight.keySet()) {
            if (!dbrt.dataSources.containsKey(newkey)) {
                logger.error("新权重的数据源名称在现有数据源中不存在:" + newkey);
                return false;
            }
        }
        if (newWeight.size() < dbrt.dataSources.size()) {
            logger.warn("新权重的数据源名称个数小于原有数据源！");
            return false; //这种情况不被允许更安全一些
        }
        Map<String, DataSource> dataSources = new HashMap<String, DataSource>(dbrt.dataSources
            .size());
        dataSources.putAll(dbrt.dataSources);
        DbRuntime newrt = new DbRuntime(dataSources, weightRandom);
        EquityDbManager.this.dbHolder.set(newrt);
        return true;
    }

    /*
     * 对等库默认不读重试，只有读库才进行读重试，写库不进行写重试；
     */
    public boolean isSupportRetry(OperationDBType type) {
        boolean flag = false;
        String dbSelectorId = getId();
        if (dbSelectorId.endsWith("_r") || (type == OperationDBType.readFromDb)) {
            flag = true;
        }
        return flag;
    }

    /**
     * 无逻辑的getter/setter
     */
    public void setInitDataSources(Map<String, DataSource> initDataSources) {
        this.initDataSources = initDataSources;
    }

    public void setDataSourceConfigFile(String dataSourceConfigFile) {
        this.dataSourceConfigFile = dataSourceConfigFile;
    }

}