/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.client.util.dispatchanalyzer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;

import com.alipay.zdal.client.datasource.keyweight.ZdalDataSourceKeyWeightRandom;
import com.alipay.zdal.client.dispatcher.DatabaseChoicer;
import com.alipay.zdal.client.dispatcher.Result;
import com.alipay.zdal.client.jdbc.AbstractZdalDataSource;
import com.alipay.zdal.client.util.condition.TableShardingRuleImpl;
import com.alipay.zdal.common.Constants;
import com.alipay.zdal.common.lang.StringUtil;
import com.alipay.zdal.rule.config.beans.AppRule;
import com.alipay.zdal.rule.config.beans.ShardRule;
import com.alipay.zdal.rule.config.beans.TableRule;
import com.alipay.zdal.rule.groovy.GroovyListRuleEngine;
import com.alipay.zdal.rule.ruleengine.entities.abstractentities.RuleChain;
import com.alipay.zdal.rule.ruleengine.entities.retvalue.TargetDB;
import com.alipay.zdal.rule.ruleengine.util.RuleUtils;

/**
 * 全活策略的支持类，随机获取某个可用的db序号或者表号,<br>
 * 编辑到业务流水号里，然后在做sharding路由的时候会用到。<br>
 * 本类可以实现的功能包括：
 * 1)全活策略场景下，根据逻辑表名和组号（默认为0，因为只有一组）随机获取一个可用的db号，以及在该db里的物理表名<br>
 * 2）获取该逻辑表在某个数据源上的分库分表的情况<br>
 * 
 * 对dual表的优化策略：
 * 由于全活策略需要获取一个可用的db序号，之前的方案是每次有业务请求到达，都要通过select from dual 
 * 进行db可用性的校验， 造成 dba 发现dual表的访问次数太高，导致load升高；优化后大大降低了对dual表的访问次数，db的load随之降低。
 * 简单描述方案：
 * 由之前的业务线程检测db 可用性，改变为 由zdal的异步线程检测，每隔一定的时间段（假设10ms，可配置）
 * 检测一次所有db的状态，但只有发现和上次的检测结果存在差异的情况下才去刷新变更db的状态，该方案很好的实现了全活策略数据源的自动剔除和恢复。
 * 
 * @author zhaofeng.wang
 * @version $Id: TDatasourceIntrospector.java, v 0.1 2012-3-26 下午06:26:21 zhaofeng.wang Exp $
 */

public class ZdalDatasourceIntrospector {

    public static final Logger                  logger                 = Logger
                                                                           .getLogger(Constants.CONFIG_LOG_NAME_LOGNAME);
    /**
     * Zdal封装的数据源 
     */
    private AbstractZdalDataSource              targetDataSource;
    /**
     * key 为 “数据源标识#逻辑表名”，value为表的后缀集合
     */
    private Map<String, List<String>>           tablesCache            = new HashMap<String, List<String>>(
                                                                           0);
    /**
     * 权重随机器
     */
    private final Random                        random                 = new Random();
    /**
     * 规则链缓存
     */
    private static final Map<String, RuleChain> ruleChainCache         = new HashMap<String, RuleChain>();
    /**
     * 单个规则链
     */
    private RuleChain                           singleRuleChain;

    /**
     * 默认的轮训时间
     */
    private long                                waitTime               = 1000L;
    /**
     * 通过Future方式获取检查连接结果的超时时长，如果超过该值就抛出timeOutException
     */
    private long                                timeOutLength          = 500L;

    /**
     * 最多可以自动剔除的数据库的个数，默认为-1
     */
    private int                                 closeDBLimitNumber     = -1;

    private CheckDBAvailableStatus              checkDBAvailableStatus = new CheckDBAvailableStatus();

    /**
     * 线程池的最小值
     */
    private int                                 corePoolSize           = 1;
    /**
     * 线程池的最大值
     */
    private int                                 maximumPoolSize        = 10;
    /**
     * 线程池队列长度
     */
    private int                                 workQueueSize          = 100;

    /**
     * 是否使用异步提交方式
     */
    private boolean                             isUseFutureMode        = true;

