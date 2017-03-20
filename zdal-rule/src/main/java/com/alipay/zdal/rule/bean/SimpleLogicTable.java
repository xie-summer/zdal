/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.rule.bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import com.alipay.zdal.rule.groovy.GroovyListRuleEngine;
import com.alipay.zdal.rule.ruleengine.entities.abstractentities.RuleChain;
import com.alipay.zdal.rule.ruleengine.entities.convientobjectmaker.DefaultTableMapProvider;
import com.alipay.zdal.rule.ruleengine.util.RuleUtils;

/**
 * 简单表规则，用于实现最基础的规则的集合，会将一些常用属性拉平，
 * 
 * 构造好以后变成运行时可以使用的运行时数据，然后进行运行。
 * 
 *
 */
public class SimpleLogicTable extends LogicTable {
    private static final Logger    log                       = Logger
                                                                 .getLogger(SimpleLogicTable.class);

    String                         databases;

    /**
     * 分库键
     */
    String                         shardingKey;

    List<Object>                   tableRuleStringList;

    List<Object>                   databaseRuleStringList;

    boolean                        isSimpleTableMapPropertiesChanged;

    /**
     * Simple logic table的内部对象,用于记录需要传递给子类的表规则的信息。
     * 
     *TODO: 不知道为什么支付宝的同志把这个东西去掉了。。
     */
    private SimpleTableMapProvider simpleTableMapProvider    = new SimpleTableMapProvider();

    SimpleListDatabaseMapProvider  simpleDatabaseMapProvider = new SimpleListDatabaseMapProvider();

    /**
     * 是否使用自动生成规则，一般来说用不上
     * 只是为了强制给业务一个不使用自动规则的选项，以备不时之需。
     */
    boolean                        useAutoGeneratingRule     = true;

    /**
     * 当分库键和分表键都指定的情况下，
     * 自动生成所有规则，只要分库键和分表键有一个指定了。那么就需要自动生成规则。
     * 
     * 自动生成规则会覆盖所有已经存在的规则。
     *
     * 
     * @return 是否可以使用自动规则生成
     */
    protected boolean canUseAutoGenerationRule() {
        if (!useAutoGeneratingRule) {
            return false;
        }
        if (shardingKey == null) {
            return false;
        }
        return true;
    }

    protected void valid(int databasesSize, int tableSizeForEachDatabase) {
        if (databasesSize == 0 || tableSizeForEachDatabase == 0) {
            //分库键和分表键为0，可能是由于分库或分表没有指定。这个时候是不需要拼接的
            return;
        }
        int dividend = 0;
        int divisor = 0;
        if (databasesSize > tableSizeForEachDatabase) {
            dividend = databasesSize;
            divisor = tableSizeForEachDatabase;
        } else {
            dividend = tableSizeForEachDatabase;
            divisor = databasesSize;
        }
        if (dividend % divisor != 0) {
            throw new IllegalArgumentException("分表个数必须是分库个数的倍数," + "分库是:" + databasesSize + "分表是:"
                                               + tableSizeForEachDatabase);
        }
    }

    static class TableAGRuleHandler extends DatabaseAndTableAGRuleHandler implements
                                                                         AutoGenerationRuleHandler {

        @Override
        public String getTableRule(String tableShardingKey, int databaseSize,
                                   int tableSizeForEachDatabase) {
            StringBuilder sb = new StringBuilder();

            sb.append("#").append(tableShardingKey).append("#");
            if (tableSizeForEachDatabase != 0) {
                sb.append(" % ").append(tableSizeForEachDatabase);
            }
            return sb.toString();
        }

        public String getDatabaseRule(String databaseShardingKey, int tablesSize,
                                      int tableSizeForEachDatabase) {
            return null;
        }
    }

    static class DatabaseAGRuleHandler extends DatabaseAndTableAGRuleHandler implements
                                                                            AutoGenerationRuleHandler {
        @Override
        public String getTableRule(String tableShardingKey, int databaseSize,
                                   int tableSizeForEachDatabase) {
            return null;
        }

        @Override
        public String getDatabaseRule(String databaseShardingKey, int databaseSize,
                                      int tableSizeForEachDatabase) {
            StringBuilder sb = new StringBuilder();
            sb.append("#").append(databaseShardingKey).append("#");
            if (databaseSize != 0) {
                sb.append(" % ").append(databaseSize);
            }
            sb.append("");
            return sb.toString();
        }
    }

    static class NoneAGRuleHandler implements AutoGenerationRuleHandler {

        public String getDatabaseRule(String databaseShardingKey, int tablesSize,
                                      int tableSizeForEachDatabase) {
            return null;
        }

        public String getTableRule(String tableShardingKey, int tablesSize,
                                   int tableSizeForEachDatabase) {
            return null;
        }

    }

    static class DatabaseAndTableAGRuleHandler implements AutoGenerationRuleHandler {

