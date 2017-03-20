/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.visitor;

import static com.alipay.zdal.parser.sql.visitor.SQLEvalVisitor.EVAL_VALUE;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alipay.zdal.parser.sql.SQLUtils;
import com.alipay.zdal.parser.sql.SqlParserRuntimeException;
import com.alipay.zdal.parser.sql.ast.SQLExpr;
import com.alipay.zdal.parser.sql.ast.SQLObject;
import com.alipay.zdal.parser.sql.ast.expr.SQLBinaryOpExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLBinaryOperator;
import com.alipay.zdal.parser.sql.ast.expr.SQLCharExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLNumericLiteralExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLVariantRefExpr;
import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlEvalVisitorImpl;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleEvalVisitor;
import com.alipay.zdal.parser.sql.util.JdbcUtils;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: SQLEvalVisitorUtils.java, v 0.1 2012-11-17 ÏÂÎç3:56:52 Exp $
 */
public class SQLEvalVisitorUtils {

    public static Object evalExpr(String dbType, String expr, Object... parameters) {
        SQLExpr sqlExpr = SQLUtils.toSQLExpr(expr, dbType);
        return eval(dbType, sqlExpr, parameters);
    }

    public static Object evalExpr(String dbType, String expr, List<Object> parameters) {
        SQLExpr sqlExpr = SQLUtils.toSQLExpr(expr);
        return eval(dbType, sqlExpr, parameters);
    }

    public static Object eval(String dbType, SQLObject sqlObject, Object... parameters) {
        return eval(dbType, sqlObject, Arrays.asList(parameters));
    }

    public static Object getValue(SQLObject sqlObject) {
        if (sqlObject instanceof SQLNumericLiteralExpr) {
            return ((SQLNumericLiteralExpr) sqlObject).getNumber();
        }

        return sqlObject.getAttributes().get(EVAL_VALUE);
    }

    public static Object eval(String dbType, SQLObject sqlObject, List<Object> parameters) {
        return eval(dbType, sqlObject, parameters, true);
    }

    public static Object eval(String dbType, SQLObject sqlObject, List<Object> parameters,
                              boolean throwError) {
        SQLEvalVisitor visitor = createEvalVisitor(dbType);
        visitor.setParameters(parameters);
        sqlObject.accept(visitor);

        Object value = getValue(sqlObject);
        if (value == null) {
            if (throwError && !sqlObject.getAttributes().containsKey(EVAL_VALUE)) {
                throw new SqlParserRuntimeException("eval error : "
                                                    + SQLUtils.toSQLString(sqlObject, dbType));
            }
        }

        return value;
    }

    public static SQLEvalVisitor createEvalVisitor(String dbType) {
        if (JdbcUtils.MYSQL.equals(dbType)) {
            return new MySqlEvalVisitorImpl();
        }

        if (JdbcUtils.ORACLE.equals(dbType)) {
            return new OracleEvalVisitor();
        }

        return new SQLEvalVisitorImpl();
    }

    public static boolean visit(SQLEvalVisitor visitor, SQLCharExpr x) {
        x.putAttribute(EVAL_VALUE, x.getText());
        return true;
    }

    public static boolean visit(SQLEvalVisitor visitor, SQLBinaryOpExpr x) {
        x.getLeft().accept(visitor);
        x.getRight().accept(visitor);

        if (!x.getLeft().getAttributes().containsKey(EVAL_VALUE)) {
            return false;
        }

        if (!x.getRight().getAttributes().containsKey(EVAL_VALUE)) {
            return false;
        }

        if (SQLBinaryOperator.Add.equals(x.getOperator())) {
            Object value = add(x.getLeft().getAttribute(EVAL_VALUE), x.getRight().getAttributes()
                .get(EVAL_VALUE));
            x.putAttribute(EVAL_VALUE, value);
        }

        return false;
    }