    public void init() {
        //启动异步线程，轮训数据库的可用状态
        //启动异步线程，轮训数据库的可用状态
        checkDBAvailableStatus.setTargetDataSource(targetDataSource);
        checkDBAvailableStatus.setIsUseFutureMode(isUseFutureMode);
        checkDBAvailableStatus.setWaitTime(waitTime);
        checkDBAvailableStatus.setTimeOutLength(timeOutLength);
        checkDBAvailableStatus.setCloseDBLimitNumber(closeDBLimitNumber);
        checkDBAvailableStatus.setCorePoolSize(corePoolSize);
        checkDBAvailableStatus.setMaximumPoolSize(maximumPoolSize);
        checkDBAvailableStatus.setWorkQueueSize(workQueueSize);
        checkDBAvailableStatus.runCirculateThread();
        logger.warn("The chekDBAvailableStatus parameter:" + checkDBAvailableStatus.toString());

    }

    /**
     * 金融交换项目、上海收单项目在调用<br>
     * 
     * 全活策略依赖的接口<br>
     * 
     * 根据权重随机的选择一个可用db，并在该db上随机选择一个表，业务用于组装分库分表字段<br>
     * 
     * @param logicTableName   逻辑表名<br>
     * @param groupNum  全活策略场景下默认为0
     * @return   String[0]代表库号，String[1]代表物理表名<br>
     */
    public String[] getAvailableDBAndTableByWeights(String logicTableName, int groupNum,
                                                    boolean isCheckConnection) {
        return getAvailableGroupDBAndTableByWeights(logicTableName, groupNum, isCheckConnection);
    }

    /**
     * 金融交换项目组在调用<br>
     * 
     * 先进行分组，再进行组内全活策略的接口<br>
     *  获取组内可用的db号以及随机的表<br>
     * @param logicTableName 逻辑表名
     * @param parameters     分组参数对
     * @return               组内可用的db号和随机选择的表
     */
    public String[] getAvailableGroupDBAndTable(String logicTableName,
                                                Map<String, String> parameters,
                                                boolean isCheckConnection) {
        int groupNum = getShardingResultByTableName(logicTableName, parameters);
        return getAvailableGroupDBAndTableByWeights(logicTableName, groupNum, isCheckConnection);
    }

    /**
     * 根据用户组号，然后根据权重在组内随机的选择一个可用db，并在该db上随机选择一个表，业务用于组装分库分表字段<br>
     * 
     * @param logicTableName   逻辑表名<br>
     * @return   String[0]代表组内随机选择的库号，String[1]代表在库内随机选择的物理表名<br>
     */
    private String[] getAvailableGroupDBAndTableByWeights(String logicTableName, int groupNum,
                                                          boolean isCheckConnection) {
        String dbAndTable[] = new String[2];
        String orderInGroup = getAvailableGroupDBByWeights(groupNum, isCheckConnection);
        ZdalDataSourceKeyWeightRandom ZdalDataSourceKeyWeightRandom = getZdalDataSourceKeyWeightRandom(groupNum);
        String dbKey = ZdalDataSourceKeyWeightRandom
            .getDBKeyByNumber(Integer.valueOf(orderInGroup));
        String tableName = getTableNumberByRandom(dbKey, logicTableName);
        dbAndTable[0] = orderInGroup;
        dbAndTable[1] = tableName;
        return dbAndTable;
    }

    /**
     * 统一支付项目在调用
     * 
     * 先进行分组，再进行组内全活策略的接口.<br>
     * 在一组内获取可用的db，不需要逻辑表名.<br>
     * 调用此接口，须保证所有表的shardingRule都相同，或者只有一个表配置该属性<br>
     * @param parameters     参数键值对<br>
     * @return               组内可用的db号
     */
    public String getAvailableGroupDB(Map<String, String> parameters) {
        int groupNum = getShardingResult(parameters);
        return this.getAvailableGroupDBByWeights(groupNum, true);
    }