        public String getDatabaseRule(String databaseShardingKey, int databaseSize,
                                      int tableSizeForEachDatabase) {
            int tablesSize = databaseSize * tableSizeForEachDatabase;
            StringBuilder sb = new StringBuilder();
            sb.append("(#").append(databaseShardingKey).append("#");
            if (tablesSize != 0) {
                sb.append(" % ").append(tablesSize);
            }
            sb.append(")");
            if (tableSizeForEachDatabase != 0) {
                sb.append(".intdiv(").append(tableSizeForEachDatabase).append(")");
            }
            return sb.toString();
        }

        public String getTableRule(String tableShardingKey, int databaseSize,
                                   int tableSizeForEachDatabase) {
            int tablesSize = databaseSize * tableSizeForEachDatabase;

            StringBuilder sb = new StringBuilder();

            sb.append("(#").append(tableShardingKey).append("#");
            if (tablesSize != 0) {
                sb.append(" % ").append(tablesSize);
            }
            sb.append(")");
            if (tableSizeForEachDatabase != 0) {
                sb.append(" % ").append(tableSizeForEachDatabase);
            }
            return sb.toString();
        }
    }

    static interface AutoGenerationRuleHandler {
        /**
         * 获取分表规则
         * @param tableShardingKey 分表键
         * @param tablesSize 表的总个数
         * @param tableSizeForEachDatabase 每个库的表的个数
         * @return
         */
        String getTableRule(String tableShardingKey, int tablesSize, int tableSizeForEachDatabase);

        /**获取分库规则
         * 
         * @param tableShardingKey 分表键
         * @param tablesSize 表的总个数
         * @param tableSizeForEachDatabase 每个库的表的个数
         * 
         * @return
         */
        String getDatabaseRule(String databaseShardingKey, int tablesSize,
                               int tableSizeForEachDatabase);
    }

    AutoGenerationRuleHandler decideAutoGenerationRuleHandler(int databaseSize,
                                                              int tableSizeForEachDatabase) {
        if (databaseSize <= 0 || tableSizeForEachDatabase <= 0) {
            throw new IllegalArgumentException("最少需要一个库,一张表");
        }
        if (databaseSize == 1) {
            if (tableSizeForEachDatabase == 1) {
                //单库单表构造
                return new NoneAGRuleHandler();
            } else {
                //单库多表构造
                return new TableAGRuleHandler();
            }
        } else {
            if (tableSizeForEachDatabase == 1) {
                //多库单表
                return new DatabaseAGRuleHandler();
            } else {
                //多库多表
                return new DatabaseAndTableAGRuleHandler();
            }
        }
    }

    /**
     * 自动生成规则。
     * 
     * 当指定了一个databaseKey或tableKey的时候触发。
     * 
     * 自动生成一条规则。
     * 
     * 然后如果没有通过外部来指定String规则的话。就会使用当前规则来替代外部规则。
     * 
     * 如果有外部指定的规则，那么使用外部规则。
     * 
     * 优先级最低的一种规则生成器。
     */
    protected void processAutoGenerationRule() {
        if (!canUseAutoGenerationRule()) {
            return;
        }
        int databaseSize = simpleDatabaseMapProvider.getDatasourceKeys().size();
        int tablesNumberForEachDatabases = getTablesNumberForEachDatabases();
        valid(databaseSize, tablesNumberForEachDatabases);
        //所有库的总表个数
        AutoGenerationRuleHandler agrHandler = decideAutoGenerationRuleHandler(databaseSize,
            tablesNumberForEachDatabases);
        String dbRule = agrHandler.getDatabaseRule(shardingKey, databaseSize,
            tablesNumberForEachDatabases);
        //只有当databaseRuleStringList为null的情况下才用默认规则替换之。
        if (dbRule != null && this.databaseRuleStringList == null) {
            this.databaseRuleStringList = new ArrayList<Object>(1);
            if (log.isDebugEnabled()) {
                log.debug("auto generation rule for database: " + dbRule);
            }
            databaseRuleStringList.add(dbRule);
        }
        String tableRule = agrHandler.getTableRule(shardingKey, databaseSize,
            tablesNumberForEachDatabases);
        if (tableRule != null) {
            this.tableRuleStringList = new ArrayList<Object>(1);
            if (log.isDebugEnabled()) {
                log.debug("auto generation rule for database: " + tableRule);
            }
            tableRuleStringList.add(tableRule);
        }
    }

    protected int getTablesNumberForEachDatabases() {
        int tablesNumberForEachDatabases = simpleTableMapProvider.getTablesNumberForEachDatabases();
        if (tablesNumberForEachDatabases == SimpleTableMapProvider.DEFAULT_TABLES_NUM_FOR_EACH_DB) {
            //如果每个库内表个数等于默认值，那么应该是to-from+1这么多张表
            tablesNumberForEachDatabases = simpleTableMapProvider.getTo()
                                           - simpleTableMapProvider.getFrom() + 1;
        }
        return tablesNumberForEachDatabases;
    }

