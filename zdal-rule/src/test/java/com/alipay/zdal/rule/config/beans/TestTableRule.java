/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.alipay.zdal.rule.config.beans;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: TestTableRule.java, v 0.1 2013-3-7 ÉÏÎç10:57:48 Exp $
 */
public class TestTableRule {

    public static void main(String[] args) {
        String dbIndex = "SETBYGROOVY:def list = [];(0..109).each {i ->if(i%11 == 0){ list.add(\"master_\"+\"${i}\".padLeft(3,\"0\"));list.add(\"failover_\"+\"${i}\".padLeft(3,\"0\"));}};(0..109).each { i->if(i%11!=0){list.add(\"master_\"+\"${i}\".padLeft(3,\"0\"));list.add(\"failover_\"+\"${i}\".padLeft(3,\"0\"));}};return list;";
        TableRule tableRule = new TableRule();
        tableRule.setDbIndexes(dbIndex);
        String[] dbIndexArray = tableRule.getDbIndexArray();
        System.out.println(dbIndexArray.length);
        for (String string : dbIndexArray) {
            System.out.println(string);
        }
    }
}
