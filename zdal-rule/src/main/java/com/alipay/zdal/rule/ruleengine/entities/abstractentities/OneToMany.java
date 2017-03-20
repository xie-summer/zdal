/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.rule.ruleengine.entities.abstractentities;

public interface OneToMany {
    /**
     * 1对多传递者
     * @param oneToManyEntry
     */
    void put(OneToManyEntry oneToManyEntry);
}
