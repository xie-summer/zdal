/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.common.sqljep.function;

/**
 * 可比较的类
 * 实际上是是两个东西的结合
 * 
 * 符号+值
 * 
 * 例如 [> 1] , [< 1] , [= 1]
 * 
 *
 */
@SuppressWarnings("unchecked")
public class Comparative implements Comparable, Cloneable {

    public static final int GreaterThan        = 1;
    public static final int GreaterThanOrEqual = 2;
    public static final int Equivalent         = 3;
    public static final int Like               = 4;
    public static final int NotLike            = 5;
    public static final int NotEquivalent      = 6;
    public static final int LessThan           = 7;
    public static final int LessThanOrEqual    = 8;
    public static final int NotSupport         = -1;

    /**
     * 表达式取反
     * 
     * @param function
     * @return
     */
    public static int reverseComparison(int function) {
        return 9 - function;
    }

    /**
     * 表达式前后位置调换的时候
     * 
     * @param function
     * @return
     */
    public static int exchangeComparison(int function) {
        if (function == GreaterThan) {
            return LessThan;
        } else if (function == GreaterThanOrEqual) {
            return LessThanOrEqual;
        } else if (function == LessThan) {
            return GreaterThan;
        }
        if (function == LessThanOrEqual) {
            return GreaterThanOrEqual;
        } else {
            return function;
        }
    }

    private Comparable value;     //这有可能又是个Comparative，从而实质上表示一课树（比较树）
    private int        comparison;

    protected Comparative() {
    }

    public Comparative(int function, Comparable value) {
        this.comparison = function;
        this.value = value;
    }

    public Comparable getValue() {
        return value;
    }

    public void setComparison(int function) {
        this.comparison = function;
    }

    public static String getComparisonName(int function) {
        if (function == Equivalent) {
            return "=";
        } else if (function == GreaterThan) {
            return ">";
        } else if (function == GreaterThanOrEqual) {
            return ">=";
        } else if (function == LessThanOrEqual) {
            return "<=";
        } else if (function == LessThan) {
            return "<";
        } else if (function == NotEquivalent) {
            return "<>";
        } else if (function == Like) {
            return "LIKE";
        } else if (function == NotLike) {
            return "NOT LIKE";
        } else {
            return null;
        }
    }

    public static int getComparisonByIdent(String ident) {
        if ("=".equals(ident)) {
            return Equivalent;
        } else if (">".equals(ident)) {
            return GreaterThan;
        } else if (">=".equals(ident)) {
            return GreaterThanOrEqual;
        } else if ("<=".equals(ident)) {
            return LessThanOrEqual;
        } else if ("<".equals(ident)) {
            return LessThan;
        } else if ("!=".equals(ident)) {
            return NotEquivalent;
        } else if ("<>".equals(ident)) {
            return NotEquivalent;
        } else if ("like".equalsIgnoreCase(ident)) {
            return Like;
        } else {
            return NotSupport;
        }
    }

    public int getComparison() {
        return comparison;
    }

    public void setValue(Comparable value) {
        this.value = value;
    }

    public int compareTo(Object o) {
        if (o instanceof Comparative) {
            Comparative other = (Comparative) o;
            return this.getValue().compareTo(other.getValue());
        } else if (o instanceof Comparable) {
            return this.getValue().compareTo(o);
        }
        return -1;
    }

    public String toString() {
        if (value != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("[").append(comparison).append("]");
            sb.append(value.toString());
            return sb.toString();
        } else {
            return null;
        }
    }

    public Object clone() {
        return new Comparative(this.comparison, this.value);
    }

}