    /**
     * 统一支付项目调用
     * 
     * 先进行分组，再进行组内全活策略的接口.<br>
     * 在一组内获取可用的db，不需要逻辑表名.<br>
     * 调用此接口，须保证所有表的shardingRule都相同，或者只有一个表配置该属性<br>
     * @param parameters     参数键值对<br>
     * @return               组内可用的db号
     */
    public String getAvailableGroupDB(Map<String, String> parameters, boolean isCheckConnection) {
        int groupNum = getShardingResult(parameters);
        return this.getAvailableGroupDBByWeights(groupNum, isCheckConnection);
    }

    /**
     * 随机的选择一个可用db<br>
     * 如果 isCheckConnection 为 false，则默认 采用轮训机制，即zdal的异步线程间隔一段时间进行检测，
     * 如果为true，则表示每次都检查一下，耗费性能的说；
     * 
     * @param    groupNum   组号<br>
     * @param    isCheckConnection 是否检查连接
     * @return   String[0]代表库号，String[1]代表物理表名<br>
     */
    private String getAvailableGroupDBByWeights(int groupNum, boolean isCheckConnection) {
        ZdalDataSourceKeyWeightRandom ZdalDataSourceKeyWeightRandom = getZdalDataSourceKeyWeightRandom(groupNum);
        int orderInGroup = -1;

        // 根据权重在该groupNum内随机获取一个
        orderInGroup = ZdalDataSourceKeyWeightRandom
            .getRandomDBIndexByWeight(new ArrayList<Integer>());
        //获取该db对应的标识dbKey
        String dbKey = ZdalDataSourceKeyWeightRandom.getDBKeyByNumber(orderInGroup);

        if (logger.isDebugEnabled()) {
            logger.debug("dbNumber=" + orderInGroup + ",dbKey=" + dbKey + ", isCheckConnection = "
                         + isCheckConnection);
        }

        if (orderInGroup < 0
            || orderInGroup >= ZdalDataSourceKeyWeightRandom.getDataSourceNumberInGroup()) {
            throw new IllegalArgumentException("The order number in group_"
                                               + groupNum
                                               + " is "
                                               + orderInGroup
                                               + ", but the biggest number is "
                                               + (ZdalDataSourceKeyWeightRandom
                                                   .getDataSourceNumberInGroup() - 1));
        }

        //返回随机获取db的number
        return Integer.toString(orderInGroup);
    }

    /**
     * 根据db标识、逻辑表名随机获取该库上的一个物理表名<br>
     * @param dbKey            数据库标识<br>
     * @param logicTableName   逻辑表名<br>
     * @return                 物理表名<br>
     */
    private String getTableNumberByRandom(String dbKey, String logicTableName) {
        List<String> tableIndexesList = new ArrayList<String>(0);
        tableIndexesList = getAllTableIndex(dbKey, logicTableName);
        if (tableIndexesList == null || tableIndexesList.size() == 0) {
            throw new IllegalArgumentException("表名后缀集合不 能为空,dbKey=" + dbKey + ",logicTableName="
                                               + logicTableName);
        }
        int rand = random.nextInt(tableIndexesList.size());
        return tableIndexesList.get(rand);
    }

