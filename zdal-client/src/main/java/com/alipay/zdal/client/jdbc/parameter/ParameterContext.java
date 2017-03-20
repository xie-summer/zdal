/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.client.jdbc.parameter;

public class ParameterContext {
    private ParameterMethod parameterMethod;
    /**
     * args[0]: parameterIndex
     * args[1]: 参数值
     * args[2]: length 适用于：setAsciiStream、setBinaryStream、setCharacterStream、setUnicodeStream
     * 。。。
     * 
     */
    private Object[]        args;

    public ParameterContext() {
    }

    public ParameterContext(ParameterMethod parameterMethod, Object[] args) {
        this.parameterMethod = parameterMethod;
        this.args = args;
    }

    public ParameterMethod getParameterMethod() {
        return parameterMethod;
    }

    public void setParameterMethod(ParameterMethod parameterMethod) {
        this.parameterMethod = parameterMethod;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(parameterMethod).append("(");
        for (int i = 0; i < args.length; ++i) {
            buffer.append(args[i]);
            if (i != args.length - 1) {
                buffer.append(", ");
            }
        }
        buffer.append(")");

        return buffer.toString();
    }
}
