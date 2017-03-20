/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.rule.ruleengine.enumerator;

import java.util.HashSet;
import java.util.Set;

import com.alipay.zdal.common.sqljep.function.Comparative;

public class IntegerPartDiscontinousRangeEnumerator extends PartDiscontinousRangeEnumerator {
    private static final int LIMIT_UNIT_OF_INT    = 1;
    public static final int  DEFAULT_ATOMIC_VALUE = 1;

    @Override
    protected Comparative changeGreater2GreaterOrEq(Comparative from) {
        if (from.getComparison() == Comparative.GreaterThan) {

            int fromComparable = (Integer) from.getValue();

            return new Comparative(Comparative.GreaterThanOrEqual, fromComparable
                                                                   + LIMIT_UNIT_OF_INT);
        } else {
            return from;
        }
    }

    @Override
    protected Comparative changeLess2LessOrEq(Comparative to) {
        if (to.getComparison() == Comparative.LessThan) {

            int toComparable = (Integer) to.getValue();

            return new Comparative(Comparative.LessThanOrEqual, toComparable - LIMIT_UNIT_OF_INT);
        } else {

            return to;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Comparable getOneStep(Comparable source, Comparable atomIncVal) {
        if (atomIncVal == null) {
            atomIncVal = DEFAULT_ATOMIC_VALUE;
        }
        int sourceInt = (Integer) source;

        int atomIncValInt = (Integer) atomIncVal;

        return sourceInt + atomIncValInt;
    }

    @SuppressWarnings("unchecked")
    protected boolean inputCloseRangeGreaterThanMaxFieldOfDifination(Comparable from,
                                                                     Comparable to,
                                                                     Integer cumulativeTimes,
                                                                     Comparable<?> atomIncrValue) {
        if (cumulativeTimes == null) {
            return false;
        }
        if (atomIncrValue == null) {
            atomIncrValue = DEFAULT_ATOMIC_VALUE;
        }
        int fromInt = (Integer) from;
        int toInt = (Integer) to;
        int atomIncValInt = (Integer) atomIncrValue;
        int size = cumulativeTimes;

        if ((toInt - fromInt) > (atomIncValInt * size)) {
            return true;
        } else {
            return false;
        }
    }

    /** 
     * @see com.alipay.zdal.rule.ruleengine.enumerator.PartDiscontinousRangeEnumerator#getAllPassableFields(com.alipay.zdal.common.sqljep.function.Comparative, java.lang.Integer, java.lang.Comparable)
     */
    @Override
    protected Set<Object> getAllPassableFields(Comparative begin, Integer cumulativeTimes,
                                               Comparable<?> atomicIncreationValue) {
        if (cumulativeTimes == null) {
            throw new IllegalStateException(
                "在没有提供叠加次数的前提下，不能够根据当前范围条件选出对应的定义域的枚举值，sql中不要出现> < >= <=");
        }
        if (atomicIncreationValue == null) {
            atomicIncreationValue = DEFAULT_ATOMIC_VALUE;
        }
        //把> < 替换为>= <=
        begin = changeGreater2GreaterOrEq(begin);
        begin = changeLess2LessOrEq(begin);

        Set<Object> returnSet = new HashSet<Object>(cumulativeTimes);
        int beginInt = (Integer) begin.getValue();
        int comparasion = begin.getComparison();

        int atomicIncreateValueInt = (Integer) atomicIncreationValue;
        if (comparasion == Comparative.GreaterThanOrEqual) {
            for (int i = 0; i < cumulativeTimes; i++) {
                returnSet.add(beginInt + atomicIncreateValueInt * i);
            }
        } else if (comparasion == Comparative.LessThanOrEqual) {
            for (int i = 0; i < cumulativeTimes; i++) {
                returnSet.add(beginInt - atomicIncreateValueInt * i);
            }
        }
        return returnSet;
    }

    public void processAllPassableFields(Comparative source, Set<Object> retValue,
                                         Integer cumulativeTimes, Comparable<?> atomIncrValue) {
        retValue.addAll(getAllPassableFields(source, cumulativeTimes, atomIncrValue));

    }
}
