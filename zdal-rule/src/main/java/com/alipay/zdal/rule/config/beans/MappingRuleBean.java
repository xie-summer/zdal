/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.rule.config.beans;

public class MappingRuleBean {
    //	private String originalColumn;
    //	private String targetRule;
    //	private String dbIndex; //默认用本TDatasource
    //	private String routeTable; //路由表（映射表）key-value表
    //	private String routeColumn;//路由表作为key的列名
    //	private String routeTargetColumn; //路由表作为value的列名
    //	private String routeReturnType; //#返回类型(int,long,String)

    private String parameter;
    private String expression;
    private String mappingRuleBeanId;

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getMappingRuleBeanId() {
        return mappingRuleBeanId;
    }

    public void setMappingRuleBeanId(String mappingRuleBeanId) {
        this.mappingRuleBeanId = mappingRuleBeanId;
    }
}
