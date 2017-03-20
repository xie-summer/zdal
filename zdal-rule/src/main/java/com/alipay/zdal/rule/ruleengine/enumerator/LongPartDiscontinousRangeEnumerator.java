/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.rule.ruleengine.enumerator;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.alipay.zdal.common.sqljep.function.Comparative;

public class LongPartDiscontinousRangeEnumerator extends PartDiscontinousRangeEnumerator {
    private static final long LIMIT_UNIT_OF_LONG        = 1l;
    private static final long DEFAULT_LONG_ATOMIC_VALUE = 1l;

    @Override
    protected Comparative changeGreater2GreaterOrEq(Comparative from) {
        if (from.getComparison() == Comparative.GreaterThan) {

            long fromComparable = (Long) from.getValue();

            return new Comparative(Comparative.GreaterThanOrEqual, fromComparable
                                                                   + LIMIT_UNIT_OF_LONG);
        } else {
            return from;
        }
    }

    @Override
    protected Comparative changeLess2LessOrEq(Comparative to) {
        if (to.getComparison() == Comparative.LessThan) {

            long toComparable = (Long) to.getValue();

            return new Comparative(Comparative.LessThanOrEqual, toComparable - LIMIT_UNIT_OF_LONG);
        } else {

            return to;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Comparable getOneStep(Comparable source, Comparable atomIncVal) {
        if (atomIncVal == null) {
            atomIncVal = DEFAULT_LONG_ATOMIC_VALUE;
        }
        long sourceLong = (Long) source;

        int atomIncValLong = (Integer) atomIncVal;

        return sourceLong + atomIncValLong;
    }

    @SuppressWarnings("unchecked")
    protected boolean inputCloseRangeGreaterThanMaxFieldOfDifination(Comparable from,
                                                                     Comparable to,
                                                                     Integer cumulativeTimes,
                                                                     Comparable<?> atomIncrValue) {
        if (cumulativeTimes == null) {
            return false;
        }
        long fromLong = (Long) from;
        long toLong = (Long) to;
        int atomIncValLong = (Integer) atomIncrValue;
        int size = cumulativeTimes;

        if ((toLong - fromLong) > (atomIncValLong * size)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected Set<Object> getAllPassableFields(Comparative begin, Integer cumulativeTimes,
                                               Comparable<?> atomicIncreationValue) {
        if (cumulativeTimes == null) {
            return Collections.emptySet();
        }
        if (atomicIncreationValue == null) {
            atomicIncreationValue = DEFAULT_LONG_ATOMIC_VALUE;
        }
        //把> < 替换为>= <=
        begin = changeGreater2GreaterOrEq(begin);
        begin = changeLess2LessOrEq(begin);

        Set<Object> returnSet = new HashSet<Object>(cumulativeTimes);
        long beginInt = (Long) begin.getValue();
        long atomicIncreateValueInt = (Integer) atomicIncreationValue;
        int comparasion = begin.getComparison();

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
