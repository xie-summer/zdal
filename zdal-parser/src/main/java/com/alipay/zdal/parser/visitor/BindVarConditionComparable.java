/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.visitor;

import java.util.Comparator;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: BindVarConditionComparable.java, v 0.1 2012-5-28 обнГ04:08:00 xiaoqing.zhouxq Exp $
 */
public class BindVarConditionComparable implements Comparator<BindVarCondition> {

    public int compare(BindVarCondition o1, BindVarCondition o2) {
        int index1 = o1.getIndex();
        int index2 = o2.getIndex();
        if (index1 < index2) {
            return -1;
        } else if (index1 > index2) {
            return 1;
        }
        return 0;
    }

}