    /**
     * 根据库的标识dbKey和逻辑表名logicTableName获取该库上该逻辑表所对应的物理表名集合<br>
     * 因为一旦分库分表后，各个分库的分表相对都是固定的，因此可以缓存起来，先去缓存查，<br>
     * 如果查不到，就去获取该库上该逻辑表所对应的物理表名集合。<br>
     * 
     * @param dbKey          库的标识<br>
     * @param logicTableName 逻辑表名<br>
     * @return               表后缀集合<br>
     */
    private List<String> getAllTableIndex(String dbKey, String logicTableName) {

        String tablesCachekey = dbKey + "#" + logicTableName;

        List<String> tableIndexesList = new ArrayList<String>(0);
        tableIndexesList = tablesCache.get(tablesCachekey);
        //去缓存里查，如果查到就返回
        if (tableIndexesList != null && tableIndexesList.size() != 0) {
            if (logger.isDebugEnabled()) {
                logger.debug("根据dbKey=" + dbKey + "获取到逻辑表名" + logicTableName + "所对应的物理表名集合，分别是："
                             + tableIndexesList.toString());

            }
            return tableIndexesList;
        } else {
            //如果查不到，就说明还没访问过该库上的该逻辑表
            DatabaseChoicer databaseChoicer = getDatabaseChoicer();
            if (databaseChoicer == null) {
                throw new IllegalArgumentException("The dispatcher is null,dbKey=" + dbKey
                                                   + ",logicTableName=" + logicTableName);
            }
            Result result = databaseChoicer.getAllDatabasesAndTables(logicTableName);
            if (result == null) {
                throw new IllegalArgumentException("The result is null,dbKey=" + dbKey
                                                   + ",logicTableName=" + logicTableName);
            }
            List<TargetDB> targetDBs = result.getTarget();
            if (targetDBs == null || targetDBs.size() == 0) {
                throw new IllegalArgumentException("TargetDBs can not be null,dbKey=" + dbKey
                                                   + ",logicTableName=" + logicTableName);
            }
            //遍历各个分库，获取对应的分表情况
            for (TargetDB targetDB : targetDBs) {
                List<String> list = new ArrayList<String>(0);
                for (String tableName : targetDB.getTableNames()) {
                    list.add(tableName);
                }
                String dbTag = targetDB.getDbIndex();
                if (StringUtil.isBlank(dbTag)) {
                    throw new IllegalArgumentException(
                        "The dbTag can't be null, please check the configure,dbKey=" + dbKey
                                + ",logicTableName=" + logicTableName);
                }
                String key = dbTag + "#" + logicTableName;
                tablesCache.put(key, list);
            }
            return tablesCache.get(tablesCachekey);
        }
    }

    /**
     * 根据表名以及传入的参数，获取对应的分组号
     * @param tableName      逻辑表名
     * @param parameters     参数对
     * @return               分组号
     */
    public int getShardingResultByTableName(String tableName, Map<String, String> parameters) {
        if (StringUtil.isBlank(tableName)) {
            throw new IllegalArgumentException("The key can't be null!");
        }
        TableShardingRuleImpl tableShardingRuleImpl = new TableShardingRuleImpl();
        tableShardingRuleImpl.put(parameters);
        RuleChain rc = this.getShardingRuleChainByTableName(tableName);
        String result = tableShardingRuleImpl.getShardingResult(rc);
        if (StringUtil.isBlank(result)) {
            throw new IllegalArgumentException("The result can not be null!");
        }
        return Integer.valueOf(result);
    }

    /**
     * 根据传入的参数，获取对应的分组号
     * @param parameters     参数
     * @return               分组号
     */
    private int getShardingResult(Map<String, String> parameters) {
        TableShardingRuleImpl tableShardingRuleImpl = new TableShardingRuleImpl();
        tableShardingRuleImpl.put(parameters);
        RuleChain rc = this.getSingleShardingRuleChain();
        String result = tableShardingRuleImpl.getShardingResult(rc);
        if (StringUtil.isBlank(result)) {
            throw new IllegalArgumentException("The result can not be null!");
        }
        return Integer.valueOf(result);
    }

    /**
     * 根据逻辑表名获取规则链
     * @param tableName 逻辑表名
     * @return shardingRule规则链对象
     */
    private RuleChain getShardingRuleChainByTableName(String tableName) {
        if (StringUtil.isBlank(tableName)) {
            throw new IllegalArgumentException("The tableName can't be null!");
        }
        RuleChain rc = ruleChainCache.get(tableName);
        if (rc == null) {
            Map<String, TableRule> tableRules = getTableRules();
            TableRule tableRule = tableRules.get(tableName);
            rc = getRuleChainByTableRule(tableRule, tableName);
            if (rc == null) {
                throw new IllegalArgumentException("The shardingRules property don't be set!");
            }
            ruleChainCache.put(tableName, rc);
        }
        return rc;

    }

