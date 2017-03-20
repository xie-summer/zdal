/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.util;

import java.io.Closeable;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.Driver;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import com.alipay.zdal.parser.sql.SqlParserRuntimeException;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: JdbcUtils.java, v 0.1 2012-11-17 ÏÂÎç3:55:44 Exp $
 */
public final class JdbcUtils {

    public static final String      MOCK             = "mock";

    public static final String      ORACLE           = "oracle";

    private static final String     ORACLE_DRIVER    = "oracle.jdbc.driver.OracleDriver";

    public static final String      MYSQL            = "mysql";

    private static final String     MYSQL_DRIVER     = "com.mysql.jdbc.Driver";

    private static final Properties DRIVERURLMAPPING = new Properties();

    static {
        try {
            for (Enumeration<URL> e = Thread.currentThread().getContextClassLoader().getResources(
                "META-INF/druid-driver.properties"); e.hasMoreElements();) {
                URL url = e.nextElement();

                Properties property = new Properties();

                InputStream is = null;
                try {
                    is = url.openStream();
                    property.load(is);
                } finally {
                    JdbcUtils.close(is);
                }

                DRIVERURLMAPPING.putAll(property);
            }
        } catch (Exception e) {
            throw new SqlParserRuntimeException("load druid-driver.properties error", e);
        }
    }

    public final static void close(Connection x) {
        if (x != null) {
            try {
                x.close();
            } catch (Exception e) {
                throw new SqlParserRuntimeException("close connection error", e);
            }
        }
    }

    public final static void close(Statement x) {
        if (x != null) {
            try {
                x.close();
            } catch (Exception e) {
                throw new SqlParserRuntimeException("close statement error", e);
            }
        }
    }

    public final static void close(ResultSet x) {
        if (x != null) {
            try {
                x.close();
            } catch (Exception e) {
                throw new SqlParserRuntimeException("close resultset error", e);
            }
        }
    }

    public final static void close(Closeable x) {
        if (x != null) {
            try {
                x.close();
            } catch (Exception e) {
                throw new SqlParserRuntimeException("close error", e);
            }
        }
    }

    public final static void printResultSet(ResultSet rs) throws SQLException {
        printResultSet(rs, System.out);
    }

    public final static void printResultSet(ResultSet rs, PrintStream out) throws SQLException {
        ResultSetMetaData metadata = rs.getMetaData();
        int columnCount = metadata.getColumnCount();
        for (int columnIndex = 1; columnIndex <= columnCount; ++columnIndex) {
            if (columnIndex != 1) {
                out.print('\t');
            }
            out.print(metadata.getColumnName(columnIndex));
        }

        out.println();

        while (rs.next()) {

            for (int columnIndex = 1; columnIndex <= columnCount; ++columnIndex) {
                if (columnIndex != 1) {
                    out.print('\t');
                }

                int type = metadata.getColumnType(columnIndex);

                if (type == Types.VARCHAR || type == Types.CHAR || type == Types.NVARCHAR
                    || type == Types.NCHAR) {
                    out.print(rs.getString(columnIndex));
                } else if (type == Types.DATE) {
                    Date date = rs.getDate(columnIndex);
                    if (rs.wasNull()) {
                        out.print("null");
                    } else {
                        out.print(date.toString());
                    }
                } else if (type == Types.BIT) {
                    boolean value = rs.getBoolean(columnIndex);
                    if (rs.wasNull()) {
                        out.print("null");
                    } else {
                        out.print(Boolean.toString(value));
                    }
                } else if (type == Types.BOOLEAN) {
                    boolean value = rs.getBoolean(columnIndex);
                    if (rs.wasNull()) {
                        out.print("null");
                    } else {
                        out.print(Boolean.toString(value));
                    }
                } else if (type == Types.TINYINT) {
                    byte value = rs.getByte(columnIndex);
                    if (rs.wasNull()) {
                        out.print("null");
                    } else {
                        out.print(Byte.toString(value));
                    }
                } else if (type == Types.SMALLINT) {
                    short value = rs.getShort(columnIndex);
                    if (rs.wasNull()) {
                        out.print("null");
                    } else {
                        out.print(Short.toString(value));
                    }
                } else if (type == Types.INTEGER) {
                    int value = rs.getInt(columnIndex);
                    if (rs.wasNull()) {
                        out.print("null");
                    } else {
                        out.print(Integer.toString(value));
                    }
                } else if (type == Types.BIGINT) {
                    long value = rs.getLong(columnIndex);
                    if (rs.wasNull()) {
                        out.print("null");
                    } else {
                        out.print(Long.toString(value));
                    }
                } else if (type == Types.TIMESTAMP) {
                    out.print(String.valueOf(rs.getTimestamp(columnIndex)));
                } else if (type == Types.DECIMAL) {
                    out.print(String.valueOf(rs.getBigDecimal(columnIndex)));
                } else if (type == Types.CLOB) {
                    out.print(String.valueOf(rs.getString(columnIndex)));
                } else if (type == Types.JAVA_OBJECT) {
                    Object objec = rs.getObject(columnIndex);

                    if (rs.wasNull()) {
                        out.print("null");
                    } else {
                        out.print(String.valueOf(objec));
                    }
                } else if (type == Types.LONGVARCHAR) {
                    Object objec = rs.getString(columnIndex);

                    if (rs.wasNull()) {
                        out.print("null");
                    } else {
                        out.print(String.valueOf(objec));
                    }
                } else {
                    Object objec = rs.getObject(columnIndex);

                    if (rs.wasNull()) {
                        out.print("null");
                    } else {
                        out.print(String.valueOf(objec));
                    }
                }
            }
            out.println();
        }
    }