    public static boolean visit(SQLEvalVisitor visitor, SQLVariantRefExpr x) {
        if (!"?".equals(x.getName())) {
            return false;
        }

        Map<String, Object> attributes = x.getAttributes();

        int varIndex = x.getIndex();

        if (varIndex != -1 && visitor.getParameters().size() > varIndex) {
            boolean containsValue = attributes.containsKey(EVAL_VALUE);
            if (!containsValue) {
                Object value = visitor.getParameters().get(varIndex);
                attributes.put(EVAL_VALUE, value);
            }
        }

        return false;
    }

    public static Boolean bool(Object val) {
        if (val == null) {
            return null;
        }

        if (val instanceof Boolean) {
            return (Boolean) val;
        }

        if (val instanceof Number) {
            return ((Number) val).intValue() == 1;
        }

        throw new IllegalArgumentException();
    }

    public static String string(Object val) {
        Object value = val;

        if (value == null) {
            return null;
        }

        return value.toString();
    }

    public static Byte byte1(Object val) {
        if (val == null) {
            return null;
        }

        if (val instanceof Byte) {
            return (Byte) val;
        }

        return ((Number) val).byteValue();
    }

    public static Short short1(Object val) {
        if (val == null) {
            return null;
        }

        if (val instanceof Short) {
            return (Short) val;
        }

        return ((Number) val).shortValue();
    }

    public static Integer int1(Object val) {
        if (val == null) {
            return null;
        }

        if (val instanceof Integer) {
            return (Integer) val;
        }

        return ((Number) val).intValue();
    }

    public static Long long1(Object val) {
        if (val == null) {
            return null;
        }

        if (val instanceof Long) {
            return (Long) val;
        }

        return ((Number) val).longValue();
    }

    public static Float float1(Object val) {
        if (val == null) {
            return null;
        }

        if (val instanceof Float) {
            return (Float) val;
        }

        return ((Number) val).floatValue();
    }

    public static Double double1(Object val) {
        if (val == null) {
            return null;
        }

        if (val instanceof Double) {
            return (Double) val;
        }

        return ((Number) val).doubleValue();
    }

    public static BigInteger bigInt(Object val) {
        if (val == null) {
            return null;
        }

        if (val instanceof BigInteger) {
            return (BigInteger) val;
        }

        if (val instanceof String) {
            return new BigInteger((String) val);
        }

        return BigInteger.valueOf(((Number) val).longValue());
    }

    public static Date date(Object val) {
        if (val == null) {
            return null;
        }

        if (val instanceof Date) {
            return (Date) val;
        }

        if (val instanceof Number) {
            return new Date(((Number) val).longValue());
        }

        if (val instanceof String) {
            return date((String) val);
        }

        throw new SqlParserRuntimeException("can cast to date");
    }

    public static Date date(String text) {
        if (text == null || text.length() == 0) {
            return null;
        }

        String format;

        if (text.length() == "yyyy-MM-dd".length()) {
            format = "yyyy-MM-dd";
        } else {
            format = "yyyy-MM-dd HH:mm:ss";
        }

        try {
            return new SimpleDateFormat(format).parse(text);
        } catch (ParseException e) {
            throw new SqlParserRuntimeException("format : " + format + ", value : " + text);
        }
    }

    public static BigDecimal decimal(Object val) {
        if (val == null) {
            return null;
        }

        if (val instanceof BigDecimal) {
            return (BigDecimal) val;
        }

        if (val instanceof String) {
            return new BigDecimal((String) val);
        }

        if (val instanceof Float) {
            return new BigDecimal((Float) val);
        }

        if (val instanceof Double) {
            return new BigDecimal((Double) val);
        }

        return BigDecimal.valueOf(((Number) val).longValue());
    }