    /**
     * 获取单一的shardingRule所对应的规则链,遍历所有的分库分表规则，
     * 如果发现有一个规则配置了shardingRule，便以它作为获取规则链的入参
     * @return          单一规则链
     */
    private RuleChain getSingleShardingRuleChain() {
        //如果singleRuleChain为空，则遍历所有表规则
        if (singleRuleChain == null) {

            Map<String, TableRule> tableRules = getTableRules();
            for (Map.Entry<String, TableRule> entry : tableRules.entrySet()) {
                String tableName = entry.getKey();
                TableRule tableRule = entry.getValue();
                singleRuleChain = getRuleChainByTableRule(tableRule, tableName);
                //如果有一条规则配置了shardingRule
                if (singleRuleChain != null) {
                    break;
                }
            }
            //如果发现所有的规则都米有配置shardingRule
            if (singleRuleChain == null) {
                throw new IllegalArgumentException("The shardingRules property don't be set!");
            }
        }
        return singleRuleChain;
    }

    /**
     * 根据分库分表规则，获取shardingRule对应的规则链
     * @param tableRule     分库分表规则
     * @param tableName     逻辑表名
     * @return   shardingRule对应的规则链
     */
    private RuleChain getRuleChainByTableRule(TableRule tableRule, String tableName) {
        //tableRule 一个逻辑表名对应着一套分库分表规则，故此属性不可为空
        if (tableRule != null) {
            List<Object> sharidngRuleStringList = tableRule.getShardingRules();
            if (sharidngRuleStringList != null && sharidngRuleStringList.size() != 0) {
                return RuleUtils.getRuleChainByRuleStringList(sharidngRuleStringList,
                    GroovyListRuleEngine.class, false);
            }
        } else {
            throw new IllegalArgumentException("The tableRule object can not be null,tableName="
                                               + tableName);
        }
        return null;
    }

    /**
     * 获取所有的分库分表规则
     * @return  分库分表规则
     */
    private Map<String, TableRule> getTableRules() {
        AppRule appRule = getTargetDataSource().getAppRule();
        if (appRule == null) {
            throw new IllegalArgumentException("The appRule can't be null!");
        }
        ShardRule shardRule = appRule.getMasterRule() != null ? appRule.getMasterRule() : appRule
            .getSlaveRule();
        if (shardRule == null) {
            throw new IllegalArgumentException("The sharding rule can't be null!");
        }
        Map<String, TableRule> tableRules = shardRule.getTableRules();
        if (tableRules == null || tableRules.size() == 0) {
            throw new IllegalArgumentException("Please set the tableRules property!");
        }
        return tableRules;

    }

    /**
     * 根据逻辑表名返回该表名所对应的所有的物理表名集合<br>
     * @param logicTableName  逻辑表名<br>
     * @return                返回所有的物理表名，key为库的标识，value为list<String>，代表该库上的物理表的集合
     */
    public Map<String, List<String>> getAllTableNames(String logicTableName) {
        Map<String, List<String>> map = new HashMap<String, List<String>>(0);
        DatabaseChoicer databaseChoicer = getDatabaseChoicer();
        if (databaseChoicer == null) {
            throw new IllegalArgumentException("The dispatcher is null,logicTableName="
                                               + logicTableName);
        }
        Result result = databaseChoicer.getAllDatabasesAndTables(logicTableName);
        if (result == null) {
            throw new IllegalArgumentException("The result is null,logicTableName="
                                               + logicTableName);
        }
        List<TargetDB> targetDBs = result.getTarget();
        //遍历各个分库，获取对应的分表情况
        for (TargetDB targetDB : targetDBs) {
            List<String> list = new ArrayList<String>(0);
            for (String tableName : targetDB.getTableNames()) {
                list.add(tableName);
            }
            map.put(targetDB.getDbIndex(), list);
        }
        return map;
    }

    /**
     * 根据dbNumber判断db是否可用
     * @param dbNumber  db序列号
     * @param groupNum  分组号
     * @return          是否可用
     */
    public boolean isDataBaseAvailable(int dbNumber, int groupNum) {
        ZdalDataSourceKeyWeightRandom ZdalDataSourceKeyWeightRandom = getZdalDataSourceKeyWeightRandom(groupNum);
        //获取该db对应的标识dbKey
        int dbWeight = ZdalDataSourceKeyWeightRandom.getDBWeightByNumber(dbNumber);
        return dbWeight > 0 ? true : false;
    }