    public static String getTypeName(int sqlType) {
        switch (sqlType) {
            case Types.ARRAY:
                return "ARRAY";

            case Types.BIGINT:
                return "BIGINT";

            case Types.BINARY:
                return "BINARY";

            case Types.BIT:
                return "BIT";

            case Types.BLOB:
                return "BLOB";

            case Types.BOOLEAN:
                return "BOOLEAN";

            case Types.CHAR:
                return "CHAR";

            case Types.CLOB:
                return "CLOB";

            case Types.DATALINK:
                return "DATALINK";

            case Types.DATE:
                return "DATE";

            case Types.DECIMAL:
                return "DECIMAL";

            case Types.DISTINCT:
                return "DISTINCT";

            case Types.DOUBLE:
                return "DOUBLE";

            case Types.FLOAT:
                return "FLOAT";

            case Types.INTEGER:
                return "INTEGER";

            case Types.JAVA_OBJECT:
                return "JAVA_OBJECT";

            case Types.LONGNVARCHAR:
                return "LONGNVARCHAR";

            case Types.LONGVARBINARY:
                return "LONGVARBINARY";

            case Types.NCHAR:
                return "NCHAR";

            case Types.NCLOB:
                return "NCLOB";

            case Types.NULL:
                return "NULL";

            case Types.NUMERIC:
                return "NUMERIC";

            case Types.NVARCHAR:
                return "NVARCHAR";

            case Types.REAL:
                return "REAL";

            case Types.REF:
                return "REF";

            case Types.ROWID:
                return "ROWID";

            case Types.SMALLINT:
                return "SMALLINT";

            case Types.SQLXML:
                return "SQLXML";

            case Types.STRUCT:
                return "STRUCT";

            case Types.TIME:
                return "TIME";

            case Types.TIMESTAMP:
                return "TIMESTAMP";

            case Types.TINYINT:
                return "TINYINT";

            case Types.VARBINARY:
                return "VARBINARY";

            case Types.VARCHAR:
                return "VARCHAR";

            default:
                return "OTHER";

        }
    }

    public static String getDriverClassName(String rawUrl) throws SQLException {
        if (rawUrl.startsWith("jdbc:mysql:")) {
            return MYSQL_DRIVER;
        } else if (rawUrl.startsWith("jdbc:oracle:")) {
            return ORACLE_DRIVER;
        } else {
            throw new SQLException("unkow jdbc driver : " + rawUrl);
        }
    }

    public static String getDbType(String rawUrl, String driverClassName) {
        if (rawUrl == null) {
            return null;
        }

        if (rawUrl.startsWith("jdbc:mysql:")) {
            return MYSQL;
        } else if (rawUrl.startsWith("jdbc:oracle:")) {
            return ORACLE;
        } else {
            return null;
        }
    }

    public static Driver createDriver(String driverClassName) throws SQLException {
        try {
            return (Driver) Class.forName(driverClassName).newInstance();
        } catch (IllegalAccessException e) {
            throw new SQLException(e.getMessage(), e);
        } catch (InstantiationException e) {
            throw new SQLException(e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            // skip
        }

        try {
            return (Driver) Thread.currentThread().getContextClassLoader().loadClass(
                driverClassName).newInstance();
        } catch (IllegalAccessException e) {
            throw new SQLException(e.getMessage(), e);
        } catch (InstantiationException e) {
            throw new SQLException(e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            throw new SQLException(e.getMessage(), e);
        }
    }

    public static int executeUpdate(Connection conn, String sql, List<Object> parameters)
                                                                                         throws SQLException {
        PreparedStatement stmt = null;

        int updateCount;
        try {
            stmt = conn.prepareStatement(sql);

            setParameters(stmt, parameters);

            updateCount = stmt.executeUpdate();
        } finally {
            JdbcUtils.close(stmt);
        }

        return updateCount;
    }

    public static void execute(Connection conn, String sql, List<Object> parameters)
                                                                                    throws SQLException {
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(sql);

            setParameters(stmt, parameters);

            stmt.executeUpdate();
        } finally {
            JdbcUtils.close(stmt);
        }
    }

    private static void setParameters(PreparedStatement stmt, List<Object> parameters)
                                                                                      throws SQLException {
        for (int i = 0, size = parameters.size(); i < size; ++i) {
            stmt.setObject(i + 1, parameters.get(i));
        }
    }

    public static Class<?> loadDriverClass(String className) {
        Class<?> clazz = null;

        if (className == null) {
            return null;
        }

        try {
            clazz = Thread.currentThread().getContextClassLoader().loadClass(className);
        } catch (ClassNotFoundException e) {
            //            throw e;
        }

        if (clazz != null) {
            return clazz;
        }

        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
}
