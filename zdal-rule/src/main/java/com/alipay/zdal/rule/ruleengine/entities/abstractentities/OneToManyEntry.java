/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.rule.ruleengine.entities.abstractentities;

import java.util.Collections;
import java.util.List;

import com.alipay.zdal.rule.bean.RuleChainImp;
import com.alipay.zdal.rule.ruleengine.entities.abstractentities.ListSharedElement.DEFAULT_LIST_RESULT_STRAGETY;
import com.alipay.zdal.rule.ruleengine.entities.convientobjectmaker.DefaultTableMapProvider;
import com.alipay.zdal.rule.ruleengine.entities.convientobjectmaker.TableMapProvider;
import com.alipay.zdal.rule.ruleengine.rule.ListAbstractResultRule;

/**
 * 传递+扩散一些属性到指定的子节点中
 * 
 * 里面是一些一对多的分发规则，设置到里面的所有规则。
 * 
 * 没有特别的意义，只是一个对一类事物的抽象，会发现在logictable或database层会有一些属性需要传递给所有子节点。
 * 
 * 所有这类数据都可以放到这个逻辑对象中。
 * 
 * 
 * 
 * 
 */
public class OneToManyEntry {

    /**
     * 传递给生成表名的便捷方法。
     */
    private String                         logicTableName;

    /**
     * 待分分发的表规则
     */
    protected RuleChain                    transmitedtableRuleChain;
    /**
     * 是表规则的另外一种表现形式
     */
    protected List<ListAbstractResultRule> tableRuleList;
    /**
     * 表生成器，被database节点使用
     */
    private TableMapProvider               tableMapProvider = new DefaultTableMapProvider();
    DEFAULT_LIST_RESULT_STRAGETY           defaultListResultStragety;
    private boolean                        isInited         = false;

    // /**
    // * 负责将一些需要扩散的数据从父传输到子节点
    // * 这个方法在init方法之前被调用
    // */
    // public void transmit() {
    // for (SharedElement sharedElement : subSharedElement.values()) {
    // if (isCurrentSubSharedElementGlovalEntryTransmitter(sharedElement)) {
    //
    // OneToManyEntry transmitter = upcastSubEntry(sharedElement);
    // //表规则传递
    // transmitter.setTableRuleChain(transmitedtableRuleChain);
    // // 用于便捷的构造表
    // transmitter.setLogicTableName(logicTableName);
    // // 将table map生成器传递下去
    // transmitter.setTableMapProvider(tableMapProvider);
    // }
    // }
    // }
    //
    // private OneToManyEntry upcastSubEntry(SharedElement sharedElement) {
    // OneToManyEntry transmitter = (OneToManyEntry) sharedElement;
    // return transmitter;
    // }
    //
    // private boolean isCurrentSubSharedElementGlovalEntryTransmitter(
    // SharedElement sharedElement) {
    // return sharedElement instanceof OneToManyEntry;
    // }

    public List<ListAbstractResultRule> getTableRule() {
        if (transmitedtableRuleChain != null) {
            return transmitedtableRuleChain.getListResultRule();
        } else {
            return Collections.emptyList();
        }

    }

    /**
     * 这个方法在默认的情况下，目标是transmitedtableRuleChain.
     * 但在database层覆盖了这个方法，把目标指向每一个database的tableRule.
     * 
     * 本方法会被transmit方法调用。
     * 
     * @param tableRule
     */
    public void setTableRuleChain(RuleChain ruleChain) {
        this.transmitedtableRuleChain = ruleChain;
    }

    public RuleChain getTableRuleChain() {
        return transmitedtableRuleChain;
    }

    public void setTableRule(List<ListAbstractResultRule> tableRule) {
        tableRuleList = tableRule;
    }

    public void init() {
        if (isInited) {
            return;
        }
        isInited = true;
        if (tableRuleList != null) {
            RuleChainImp ruleChainImp = getRuleChain(tableRuleList);

            if (transmitedtableRuleChain == null) {
                transmitedtableRuleChain = ruleChainImp;
            } else {
                throw new IllegalArgumentException("ruleChain已经有数据了，但仍然尝试使用init来进行初始化");
            }
        }
        if (transmitedtableRuleChain != null) {
            transmitedtableRuleChain.init();
        }

    }

    public static RuleChainImp getRuleChain(List<ListAbstractResultRule> tableRuleList) {
        RuleChainImp ruleChainImp = new RuleChainImp();
        ruleChainImp.setDatabaseRuleChain(false);
        ruleChainImp.setListResultRule(tableRuleList);
        return ruleChainImp;
    }

    public TableMapProvider getTableMapProvider() {
        return tableMapProvider;
    }

    /**
     * 因为tableMapProvider需要传递给每一个database对象，因此如果当前database对象已经有了tableMapProvider
     * 那么就不需要再将外部的tableMapProvider指定给他了
     * 
     * @param tableMapProvider
     */
    public void setTableMapProvider(TableMapProvider tableMapProvider) {
        // 如果输入的tableProvider不为空
        // --并且当前节点的tableMapProvider为空或为默认表规则(也就是默认的以logicTable作为表规则的话)
        // --&& (this.tableMapProvider == null || this.tableMapProvider
        // instanceof DefaultTableMapProvider)
        if (tableMapProvider != null && (!(tableMapProvider instanceof DefaultTableMapProvider))) {
            this.tableMapProvider = tableMapProvider;
        }
    }

    public String getLogicTableName() {
        return logicTableName;
    }

    public void setLogicTableName(String logicTablename) {
        this.logicTableName = logicTablename;
    }

    public DEFAULT_LIST_RESULT_STRAGETY getDefaultListResultStragety() {
        return defaultListResultStragety;
    }

    public void setDefaultListResultStragety(DEFAULT_LIST_RESULT_STRAGETY defaultListResultStragety) {
        this.defaultListResultStragety = defaultListResultStragety;
    }

}