    @Override
    public void init() {

        boolean isDatabase = true;
        //请注意这里不要随意颠倒初始化顺序
        if (superClassDatabaseProviderIsNull()) {

            setSimpleDatabaseMapToSuperLogicTable();
        }

        if (superClassTableMapProviderIsNull()) {
            if (isSimpleTableMapPropertiesChanged)
                setTableMapProvider(this.simpleTableMapProvider);
        }

        processAutoGenerationRule();

        if (superClassDatabaseRuleIsNull()) {
            RuleChain rc = RuleUtils.getRuleChainByRuleStringList(databaseRuleStringList,
                GroovyListRuleEngine.class, isDatabase);

            super.listResultRule = rc;
        }

        if (transmitterTableRuleIsNull()) {
            RuleChain rc = RuleUtils.getRuleChainByRuleStringList(tableRuleStringList,
                GroovyListRuleEngine.class, !isDatabase);
            setTableRuleChain(rc);
        }
        super.init();
    }

    private boolean transmitterTableRuleIsNull() {
        return getTableRule() == null || getTableRule().isEmpty();
    }

    private boolean superClassDatabaseRuleIsNull() {
        return listResultRule == null;
    }

    private boolean superClassTableMapProviderIsNull() {
        return getTableMapProvider() == null
               || getTableMapProvider() instanceof DefaultTableMapProvider;
    }

    protected void setSimpleDatabaseMapToSuperLogicTable() {
        if (databases == null) {
            return;
        }
        String[] databasesTokens = databases.split(",");
        simpleDatabaseMapProvider.setDatasourceKeys(Arrays.asList(databasesTokens));
        setDatabaseMapProvider(simpleDatabaseMapProvider);
    }

    private boolean superClassDatabaseProviderIsNull() {
        return getDatabaseMapProvider() == null;
    }

    /**
     * 与{@linkplain setLogicTableName}含义一致
     * @param logicTable
     */
    public void setLogicTable(String logicTable) {
        setLogicTableName(logicTable);
    }

    public void setPadding(String padding) {
        isSimpleTableMapPropertiesChanged = true;
        simpleTableMapProvider.setPadding(padding);
    }

    public void setParentID(String parentID) {

        isSimpleTableMapPropertiesChanged = true;
        simpleTableMapProvider.setParentID(parentID);
    }

    public void setStep(int step) {

        isSimpleTableMapPropertiesChanged = true;
        simpleTableMapProvider.setStep(step);
    }

    /**
     * 与{@linkplain setLogicTableName}含义一致
     * @param tableFactor
     */
    public void setTableFactor(String tableFactor) {
        setLogicTableName(tableFactor);
    }

    public void setTablesNumberForEachDatabases(int tablesNumberForEachDatabases) {

        isSimpleTableMapPropertiesChanged = true;
        simpleTableMapProvider.setTablesNumberForEachDatabases(tablesNumberForEachDatabases);
    }

    public void setFrom(int from) {

        isSimpleTableMapPropertiesChanged = true;
        simpleTableMapProvider.setFrom(from);
    }

    public void setTo(int to) {

        isSimpleTableMapPropertiesChanged = true;
        simpleTableMapProvider.setTo(to);
    }

    public void setType(String type) {

        isSimpleTableMapPropertiesChanged = true;
        simpleTableMapProvider.setType(type);
    }

    public void setWidth(int width) {

        isSimpleTableMapPropertiesChanged = true;
        simpleTableMapProvider.setWidth(width);
    }

    public String getDatabases() {
        return databases;
    }

    public void setDatabases(String databases) {
        this.databases = databases;
    }

    public List<Object> getTableRuleStringList() {
        return tableRuleStringList;
    }

    public void setTableRuleStringList(List<Object> tableRuleStringList) {
        this.tableRuleStringList = tableRuleStringList;
    }

    public List<Object> getDatabaseRuleStringList() {
        return databaseRuleStringList;
    }

    public void setDatabaseRuleStringList(List<Object> databaseRuleStringList) {
        this.databaseRuleStringList = databaseRuleStringList;
    }

    public boolean isUseAutoGeneratingRule() {
        return useAutoGeneratingRule;
    }

    public void setUseAutoGeneratingRule(boolean useAutoGeneratingRule) {
        this.useAutoGeneratingRule = useAutoGeneratingRule;
    }

    public String getShardingKey() {
        return shardingKey;
    }

    public void setShardingKey(String shardingKey) {
        this.shardingKey = shardingKey;
    }

    public void setSimpleTableMapProvider(SimpleTableMapProvider simpleTableMapProvider) {
        this.simpleTableMapProvider = simpleTableMapProvider;
    }

    @Override
    public String toString() {
        return "SimpleLogicTable [databaseRuleStringList=" + databaseRuleStringList
               + ", databases=" + databases + ", isSimpleTableMapPropertiesChanged="
               + isSimpleTableMapPropertiesChanged + ", shardingKey=" + shardingKey
               + ", simpleDatabaseMapProvider=" + simpleDatabaseMapProvider
               + ", simpleTableMapProvider=" + simpleTableMapProvider + ", tableRuleStringList="
               + tableRuleStringList + ", useAutoGeneratingRule=" + useAutoGeneratingRule
               + ", defaultListResult=" + defaultListResult + ", defaultListResultStragety="
               + defaultListResultStragety + ", listResultRule=" + listResultRule
               + ", subSharedElement=" + subSharedElement + "]";
    }

}
