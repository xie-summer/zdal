/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.rule.ruleengine.rule;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 存放列名->sourceKey的映射。
 * 
 * 多添加一个Set。用于特殊场景的运算。
 * 
 * 这个set的主要作用就是存放sourceKey的同时，也存放映射后的结果。这个结果是在mapping rule中查tair以后产生的，为了减少一次查
 * 
 * tair的过程，因此要记录下查tair以后的值都是哪些，并且按照结果进行分类。
 * 
 * 因为映射规则只允许列名唯一，不允许多列参与运算。
 * 
 * 在列名有且仅有一个的情况下。set中的targetValue应该就是sourceKey通过tair映射以后的结果。
 * 
 * 在其他情况下,mappingKeys应该永远为空。
 * 
 * 这样写的作用是不污染现有sourceKeys。减少逻辑改动
 * 
 *
 */
public class Field {
    public Field(int capacity) {
        sourceKeys = new HashMap<String, Set<Object>>(capacity);
    }

    public Map<String/* 列名 */, Set<Object>/* 得到该结果的描点值名 */> sourceKeys;
    /**
    * 用于映射规则中存放映射后的所有值，这些值都应该有相同的列名，对应mappingTargetColumn
    */
    public Set<Object>                                        mappingKeys;
    /**
    * 对应上述mappingKeys的targetColumn
    */
    public String                                             mappingTargetColumn;

    public static final Field                                 EMPTY_FIELD = new Field(0);
}
