/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.rule.ruleengine.entities.abstractentities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

/**
 * 一对多节点抽象。
 * 
 * 持有一个一对多的映射map,会根据输入的本节点所持有的规则链，
 * 以及运算结果，决定哪些节点是符合要求，然后计算并返回
 * 
 *
 */
public abstract class ListSharedElement extends SharedElement {

    protected static final Logger log = Logger.getLogger(ListSharedElement.class);

    public enum DEFAULT_LIST_RESULT_STRAGETY {
        /**
         * 默认全子集查询
         */
        FULL_TABLE_SCAN,

        /**
         * 默认一个都不选择
         */
        NONE
    }

    /**
     * 默认子节点的选择方式。全表
     */
    public DEFAULT_LIST_RESULT_STRAGETY            defaultListResultStragety;

    /**
     * 如果没有找到指定的ListResult的时候，应该应该返回的节点的信息
     */
    protected List<String>                         defaultListResult;

    /**
     * 结果为一个list 数组的计算式，是一个规则链，自低到高，规则优先级依次降低
     */
    //TODO:改名儿
    protected RuleChain                            listResultRule;

    /**
     * 子节点Map
     * 		如果直接以map形式指定则key会在init方法中被设置到子节点作为子节点的id
     * 
     * 		如果以list形式指定，则下标会成为这个map的key,并在init方法中被设置到子节点作为id.
     */
    protected Map<String, ? extends SharedElement> subSharedElement = Collections.emptyMap();

    /**
     * 像LogicMap注册自己的函数。
     * 
     * 拉平库表规则链，放在LogicTable里面,这样在matcher中就可以知道所有规则所需要的参数是否都具备了。
     * 
     * 所谓matcher就是根据规则链里的规则所需要的参数，去sql中找寻与所要求参数相匹配的列名+参数。
     *
     * 放在一个context中在计算时会用到
     * 
     * @param ruleSet
     */
    public void registeRule(Set<RuleChain> ruleSet) {

        registeSubSharedElement(ruleSet);

        registeCurrentSharedElement(ruleSet);
    }

    /** 注册当前节点
     * @param ruleSet
     */
    private void registeCurrentSharedElement(Set<RuleChain> ruleSet) {
        if (this.listResultRule != null) {
            ruleSet.add(this.listResultRule);
        }
    }

    /**
     * 如果有子节点则注册子节点
     * 
     * @param ruleSet
     */
    private void registeSubSharedElement(Set<RuleChain> ruleSet) {
        for (SharedElement sharedElement : subSharedElement.values()) {
            if (sharedElement instanceof ListSharedElement) {
                ((ListSharedElement) sharedElement).registeRule(ruleSet);
            }
        }
    }

    public void setSubSharedElement(Map<String, ? extends SharedElement> subSharedElement) {
        this.subSharedElement = subSharedElement;
    }

    /** 
     * @see com.alipay.zdal.rule.ruleengine.entities.abstractentities.SharedElement#init()
     * 如果有databaseMapProvider =>初始化databaseMap
     */
    public void init() {

        Map<String, ? extends SharedElement> subSharedElements = fillNullSubSharedElementWithEmptyList();

        initDefaultSubSharedElementsListRule();

        setChildIdByUsingMapKey(subSharedElements);

        super.init();

    }

    private void setChildIdByUsingMapKey(Map<String, ? extends SharedElement> subSharedElements) {
        for (Entry<String, ? extends SharedElement> sharedElement : subSharedElements.entrySet()) {
            //将map中的key作为id设置给子节点
            sharedElement.getValue().setId(sharedElement.getKey());
            initOneSubSharedElement(sharedElement.getValue());
        }
    }

    protected Map<String, ? extends SharedElement> fillNullSubSharedElementWithEmptyList() {
        if (this.subSharedElement == null) {
            this.subSharedElement = Collections.emptyMap();
        }
        return subSharedElement;
    }

    protected void initOneSubSharedElement(SharedElement sharedElement) {
        sharedElement.init();
    }

    /**
     * 初始化默认规则key的列表。
     * 
     * 会按照既定策略初始化，这样如果规则运算中不能获得target 子规则的话
     * 
     * 就会使用默认规则。
     * 
     */
    protected void initDefaultSubSharedElementsListRule() {
        if (defaultListResultStragety == null) {
            if (log.isDebugEnabled()) {
                log.debug("default stragety is null ,use none stragety .");
            }
            defaultListResultStragety = DEFAULT_LIST_RESULT_STRAGETY.NONE;
        }

        switch (defaultListResultStragety) {
            case FULL_TABLE_SCAN:
                buildFullTableKeysList();
                break;
            case NONE:
                //fix by shen: 需要注意的是空表规则和的时候，应该返回唯一的节点而不是空节点。
                if (listResultRule == null || listResultRule.getListResultRule() == null
                    || listResultRule.getListResultRule().isEmpty()) {
                    if (subSharedElement.size() == 1) {
                        log.warn("NONE stragety ,current element has only one SubElement,use "
                                 + "full table stragety! subElement is " + subSharedElement);
                        buildFullTableKeysList();
                    } else {
                        log
                            .warn("NONE stragety ,current element has more than one SubElement,use empty"
                                  + " default stragety! subElement is " + subSharedElement);
                        defaultListResult = Collections.emptyList();
                    }

                } else {
                    defaultListResult = Collections.emptyList();
                }
                break;
            default:
                throw new IllegalArgumentException("不能处理的类型");
        }
    }

    protected void buildFullTableKeysList() {
        int subSharedElementSize = 0;
        if (subSharedElement == null) {
            subSharedElement = Collections.emptyMap();
        }
        subSharedElementSize = subSharedElement.size();
        defaultListResult = new ArrayList<String>(subSharedElementSize);
        if (log.isDebugEnabled()) {
            log.debug("use full table stragety, default keys are :");
        }
        StringBuilder sb = new StringBuilder();
        for (String key : subSharedElement.keySet()) {
            sb.append(key).append("|");
            defaultListResult.add(key);
        }
        if (log.isDebugEnabled()) {
            log.debug(sb.toString());
        }
    }

    public Map<String, ? extends SharedElement> getSubSharedElements() {
        return subSharedElement;
    }

    public DEFAULT_LIST_RESULT_STRAGETY getDefaultListResultStragety() {
        return defaultListResultStragety;
    }

    public void setDefaultListResultStragety(DEFAULT_LIST_RESULT_STRAGETY defaultListResultStragety) {
        this.defaultListResultStragety = defaultListResultStragety;
    }

    @Override
    public String toString() {
        return "ListSharedElement [defaultListResult=" + defaultListResult
               + ", defaultListResultStragety=" + defaultListResultStragety + ", listResultRule="
               + listResultRule + ", subSharedElement=" + subSharedElement + "]";
    }

}