    public static Object sum(Object a, Object b) {
        if (a == null) {
            return b;
        }

        if (b == null) {
            return a;
        }

        if (a instanceof BigDecimal || b instanceof BigDecimal) {
            return decimal(a).add(decimal(b));
        }

        if (a instanceof BigInteger || b instanceof BigInteger) {
            return bigInt(a).add(bigInt(b));
        }

        if (a instanceof Long || b instanceof Long) {
            return long1(a) + long1(b);
        }

        if (a instanceof Integer || b instanceof Integer) {
            return int1(a) + int1(b);
        }

        if (a instanceof Short || b instanceof Short) {
            return short1(a) + short1(b);
        }

        if (a instanceof Byte || b instanceof Byte) {
            return byte1(a) + byte1(b);
        }

        throw new IllegalArgumentException();
    }

    public static Object div(Object a, Object b) {
        if (a == null || b == null) {
            return null;
        }

        BigDecimal decimalA = decimal(a);
        BigDecimal decimalB = decimal(b);

        try {
            return decimalA.divide(decimalB);
        } catch (ArithmeticException ex) {
            return decimalA.divide(decimalB, 4, RoundingMode.CEILING);
        }
    }

    public static Object div2(Object a, Object b) {
        if (a == null || b == null) {
            return null;
        }

        if (a instanceof BigDecimal || b instanceof BigDecimal) {
            return decimal(a).divide(decimal(b));
        }

        if (a instanceof BigInteger || b instanceof BigInteger) {
            return bigInt(a).divide(bigInt(b));
        }

        if (a instanceof Long || b instanceof Long) {
            return long1(a) / long1(b);
        }

        if (a instanceof Integer || b instanceof Integer) {
            return int1(a) / int1(b);
        }

        if (a instanceof Short || b instanceof Short) {
            return short1(a) / short1(b);
        }

        if (a instanceof Byte || b instanceof Byte) {
            return byte1(a) / byte1(b);
        }

        throw new IllegalArgumentException();
    }

    public static boolean gt(Object a, Object b) {
        if (a == null) {
            return false;
        }

        if (b == null) {
            return true;
        }

        if (a instanceof BigDecimal || b instanceof BigDecimal) {
            return decimal(a).compareTo(decimal(b)) > 0;
        }

        if (a instanceof BigInteger || b instanceof BigInteger) {
            return bigInt(a).compareTo(bigInt(b)) > 0;
        }

        if (a instanceof Long || b instanceof Long) {
            return long1(a) > long1(b);
        }

        if (a instanceof Integer || b instanceof Integer) {
            return int1(a) > int1(b);
        }

        if (a instanceof Short || b instanceof Short) {
            return short1(a) > short1(b);
        }

        if (a instanceof Byte || b instanceof Byte) {
            return byte1(a) > byte1(b);
        }

        if (a instanceof Date || b instanceof Date) {
            Date d1 = date(a);
            Date d2 = date(b);

            if (d1 == d2) {
                return false;
            }

            if (d1 == null) {
                return false;
            }

            if (d2 == null) {
                return true;
            }

            return d1.compareTo(d2) > 0;
        }

        throw new IllegalArgumentException();
    }

    public static boolean gteq(Object a, Object b) {
        if (eq(a, b)) {
            return true;
        }

        return gt(a, b);
    }

    public static boolean lt(Object a, Object b) {
        if (a == null) {
            return true;
        }

        if (b == null) {
            return false;
        }

        if (a instanceof BigDecimal || b instanceof BigDecimal) {
            return decimal(a).compareTo(decimal(b)) < 0;
        }

        if (a instanceof BigInteger || b instanceof BigInteger) {
            return bigInt(a).compareTo(bigInt(b)) < 0;
        }

        if (a instanceof Long || b instanceof Long) {
            return long1(a) < long1(b);
        }

        if (a instanceof Integer || b instanceof Integer) {
            return int1(a) < int1(b);
        }

        if (a instanceof Short || b instanceof Short) {
            return short1(a) < short1(b);
        }

        if (a instanceof Byte || b instanceof Byte) {
            return byte1(a) < byte1(b);
        }

        if (a instanceof Date || b instanceof Date) {
            Date d1 = date(a);
            Date d2 = date(b);

            if (d1 == d2) {
                return false;
            }

            if (d1 == null) {
                return true;
            }

            if (d2 == null) {
                return false;
            }

            return d1.compareTo(d2) < 0;
        }

        throw new IllegalArgumentException();
    }