    /**
     * 根据权重获取不可用的数据库序列集合
     * 如果db故障了，只有权重推送了之后，才能置为逻辑上不可用
     * 此方法的返回结果依赖于推送行为
     * @return   返回不可用的库的标号，其中类型为 Integer。
     */
    public List<Integer> getNotAvailableDBIndexes(int groupNum) {
        ZdalDataSourceKeyWeightRandom ZdalDataSourceKeyWeightRandom = getZdalDataSourceKeyWeightRandom(groupNum);
        return ZdalDataSourceKeyWeightRandom.getNotAvailableDBIndexes();
    }

    /**
     * 根据权重获取可用的数据库序列集合
     * 如果db故障了，只有权重推送了之后，才能置为逻辑上不可用
     * 此方法的返回结果依赖于推送行为
     * @return  返回可用的db的标号集合。
     */
    public List<Integer> getAvailableDBIndexes(int groupNum) {
        ZdalDataSourceKeyWeightRandom ZdalDataSourceKeyWeightRandom = getZdalDataSourceKeyWeightRandom(groupNum);
        return ZdalDataSourceKeyWeightRandom.getAvailableDBIndexes();
    }

    /**
     * 根据组号获取随机器对象
     * @param groupNum     组号<br>
     * @return      ZdalDataSourceKeyWeightRandom对象
     */
    private ZdalDataSourceKeyWeightRandom getZdalDataSourceKeyWeightRandom(int groupNum) {
        Map<String, ZdalDataSourceKeyWeightRandom> keyWeightMapConfig = getTargetDataSource()
            .getKeyWeightMapConfig();
        if (keyWeightMapConfig == null) {
            throw new IllegalArgumentException(
                "Please check the *-db.xml,property keyWeightMapConfig not set!");
        }
        if (groupNum < 0 || groupNum > keyWeightMapConfig.size() - 1) {
            throw new IllegalArgumentException("The groupNum can't be more than "
                                               + (keyWeightMapConfig.size() - 1) + ",it is"
                                               + groupNum);
        }
        String groupNo = Constants.DBINDEX_DS_GROUP_KEY_PREFIX + groupNum;
        ZdalDataSourceKeyWeightRandom ZdalDataSourceKeyWeightRandom = keyWeightMapConfig
            .get(groupNo);
        if (ZdalDataSourceKeyWeightRandom == null) {
            throw new IllegalArgumentException("Please check the configure,the groupNum is "
                                               + groupNo);
        }
        return ZdalDataSourceKeyWeightRandom;
    }

    /**
     * 获取数据源管理器 <br>
     * 
     * @return    DatabaseChoicer
     */
    private DatabaseChoicer getDatabaseChoicer() {
        AbstractZdalDataSource tds = getTargetDataSource();
        return tds.getWriteDispatcher() != null ? tds.getWriteDispatcher() : tds
            .getReadDispatcher();
    }

    /**
     * 获取zdal数据源
     * 
     * @return  zdal封装后的数据源
     */
    public AbstractZdalDataSource getTargetDataSource() {
        if (targetDataSource == null) {
            throw new IllegalArgumentException("The targetDataSource can't be null!");
        }
        return targetDataSource;
    }

    /**
     * 设置数据源
     * 
     * @param targetDataSource 目标数据源
     */
    public void setTargetDataSource(AbstractZdalDataSource targetDataSource) {
        this.targetDataSource = targetDataSource;
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
     * Setter method for property <tt>closeDBLimitNumber</tt>.
     * 
     * @param closeDBLimitNumber value to be assigned to property closeDBLimitNumber
     */
    public void setCloseDBLimitNumber(int closeDBLimitNumber) {
        this.closeDBLimitNumber = closeDBLimitNumber;
    }

    /**
     * Getter method for property <tt>closeDBLimitNumber</tt>.
     * 
     * @return property value of closeDBLimitNumber
     */
    public int getCloseDBLimitNumber() {
        return closeDBLimitNumber;
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
