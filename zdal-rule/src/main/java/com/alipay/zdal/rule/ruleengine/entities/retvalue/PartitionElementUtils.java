/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.rule.ruleengine.entities.retvalue;

import java.util.List;
import java.util.Set;

public class PartitionElementUtils {
    public static <E> void add(List<Set<E>> target, E value) {
        int dbsize = target.size();
        switch (dbsize) {
            //			case 0:
            //				target.add(new HashSet<E>());
            //				target.get(0).add(value);
            //				break;
            case 1:
                target.get(0).add(value);
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    public static <E> void addAll(List<Set<E>> target, Set<E> value) {
        int dbsize = target.size();
        switch (dbsize) {
            //				case 0:
            //					target.add(new HashSet<E>());
            //					target.get(0).addAll(value);
            //					break;
            case 1:
                target.get(0).addAll(value);
                break;
            default:
                throw new IllegalArgumentException();
        }
    }
}
