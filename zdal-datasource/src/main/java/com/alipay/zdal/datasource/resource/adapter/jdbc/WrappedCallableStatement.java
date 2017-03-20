/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.resource.adapter.jdbc;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

/**
 * WrappedCallableStatement
 *
 * @author ����
 * @version $Id: WrappedCallableStatement.java, v 0.1 2014-1-6 ����05:30:17 Exp $
 */
public class WrappedCallableStatement extends WrappedPreparedStatement implements CallableStatement {

    private final CallableStatement cs;

    /**
     * @param lc
     * @param cs
     */
    public WrappedCallableStatement(final WrappedConnection lc, final CallableStatement cs) {
        super(lc, cs, "", null);
        this.cs = cs;
    }

    public Object getObject(int parameterIndex) throws SQLException {
        checkState();
        try {
            return cs.getObject(parameterIndex);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public Object getObject(int parameterIndex, Map<String, Class<?>> typeMap) throws SQLException {
        checkState();
        try {
            return cs.getObject(parameterIndex, typeMap);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public Object getObject(String parameterName) throws SQLException {
        checkState();
        try {
            return cs.getObject(parameterName);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public Object getObject(String parameterName, Map<String, Class<?>> typeMap)
                                                                                throws SQLException {
        checkState();
        try {
            return cs.getObject(parameterName, typeMap);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public boolean getBoolean(int parameterIndex) throws SQLException {
        checkState();
        try {
            return cs.getBoolean(parameterIndex);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public boolean getBoolean(String parameterName) throws SQLException {
        checkState();
        try {
            return cs.getBoolean(parameterName);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public byte getByte(int parameterIndex) throws SQLException {
        checkState();
        try {
            return cs.getByte(parameterIndex);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public byte getByte(String parameterName) throws SQLException {
        checkState();
        try {
            return cs.getByte(parameterName);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public short getShort(int parameterIndex) throws SQLException {
        checkState();
        try {
            return cs.getShort(parameterIndex);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public short getShort(String parameterName) throws SQLException {
        checkState();
        try {
            return cs.getShort(parameterName);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public int getInt(int parameterIndex) throws SQLException {
        checkState();
        try {
            return cs.getInt(parameterIndex);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public int getInt(String parameterName) throws SQLException {
        checkState();
        try {
            return cs.getInt(parameterName);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public long getLong(int parameterIndex) throws SQLException {
        checkState();
        try {
            return cs.getLong(parameterIndex);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public long getLong(String parameterName) throws SQLException {
        checkState();
        try {
            return cs.getLong(parameterName);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public float getFloat(int parameterIndex) throws SQLException {
        checkState();
        try {
            return cs.getFloat(parameterIndex);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public float getFloat(String parameterName) throws SQLException {
        checkState();
        try {
            return cs.getFloat(parameterName);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public double getDouble(int parameterIndex) throws SQLException {
        checkState();
        try {
            return cs.getDouble(parameterIndex);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public double getDouble(String parameterName) throws SQLException {
        checkState();
        try {
            return cs.getDouble(parameterName);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public byte[] getBytes(int parameterIndex) throws SQLException {
        checkState();
        try {
            return cs.getBytes(parameterIndex);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public byte[] getBytes(String parameterName) throws SQLException {
        checkState();
        try {
            return cs.getBytes(parameterName);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public URL getURL(int parameterIndex) throws SQLException {
        checkState();
        try {
            return cs.getURL(parameterIndex);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public URL getURL(String parameterName) throws SQLException {
        checkState();
        try {
            return cs.getURL(parameterName);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public String getString(int parameterIndex) throws SQLException {
        checkState();
        try {
            return cs.getString(parameterIndex);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public String getString(String parameterName) throws SQLException {
        checkState();
        try {
            return cs.getString(parameterName);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public Ref getRef(int parameterIndex) throws SQLException {
        checkState();
        try {
            return cs.getRef(parameterIndex);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public Ref getRef(String parameterName) throws SQLException {
        checkState();
        try {
            return cs.getRef(parameterName);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public Time getTime(int parameterIndex) throws SQLException {
        checkState();
        try {
            return cs.getTime(parameterIndex);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public Time getTime(int parameterIndex, Calendar calendar) throws SQLException {
        checkState();
        try {
            return cs.getTime(parameterIndex, calendar);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public Time getTime(String parameterName) throws SQLException {
        checkState();
        try {
            return cs.getTime(parameterName);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public Time getTime(String parameterName, Calendar calendar) throws SQLException {
        checkState();
        try {
            return cs.getTime(parameterName, calendar);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public Date getDate(int parameterIndex) throws SQLException {
        checkState();
        try {
            return cs.getDate(parameterIndex);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public Date getDate(int parameterIndex, Calendar calendar) throws SQLException {
        checkState();
        try {
            return cs.getDate(parameterIndex, calendar);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public Date getDate(String parameterName) throws SQLException {
        checkState();
        try {
            return cs.getDate(parameterName);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public Date getDate(String parameterName, Calendar calendar) throws SQLException {
        checkState();
        try {
            return cs.getDate(parameterName, calendar);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void registerOutParameter(int parameterIndex, int sqlType) throws SQLException {
        checkState();
        try {
            cs.registerOutParameter(parameterIndex, sqlType);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void registerOutParameter(int parameterIndex, int sqlType, int scale)
                                                                                throws SQLException {
        checkState();
        try {
            cs.registerOutParameter(parameterIndex, sqlType, scale);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void registerOutParameter(int parameterIndex, int sqlType, String typeName)
                                                                                      throws SQLException {
        checkState();
        try {
            cs.registerOutParameter(parameterIndex, sqlType, typeName);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void registerOutParameter(String parameterName, int sqlType) throws SQLException {
        checkState();
        try {
            cs.registerOutParameter(parameterName, sqlType);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void registerOutParameter(String parameterName, int sqlType, int scale)
                                                                                  throws SQLException {
        checkState();
        try {
            cs.registerOutParameter(parameterName, sqlType, scale);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void registerOutParameter(String parameterName, int sqlType, String typeName)
                                                                                        throws SQLException {
        checkState();
        try {
            cs.registerOutParameter(parameterName, sqlType, typeName);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public boolean wasNull() throws SQLException {
        checkState();
        try {
            return cs.wasNull();
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    /**
     * @deprecated
     */
    @Deprecated
    public BigDecimal getBigDecimal(int parameterIndex, int scale) throws SQLException {
        checkState();
        try {
            return cs.getBigDecimal(parameterIndex, scale);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public BigDecimal getBigDecimal(int parameterIndex) throws SQLException {
        checkState();
        try {
            return cs.getBigDecimal(parameterIndex);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public BigDecimal getBigDecimal(String parameterName) throws SQLException {
        checkState();
        try {
            return cs.getBigDecimal(parameterName);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public Timestamp getTimestamp(int parameterIndex) throws SQLException {
        checkState();
        try {
            return cs.getTimestamp(parameterIndex);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public Timestamp getTimestamp(int parameterIndex, Calendar calendar) throws SQLException {
        checkState();
        try {
            return cs.getTimestamp(parameterIndex, calendar);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public Timestamp getTimestamp(String parameterName) throws SQLException {
        checkState();
        try {
            return cs.getTimestamp(parameterName);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public Timestamp getTimestamp(String parameterName, Calendar calendar) throws SQLException {
        checkState();
        try {
            return cs.getTimestamp(parameterName, calendar);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public Blob getBlob(int parameterIndex) throws SQLException {
        checkState();
        try {
            return cs.getBlob(parameterIndex);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public Blob getBlob(String parameterName) throws SQLException {
        checkState();
        try {
            return cs.getBlob(parameterName);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public Clob getClob(int parameterIndex) throws SQLException {
        checkState();
        try {
            return cs.getClob(parameterIndex);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public Clob getClob(String parameterName) throws SQLException {
        checkState();
        try {
            return cs.getClob(parameterName);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public Array getArray(int parameterIndex) throws SQLException {
        checkState();
        try {
            return cs.getArray(parameterIndex);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public Array getArray(String parameterName) throws SQLException {
        checkState();
        try {
            return cs.getArray(parameterName);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void setBoolean(String parameterName, boolean value) throws SQLException {
        checkState();
        try {
            cs.setBoolean(parameterName, value);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void setByte(String parameterName, byte value) throws SQLException {
        checkState();
        try {
            cs.setByte(parameterName, value);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void setShort(String parameterName, short value) throws SQLException {
        checkState();
        try {
            cs.setShort(parameterName, value);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void setInt(String parameterName, int value) throws SQLException {
        checkState();
        try {
            cs.setInt(parameterName, value);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void setLong(String parameterName, long value) throws SQLException {
        checkState();
        try {
            cs.setLong(parameterName, value);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void setFloat(String parameterName, float value) throws SQLException {
        checkState();
        try {
            cs.setFloat(parameterName, value);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void setDouble(String parameterName, double value) throws SQLException {
        checkState();
        try {
            cs.setDouble(parameterName, value);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void setURL(String parameterName, URL value) throws SQLException {
        checkState();
        try {
            cs.setURL(parameterName, value);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void setTime(String parameterName, Time value) throws SQLException {
        checkState();
        try {
            cs.setTime(parameterName, value);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void setTime(String parameterName, Time value, Calendar calendar) throws SQLException {
        checkState();
        try {
            cs.setTime(parameterName, value, calendar);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void setNull(String parameterName, int value) throws SQLException {
        checkState();
        try {
            cs.setNull(parameterName, value);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void setNull(String parameterName, int sqlType, String typeName) throws SQLException {
        checkState();
        try {
            cs.setNull(parameterName, sqlType, typeName);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void setBigDecimal(String parameterName, BigDecimal value) throws SQLException {
        checkState();
        try {
            cs.setBigDecimal(parameterName, value);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void setString(String parameterName, String value) throws SQLException {
        checkState();
        try {
            cs.setString(parameterName, value);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void setBytes(String parameterName, byte[] value) throws SQLException {
        checkState();
        try {
            cs.setBytes(parameterName, value);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void setDate(String parameterName, Date value) throws SQLException {
        checkState();
        try {
            cs.setDate(parameterName, value);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void setDate(String parameterName, Date value, Calendar calendar) throws SQLException {
        checkState();
        try {
            cs.setDate(parameterName, value, calendar);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void setTimestamp(String parameterName, Timestamp value) throws SQLException {
        checkState();
        try {
            cs.setTimestamp(parameterName, value);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void setTimestamp(String parameterName, Timestamp value, Calendar calendar)
                                                                                      throws SQLException {
        checkState();
        try {
            cs.setTimestamp(parameterName, value, calendar);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void setAsciiStream(String parameterName, InputStream stream, int length)
                                                                                    throws SQLException {
        checkState();
        try {
            cs.setAsciiStream(parameterName, stream, length);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void setBinaryStream(String parameterName, InputStream stream, int length)
                                                                                     throws SQLException {
        checkState();
        try {
            cs.setBinaryStream(parameterName, stream, length);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void setObject(String parameterName, Object value, int sqlType, int scale)
                                                                                     throws SQLException {
        checkState();
        try {
            cs.setObject(parameterName, value, sqlType, scale);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void setObject(String parameterName, Object value, int sqlType) throws SQLException {
        checkState();
        try {
            cs.setObject(parameterName, value, sqlType);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void setObject(String parameterName, Object value) throws SQLException {
        checkState();
        try {
            cs.setObject(parameterName, value);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public void setCharacterStream(String parameterName, Reader reader, int length)
                                                                                   throws SQLException {
        checkState();
        try {
            cs.setCharacterStream(parameterName, reader, length);
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public Reader getCharacterStream(int parameterIndex) throws SQLException {
        return null;
    }

    public Reader getCharacterStream(String parameterName) throws SQLException {
        return null;
    }

    public Reader getNCharacterStream(int parameterIndex) throws SQLException {
        return null;
    }

    public Reader getNCharacterStream(String parameterName) throws SQLException {
        return null;
    }

    public NClob getNClob(int parameterIndex) throws SQLException {
        return null;
    }

    public NClob getNClob(String parameterName) throws SQLException {
        return null;
    }

    public String getNString(int parameterIndex) throws SQLException {
        return null;
    }

    public String getNString(String parameterName) throws SQLException {
        return null;
    }

    public RowId getRowId(int parameterIndex) throws SQLException {
        return null;
    }

    public RowId getRowId(String parameterName) throws SQLException {
        return null;
    }

    public SQLXML getSQLXML(int parameterIndex) throws SQLException {
        return null;
    }

    public SQLXML getSQLXML(String parameterName) throws SQLException {
        return null;
    }

    public void setAsciiStream(String parameterName, InputStream x) throws SQLException {
    }

    public void setAsciiStream(String parameterName, InputStream x, long length)
                                                                                throws SQLException {
    }

    public void setBinaryStream(String parameterName, InputStream x) throws SQLException {
    }

    public void setBinaryStream(String parameterName, InputStream x, long length)
                                                                                 throws SQLException {
    }

    public void setBlob(String parameterName, Blob x) throws SQLException {
    }

    public void setBlob(String parameterName, InputStream inputStream) throws SQLException {
    }

    public void setBlob(String parameterName, InputStream inputStream, long length)
                                                                                   throws SQLException {
    }

    public void setCharacterStream(String parameterName, Reader reader) throws SQLException {
    }

    public void setCharacterStream(String parameterName, Reader reader, long length)
                                                                                    throws SQLException {
    }

    public void setClob(String parameterName, Clob x) throws SQLException {
    }

    public void setClob(String parameterName, Reader reader) throws SQLException {
    }

    public void setClob(String parameterName, Reader reader, long length) throws SQLException {
    }

    public void setNCharacterStream(String parameterName, Reader value) throws SQLException {
    }

    public void setNCharacterStream(String parameterName, Reader value, long length)
                                                                                    throws SQLException {
    }

    public void setNClob(String parameterName, NClob value) throws SQLException {
    }

    public void setNClob(String parameterName, Reader reader) throws SQLException {
    }

    public void setNClob(String parameterName, Reader reader, long length) throws SQLException {
    }

    public void setNString(String parameterName, String value) throws SQLException {
    }

    public void setRowId(String parameterName, RowId x) throws SQLException {
    }

    public void setSQLXML(String parameterName, SQLXML xmlObject) throws SQLException {
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream, long length)
                                                                                 throws SQLException {
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, long length)
                                                                                  throws SQLException {
    }

    @Override
    public void setClob(int parameterIndex, Reader reader) throws SQLException {
    }

    @Override
    public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value, long length)
                                                                                  throws SQLException {
    }

    @Override
    public void setNClob(int parameterIndex, NClob value) throws SQLException {
    }

    @Override
    public void setNClob(int parameterIndex, Reader reader) throws SQLException {
    }

    @Override
    public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
    }

    @Override
    public void setNString(int parameterIndex, String value) throws SQLException {
    }

    @Override
    public void setRowId(int parameterIndex, RowId x) throws SQLException {
    }

    @Override
    public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
    }

    @Override
    public boolean isPoolable() throws SQLException {
        return false;
    }

    @Override
    public void setPoolable(boolean poolable) throws SQLException {
    }

	@Override
	public void closeOnCompletion() throws SQLException {
		
	}

	@Override
	public boolean isCloseOnCompletion() throws SQLException {
		return false;
	}

	@Override
	public <T> T getObject(int parameterIndex, Class<T> type) throws SQLException {
		return null;
	}

	@Override
	public <T> T getObject(String parameterName, Class<T> type) throws SQLException {
		return null;
	}
}
