/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.rule.groovy.staticmethod;

import java.util.Calendar;
import java.util.Date;

import com.alipay.zdal.common.util.NestThreadLocalMap;

public class GroovyStaticMethod {
    public static final String  GROOVY_STATIC_METHOD_CALENDAR = "GROOVY_STATIC_METHOD_CALENDAR";
    private final static long[] pow10                         = { 1, 10, 100, 1000, 10000, 100000,
            1000000, 10000000, 100000000, 1000000000, 10000000000L, 100000000000L, 1000000000000L,
            10000000000000L, 100000000000000L, 1000000000000000L, 10000000000000000L,
            100000000000000000L, 1000000000000000000L        };

    /**
     * 
     * 默认的dayofweek :
     * 如果offset  = 0;那么为默认dow
     * san = 1
     * sat = 7
     * 
     * @param date
     * @return
     */
    public static int dayofweek(Date date, int offset) {
        Calendar cal = getCalendar();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK) + offset;
    }

    /**
     * 我们自己的dayofweek.因为所有index都默认从0开始，因此我也必须让day of week从0 开始。
     * 默认情况下 直接offset = -1 解决
     * san = 0;
     * sat = 6;
     * @param date
     * @return
     */
    public static int dayofweek(Date date) {
        return dayofweek(date, -1);
    }

    private static Calendar getCalendar() {
        Calendar cal = (Calendar) NestThreadLocalMap.get(GROOVY_STATIC_METHOD_CALENDAR);
        if (cal == null) {
            cal = Calendar.getInstance();
            NestThreadLocalMap.put(GROOVY_STATIC_METHOD_CALENDAR, cal);
        }
        return cal;
    }

    private static long getModRight(long targetID, int size, int bitNumber) {
        if (bitNumber < size) {
            throw new IllegalArgumentException("输入的位数比要求的size还小");
        }
        return (size == 0 ? 0 : targetID / pow10[bitNumber - size]);
    }

    /**
     * 从左开始，取指定多的位数。
     * 
     * @param targetID 目标id，也就是等待被decode的数据
     * @param bitNumber 目标id数据的位数
     * @param st 从哪儿开始取，如果想取最左边的一位那么可以输入st = 0;ed =1;
     * @param ed 取到哪儿，如果想取最左边的两位，那么可以输入st = 0;ed = 2;
     * @return
     */
    public static long left(long targetID, int bitNumber, int st, int ed) {
        long end = getModRight(targetID, ed, bitNumber);
        return end % pow10[(ed - st)];
    }

    /**
     * 从左开始，取指定多的位数。默认是一个long形长度的数据，也就是bitNumber= 19
     * 
     * @param targetID 目标id，也就是等待被decode的数据
     * @param st 从哪儿开始取，如果想取最左边的一位那么可以输入st = 0;ed =1;
     * @param ed 取到哪儿，如果想取最左边的两位，那么可以输入st = 0;ed = 2;
     * @return
     */
    public static long left(long targetID, int st, int ed) {
        long end = getModRight(targetID, ed, 19);
        return end % pow10[(ed - st)];
    }

    /**
     * 从右开始，取指定多的位数。
     * 
     * @param targetID 目标id，也就是等待被decode的数据
     * @param st 从哪儿开始取，如果想取最右边的一位那么可以输入st = 0;ed =1;
     * @param ed 取到哪儿，如果想取最右边的两位，那么可以输入st = 0;ed = 2;
     * @return
     */
    public static long right(long targetID, int st, int ed) {
        long right = targetID % pow10[ed];
        return right / pow10[(st)];
    }

    public static String right(String right, int rightLength) {
        int length = right.length();
        int start = length - rightLength;
        return right.substring(start < 0 ? 0 : start);
    }

    public static void main(String[] args) {
        //		String l = "8l";
        //System.out.println(right(l, 2));
    }
}