    public static boolean lteq(Object a, Object b) {
        if (eq(a, b)) {
            return true;
        }

        return lt(a, b);
    }

    public static boolean eq(Object a, Object b) {
        if (a == b) {
            return true;
        }

        if (a == null || b == null) {
            return false;
        }

        if (a.equals(b)) {
            return true;
        }

        if (a instanceof BigDecimal || b instanceof BigDecimal) {
            return decimal(a).compareTo(decimal(b)) == 0;
        }

        if (a instanceof BigInteger || b instanceof BigInteger) {
            return bigInt(a).compareTo(bigInt(b)) == 0;
        }

        if (a instanceof Long || b instanceof Long) {
            return long1(a) == long1(b);
        }

        if (a instanceof Integer || b instanceof Integer) {
            return int1(a) == int1(b);
        }

        if (a instanceof Short || b instanceof Short) {
            return short1(a) == short1(b);
        }

        if (a instanceof Byte || b instanceof Byte) {
            return byte1(a) == byte1(b);
        }

        if (a instanceof Date || b instanceof Date) {
            Date d1 = date(a);
            Date d2 = date(b);

            if (d1 == d2) {
                return true;
            }

            if (d1 == null || d2 == null) {
                return false;
            }

            return d1.equals(d2);
        }

        throw new IllegalArgumentException();
    }

    public static Object add(Object a, Object b) {
        if (a == null) {
            return b;
        }

        if (b == null) {
            return a;
        }

        if (a instanceof BigDecimal || b instanceof BigDecimal) {
            return decimal(a).add(decimal(b));
        }

        if (a instanceof BigInteger || b instanceof BigInteger) {
            return bigInt(a).add(bigInt(b));
        }

        if (a instanceof Long || b instanceof Long) {
            return long1(a) + long1(b);
        }

        if (a instanceof Integer || b instanceof Integer) {
            return int1(a) + int1(b);
        }

        if (a instanceof Short || b instanceof Short) {
            return short1(a) + short1(b);
        }

        if (a instanceof Byte || b instanceof Byte) {
            return byte1(a) + byte1(b);
        }

        if (a instanceof String || b instanceof String) {
            return string(a) + string(b);
        }

        throw new IllegalArgumentException();
    }

    public static Object sub(Object a, Object b) {
        if (a == null) {
            return null;
        }

        if (b == null) {
            return a;
        }

        if (a instanceof BigDecimal || b instanceof BigDecimal) {
            return decimal(a).subtract(decimal(b));
        }

        if (a instanceof BigInteger || b instanceof BigInteger) {
            return bigInt(a).subtract(bigInt(b));
        }

        if (a instanceof Long || b instanceof Long) {
            return long1(a) - long1(b);
        }

        if (a instanceof Integer || b instanceof Integer) {
            return int1(a) - int1(b);
        }

        if (a instanceof Short || b instanceof Short) {
            return short1(a) - short1(b);
        }

        if (a instanceof Byte || b instanceof Byte) {
            return byte1(a) - byte1(b);
        }

        throw new IllegalArgumentException();
    }

    public static Object multi(Object a, Object b) {
        if (a == null || b == null) {
            return null;
        }

        if (a instanceof BigDecimal || b instanceof BigDecimal) {
            return decimal(a).multiply(decimal(b));
        }

        if (a instanceof BigInteger || b instanceof BigInteger) {
            return bigInt(a).multiply(bigInt(b));
        }

        if (a instanceof Long || b instanceof Long) {
            return long1(a) * long1(b);
        }

        if (a instanceof Integer || b instanceof Integer) {
            return int1(a) * int1(b);
        }

        if (a instanceof Short || b instanceof Short) {
            return short1(a) * short1(b);
        }

        if (a instanceof Byte || b instanceof Byte) {
            return byte1(a) * byte1(b);
        }

        throw new IllegalArgumentException();
    }
}
