/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.client.util;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: ExceptionUtils.java, v 0.1 2014-1-6 ÏÂÎç05:15:12 Exp $
 */
public class ExceptionUtils {
    public static final String  SQL_EXECUTION_ERROR_CONTEXT_LOG     = "SQL_EXECUTION_ERROR_CONTEXT_LOG";
    private static final String SQL_EXECUTION_ERROR_CONTEXT_MESSAGE = "SQLException ,context is ";
    private static final Logger log                                 = Logger
                                                                        .getLogger(SQL_EXECUTION_ERROR_CONTEXT_LOG);

    public static void throwSQLException(List<SQLException> exceptions, String sql,
                                         List<Object> args) throws SQLException {
        if (exceptions != null && !exceptions.isEmpty()) {
            SQLException first = exceptions.get(0);
            if (sql != null) {
                log.warn(("ZDAL SQL EXECUTE ERROR REPORTER:" + getErrorContext(sql, args,
                    SQL_EXECUTION_ERROR_CONTEXT_MESSAGE)), first);
            }
            for (int i = 1, n = exceptions.size(); i < n; i++) {
                if (sql != null) {
                    log.warn(("layer:" + n + "ZDAL SQL EXECUTE ERROR REPORTER :" + getErrorContext(
                        sql, args, SQL_EXECUTION_ERROR_CONTEXT_MESSAGE)), exceptions.get(i));
                }
            }
            throw mergeException(exceptions);
        }
    }

    public static SQLException mergeException(List<SQLException> exceptions) {
        SQLException first = exceptions.get(0);
        List<StackTraceElement> stes = new ArrayList<StackTraceElement>(30 * exceptions.size());
        for (StackTraceElement ste : first.getStackTrace()) {
            stes.add(ste);
        }
        //SQLException current = first;
        Set<SQLException> exceptionsSet = new HashSet<SQLException>(exceptions.size());
        exceptionsSet.add(first);
        for (int i = 1, n = exceptions.size(); i < n; i++) {
            if (exceptionsSet.contains(exceptions.get(i))) {
                continue;
            }
            //current.setNextException(exceptions.get(i));
            //current = exceptions.get(i);
            exceptionsSet.add(exceptions.get(i));
            for (StackTraceElement ste : exceptions.get(i).getStackTrace()) {
                stes.add(ste);
            }
        }
        //newEx.getCause();
        first.setStackTrace(stes.toArray(new StackTraceElement[stes.size()]));
        return first;
    }

    public static void throwSQLException(SQLException exception, String sql, List<Object> args)
                                                                                               throws SQLException {
        if (sql != null) {
            log.warn(("ZDAL SQL EXECUTE ERROR REPORTER:" + getErrorContext(sql, args,
                SQL_EXECUTION_ERROR_CONTEXT_MESSAGE))
                     + "nest Exceptions is " + exception.getMessage(), exception);
        }
        throw exception;
    }

    public static String getErrorContext(String sql, List<Object> arguments, String message) {
        StringBuilder sb = new StringBuilder();
        sb.append(message).append(sql).append("|||arguments:");
        printArgument(arguments, sb);
        return sb.toString();
    }

    private static void printArgument(List<Object> parameters, StringBuilder sb) {
        int i = 0;
        if (parameters != null) {
            for (Object param : parameters) {

                sb.append("[index:").append(i).append("|parameter:").append(param).append(
                    "|typeclass:").append(param == null ? null : param.getClass().getName())
                    .append("]");
                i++;
            }
        } else {
            sb.append("[empty]");
        }
    }
}
