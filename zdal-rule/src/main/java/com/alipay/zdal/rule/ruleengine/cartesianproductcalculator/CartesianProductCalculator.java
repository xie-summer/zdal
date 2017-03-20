/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.rule.ruleengine.cartesianproductcalculator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 算笛卡尔积
 * 
 * 
 */
public class CartesianProductCalculator implements Parent, Iterable<SamplingField>,
                                       Iterator<SamplingField> {
    /**
     * samplingField在内存里是被复用的，因为他本身只是用于值的传递的，而且是在单线程里面被使用。
     * 他的父class CartesianProductCalculator本身是在每次计算笛卡尔积的时候都会新建的，因此里面的类
     * 是不需要关注线程安全的
     * 
     */
    final SamplingField                                    samplingFieldToBeReturned;
    private boolean                                        hasNext      = true;
    final List<CartesianProductCalculatorElement>          list;
    private boolean                                        firstNext    = true;
    private static final CartesianProductCalculatorElement DEFAULT_CPCE = new CartesianProductCalculatorElement(
                                                                            null, Collections
                                                                                .emptySet());
    /**
     * 最终会是整个笛卡尔积的最右边的一列，也就是自增发起的列
     */
    private CartesianProductCalculatorElement              firstCartesianProductCalculatorElement;

    public CartesianProductCalculator(Map<String, Set<Object>> enumeratedMap) {
        List<String> columnList = new ArrayList<String>(enumeratedMap.size());
        columnList.addAll(enumeratedMap.keySet());

        samplingFieldToBeReturned = new SamplingField(columnList, enumeratedMap.size());

        List<Set<Object>> enumeratedValuesSetOrderByColumnList = new ArrayList<Set<Object>>(
            columnList.size());
        for (String key : columnList) {
            enumeratedValuesSetOrderByColumnList.add(enumeratedMap.get(key));
        }
        list = new ArrayList<CartesianProductCalculatorElement>();
        CartesianProductCalculatorElement parentProductor = null;
        CartesianProductCalculatorElement childrenProductor = null;
        boolean isFirst = true;
        //TODO:这个columnList为空
        if (!enumeratedValuesSetOrderByColumnList.isEmpty()) {
            for (Set<Object> set : enumeratedValuesSetOrderByColumnList) {
                //parent
                parentProductor = new CartesianProductCalculatorElement(null, set);
                if (isFirst) {
                    firstCartesianProductCalculatorElement = parentProductor;
                    isFirst = false;
                }
                if (childrenProductor != null) {
                    //children
                    childrenProductor.setParent(parentProductor);
                }
                //children become parent
                childrenProductor = parentProductor;
                list.add(childrenProductor);
            }
            childrenProductor.setParent(this);
        } else {
            firstCartesianProductCalculatorElement = DEFAULT_CPCE;
            firstCartesianProductCalculatorElement.setParent(this);
        }

    }

    public boolean hasNext() {
        return firstCartesianProductCalculatorElement.hasNext() && hasNext;
    }

    public SamplingField next() {
        if (firstNext) {
            // 第一次初始化的时候要所有的笛卡尔积元素都做一次next操作以初始化数据
            for (CartesianProductCalculatorElement element : list) {
                // TODO:测试如果有两列都无法next的情况下的行为
                if (element.hasNext()) {
                    element.init();
                }
            }
            firstNext = false;
        } else {
            if (firstCartesianProductCalculatorElement.hasNext()) {
                firstCartesianProductCalculatorElement.next();
            }
        }
        samplingFieldToBeReturned.clear();
        int index = 0;
        for (CartesianProductCalculatorElement element : list) {

            samplingFieldToBeReturned.add(index, element.currentObject);
            index++;
        }
        return samplingFieldToBeReturned;

    }

    public boolean parentHasNext() {
        //传递到头的时候表示没有下一个了
        return false;
    }

    public void add() {
        throw new IllegalStateException("should not be here");
    }

    public void remove() {
        throw new IllegalStateException("should not be here");

    }

    public Iterator<SamplingField> iterator() {
        return this;
    }

}
