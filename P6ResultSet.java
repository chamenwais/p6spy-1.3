/*
 *
 * ====================================================================
 *
 * The P6Spy Software License, Version 1.1
 *
 * This license is derived and fully compatible with the Apache Software
 * license, see http://www.apache.org/LICENSE.txt
 *
 * Copyright (c) 2001-2002 Andy Martin, Ph.D. and Jeff Goke
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in
 * the documentation and/or other materials provided with the
 * distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 * any, must include the following acknowlegement:
 * "The original concept and code base for P6Spy was conceived
 * and developed by Andy Martin, Ph.D. who generously contribued
 * the first complete release to the public under this license.
 * This product was due to the pioneering work of Andy
 * that began in December of 1995 developing applications that could
 * seamlessly be deployed with minimal effort but with dramatic results.
 * This code is maintained and extended by Jeff Goke and with the ideas
 * and contributions of other P6Spy contributors.
 * (http://www.p6spy.com)"
 * Alternately, this acknowlegement may appear in the software itself,
 * if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "P6Spy", "Jeff Goke", and "Andy Martin" must not be used
 * to endorse or promote products derived from this software without
 * prior written permission. For written permission, please contact
 * license@p6spy.com.
 *
 * 5. Products derived from this software may not be called "P6Spy"
 * nor may "P6Spy" appear in their names without prior written
 * permission of Jeff Goke and Andy Martin.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 */

/**
 * Description: JDBC Driver Extension implementing PreparedStatement.
 *
 * $Author$
 * $Revision$
 * $Date$
 *
 * $Id$
 * $Source$
 * $Log$
 * Revision 1.3  2002/05/16 04:58:40  jeffgoke
 * Viktor Szathmary added multi-driver support.
 * Rewrote P6SpyOptions to be easier to manage.
 * Fixed several bugs.
 *
 * Revision 1.2  2002/04/15 05:13:32  jeffgoke
 * Simon Sadedin added timing support.  Fixed bug where batch execute was not
 * getting logged.  Added result set timing.  Updated the log format to include
 * categories, and updated options to control the categories.  Updated
 * documentation.
 *
 * Revision 1.1  2002/04/10 04:24:26  jeffgoke
 * added support for callable statements and fixed numerous bugs that allowed the real class to be returned
 *
 *
 *
 */

package com.p6spy.engine.spy;

import java.io.*;
import java.sql.*;
import java.math.*;
import java.util.*;
import java.text.*;

public final class P6ResultSet implements ResultSet {
    protected ResultSet passthru;
    protected P6Statement statement;
    protected String query;
    protected String preparedQuery;
    
    public P6ResultSet(ResultSet resultSet, P6Statement statement, String preparedQuery, String query) {
        this.passthru = resultSet;
        this.statement = statement;
        this.query = query;
        this.preparedQuery = preparedQuery;;
    }
    
    // we should be logging this for timing purposes
    public final boolean next() throws SQLException {
        long startTime = System.currentTimeMillis();
        try {
            return passthru.next();
        }
        finally {
            if (P6SpyOptions.getTrace()) {
                P6LogQuery.logElapsed(this.statement.connection, startTime, "result", preparedQuery, query);
            }
        }
    }
    
    public final int getRow() throws SQLException {
        return passthru.getRow();
    }
    
    public final byte[] getBytes(String p0) throws SQLException {
        return passthru.getBytes(p0);
    }
    
    public final byte[] getBytes(int p0) throws SQLException {
        return passthru.getBytes(p0);
    }
    
    public final boolean getBoolean(int p0) throws SQLException {
        return passthru.getBoolean(p0);
    }
    
    public final boolean getBoolean(String p0) throws SQLException {
        return passthru.getBoolean(p0);
    }
    
    public final int getType() throws SQLException {
        return passthru.getType();
    }
    
    public final long getLong(int p0) throws SQLException {
        return passthru.getLong(p0);
    }
    
    public final long getLong(String p0) throws SQLException {
        return passthru.getLong(p0);
    }
    
    public final boolean previous() throws SQLException {
        return passthru.previous();
    }
    
    public final void close() throws SQLException {
        passthru.close();
    }
    
    public final Object getObject(String p0, java.util.Map p1) throws SQLException {
        return passthru.getObject(p0,p1);
    }
    
    public final Object getObject(int p0) throws SQLException {
        return passthru.getObject(p0);
    }
    
    public final Object getObject(String p0) throws SQLException {
        return passthru.getObject(p0);
    }
    
    public final Object getObject(int p0, java.util.Map p1) throws SQLException {
        return passthru.getObject(p0,p1);
    }
    
    public final Ref getRef(String p0) throws SQLException {
        return passthru.getRef(p0);
    }
    
    public final Ref getRef(int p0) throws SQLException {
        return passthru.getRef(p0);
    }
    
    public final Time getTime(int p0, java.util.Calendar p1) throws SQLException {
        return passthru.getTime(p0,p1);
    }
    
    public final Time getTime(String p0, java.util.Calendar p1) throws SQLException {
        return passthru.getTime(p0,p1);
    }
    
    public final Time getTime(String p0) throws SQLException {
        return passthru.getTime(p0);
    }
    
    public final Time getTime(int p0) throws SQLException {
        return passthru.getTime(p0);
    }
    
    public final java.sql.Date getDate(int p0) throws SQLException {
        return passthru.getDate(p0);
    }
    
    public final java.sql.Date getDate(String p0, java.util.Calendar p1) throws SQLException {
        return passthru.getDate(p0);
    }
    
    public final java.sql.Date getDate(String p0) throws SQLException {
        return passthru.getDate(p0);
    }
    
    public final java.sql.Date getDate(int p0, java.util.Calendar p1) throws SQLException {
        return passthru.getDate(p0,p1);
    }
    
    public final boolean wasNull() throws SQLException {
        return passthru.wasNull();
    }
    
    public final String getString(String p0) throws SQLException {
        return passthru.getString(p0);
    }
    
    public final String getString(int p0) throws SQLException {
        return passthru.getString(p0);
    }
    
    public final byte getByte(String p0) throws SQLException {
        return passthru.getByte(p0);
    }
    
    public final byte getByte(int p0) throws SQLException {
        return passthru.getByte(p0);
    }
    
    public final short getShort(String p0) throws SQLException {
        return passthru.getShort(p0);
    }
    
    public final short getShort(int p0) throws SQLException {
        return passthru.getShort(p0);
    }
    
    public final int getInt(int p0) throws SQLException {
        return passthru.getInt(p0);
    }
    
    public final int getInt(String p0) throws SQLException {
        return passthru.getInt(p0);
    }
    
    public final float getFloat(String p0) throws SQLException {
        return passthru.getFloat(p0);
    }
    
    public final float getFloat(int p0) throws SQLException {
        return passthru.getFloat(p0);
    }
    
    public final double getDouble(int p0) throws SQLException {
        return passthru.getDouble(p0);
    }
    
    public final double getDouble(String p0) throws SQLException {
        return passthru.getDouble(p0);
    }
    
    public final BigDecimal getBigDecimal(String p0) throws SQLException {
        return passthru.getBigDecimal(p0);
    }
    
    public final BigDecimal getBigDecimal(int p0) throws SQLException {
        return passthru.getBigDecimal(p0);
    }
    
    public final BigDecimal getBigDecimal(int p0, int p1) throws SQLException {
        return passthru.getBigDecimal(p0,p1);
    }
    
    public final BigDecimal getBigDecimal(String p0, int p1) throws SQLException {
        return passthru.getBigDecimal(p0,p1);
    }
    
    public final Timestamp getTimestamp(String p0) throws SQLException {
        return passthru.getTimestamp(p0);
    }
    
    public final Timestamp getTimestamp(String p0, java.util.Calendar p1) throws SQLException {
        return passthru.getTimestamp(p0,p1);
    }
    
    public final Timestamp getTimestamp(int p0) throws SQLException {
        return passthru.getTimestamp(p0);
    }
    
    public final Timestamp getTimestamp(int p0, java.util.Calendar p1) throws SQLException {
        return passthru.getTimestamp(p0,p1);
    }
    
    public final InputStream getAsciiStream(String p0) throws SQLException {
        return passthru.getAsciiStream(p0);
    }
    
    public final InputStream getAsciiStream(int p0) throws SQLException {
        return passthru.getAsciiStream(p0);
    }
    
    public final InputStream getUnicodeStream(int p0) throws SQLException {
        return passthru.getUnicodeStream(p0);
    }
    
    public final InputStream getUnicodeStream(String p0) throws SQLException {
        return passthru.getUnicodeStream(p0);
    }
    
    public final InputStream getBinaryStream(int p0) throws SQLException {
        return passthru.getBinaryStream(p0);
    }
    
    public final InputStream getBinaryStream(String p0) throws SQLException {
        return passthru.getBinaryStream(p0);
    }
    
    public final SQLWarning getWarnings() throws SQLException {
        return passthru.getWarnings();
    }
    
    public final void clearWarnings() throws SQLException {
        clearWarnings();
    }
    
    public final String getCursorName() throws SQLException {
        return passthru.getCursorName();
    }
    
    public final ResultSetMetaData getMetaData() throws SQLException {
        return passthru.getMetaData();
    }
    
    public final int findColumn(String p0) throws SQLException {
        return passthru.findColumn(p0);
    }
    
    public final Reader getCharacterStream(String p0) throws SQLException {
        return passthru.getCharacterStream(p0);
    }
    
    public final Reader getCharacterStream(int p0) throws SQLException {
        return passthru.getCharacterStream(p0);
    }
    
    public final boolean isBeforeFirst() throws SQLException {
        return passthru.isBeforeFirst();
    }
    
    public final boolean isAfterLast() throws SQLException {
        return passthru.isAfterLast();
    }
    
    public final boolean isFirst() throws SQLException {
        return passthru.isFirst();
    }
    
    public final boolean isLast() throws SQLException {
        return passthru.isLast();
    }
    
    public final void beforeFirst() throws SQLException {
        passthru.beforeFirst();
    }
    
    public final void afterLast() throws SQLException {
        passthru.afterLast();
    }
    
    public final boolean first() throws SQLException {
        return passthru.first();
    }
    
    public final boolean last() throws SQLException {
        return passthru.last();
    }
    
    public final boolean absolute(int p0) throws SQLException {
        return passthru.absolute(p0);
    }
    
    public final boolean relative(int p0) throws SQLException {
        return passthru.relative(p0);
    }
    
    public final void setFetchDirection(int p0) throws SQLException {
        passthru.setFetchDirection(p0);
    }
    
    public final int getFetchDirection() throws SQLException {
        return passthru.getFetchDirection();
    }
    
    public final void setFetchSize(int p0) throws SQLException {
        passthru.setFetchSize(p0);
    }
    
    public final int getFetchSize() throws SQLException {
        return passthru.getFetchSize();
    }
    
    public final int getConcurrency() throws SQLException {
        return passthru.getConcurrency();
    }
    
    public final boolean rowUpdated() throws SQLException {
        return passthru.rowUpdated();
    }
    
    public final boolean rowInserted() throws SQLException {
        return passthru.rowInserted();
    }
    
    public final boolean rowDeleted() throws SQLException {
        return passthru.rowDeleted();
    }
    
    public final void updateNull(int p0) throws SQLException {
        passthru.updateNull(p0);
    }
    
    public final void updateNull(String p0) throws SQLException {
        passthru.updateNull(p0);
    }
    
    public final void updateBoolean(int p0, boolean p1) throws SQLException {
        passthru.updateBoolean(p0,p1);
    }
    
    public final void updateBoolean(String p0, boolean p1) throws SQLException {
        passthru.updateBoolean(p0,p1);
    }
    
    public final void updateByte(String p0, byte p1) throws SQLException {
        passthru.updateByte(p0,p1);
    }
    
    public final void updateByte(int p0, byte p1) throws SQLException {
        passthru.updateByte(p0,p1);
    }
    
    public final void updateShort(int p0, short p1) throws SQLException {
        passthru.updateShort(p0,p1);
    }
    
    public final void updateShort(String p0, short p1) throws SQLException {
        passthru.updateShort(p0,p1);
    }
    
    public final void updateInt(int p0, int p1) throws SQLException {
        passthru.updateInt(p0,p1);
    }
    
    public final void updateInt(String p0, int p1) throws SQLException {
        passthru.updateInt(p0,p1);
    }
    
    public final void updateLong(int p0, long p1) throws SQLException {
        passthru.updateLong(p0,p1);
    }
    
    public final void updateLong(String p0, long p1) throws SQLException {
        passthru.updateLong(p0,p1);
    }
    
    public final void updateFloat(String p0, float p1) throws SQLException {
        passthru.updateFloat(p0,p1);
    }
    
    public final void updateFloat(int p0, float p1) throws SQLException {
        passthru.updateFloat(p0,p1);
    }
    
    public final void updateDouble(int p0, double p1) throws SQLException {
        passthru.updateDouble(p0,p1);
    }
    
    public final void updateDouble(String p0, double p1) throws SQLException {
        passthru.updateDouble(p0,p1);
    }
    
    public final void updateBigDecimal(String p0, BigDecimal p1) throws SQLException {
        passthru.updateBigDecimal(p0,p1);
    }
    
    public final void updateBigDecimal(int p0, BigDecimal p1) throws SQLException {
        passthru.updateBigDecimal(p0,p1);
    }
    
    public final void updateString(String p0, String p1) throws SQLException {
        passthru.updateString(p0,p1);
    }
    
    public final void updateString(int p0, String p1) throws SQLException {
        passthru.updateString(p0,p1);
    }
    
    public final void updateBytes(int p0, byte[] p1) throws SQLException {
        passthru.updateBytes(p0,p1);
    }
    
    public final void updateBytes(String p0, byte[] p1) throws SQLException {
        passthru.updateBytes(p0,p1);
    }
    
    public final void updateDate(int p0, java.sql.Date p1) throws SQLException {
        passthru.updateDate(p0,p1);
    }
    
    public final void updateDate(String p0, java.sql.Date p1) throws SQLException {
        passthru.updateDate(p0,p1);
    }
    
    public final void updateTime(String p0, Time p1) throws SQLException {
        passthru.updateTime(p0,p1);
    }
    
    public final void updateTime(int p0, Time p1) throws SQLException {
        passthru.updateTime(p0,p1);
    }
    
    public final void updateTimestamp(int p0, Timestamp p1) throws SQLException {
        passthru.updateTimestamp(p0,p1);
    }
    
    public final void updateTimestamp(String p0, Timestamp p1) throws SQLException {
        passthru.updateTimestamp(p0,p1);
    }
    
    public final void updateAsciiStream(int p0, InputStream p1, int p2) throws SQLException {
        passthru.updateAsciiStream(p0,p1,p2);
    }
    
    public final void updateAsciiStream(String p0, InputStream p1, int p2) throws SQLException {
        passthru.updateAsciiStream(p0,p1,p2);
    }
    
    public final void updateBinaryStream(int p0, InputStream p1, int p2) throws SQLException {
        passthru.updateBinaryStream(p0,p1,p2);
    }
    
    public final void updateBinaryStream(String p0, InputStream p1, int p2) throws SQLException {
        passthru.updateBinaryStream(p0,p1,p2);
    }
    
    public final void updateCharacterStream(int p0, Reader p1, int p2) throws SQLException {
        passthru.updateCharacterStream(p0,p1,p2);
    }
    
    public final void updateCharacterStream(String p0, Reader p1, int p2) throws SQLException {
        passthru.updateCharacterStream(p0,p1,p2);
    }
    
    public final void updateObject(int p0, Object p1) throws SQLException {
        passthru.updateObject(p0,p1);
    }
    
    public final void updateObject(int p0, Object p1, int p2) throws SQLException {
        passthru.updateObject(p0,p1,p2);
    }
    
    public final void updateObject(String p0, Object p1) throws SQLException {
        passthru.updateObject(p0,p1);
    }
    
    public final void updateObject(String p0, Object p1, int p2) throws SQLException {
        passthru.updateObject(p0,p1,p2);
    }
    
    public final void insertRow() throws SQLException {
        passthru.insertRow();
    }
    
    public final void updateRow() throws SQLException {
        passthru.updateRow();
    }
    
    public final void deleteRow() throws SQLException {
        passthru.deleteRow();
    }
    
    public final void refreshRow() throws SQLException {
        passthru.refreshRow();
    }
    
    public final void cancelRowUpdates() throws SQLException {
        passthru.cancelRowUpdates();
    }
    
    public final void moveToInsertRow() throws SQLException {
        passthru.moveToInsertRow();
    }
    
    public final void moveToCurrentRow() throws SQLException {
        passthru.moveToCurrentRow();
    }
    
    public final Statement getStatement() throws SQLException {
        return this.statement;
    }
    
    public final Blob getBlob(int p0) throws SQLException {
        return passthru.getBlob(p0);
    }
    
    public final Blob getBlob(String p0) throws SQLException {
        return passthru.getBlob(p0);
    }
    
    public final Clob getClob(String p0) throws SQLException {
        return passthru.getClob(p0);
    }
    
    public final Clob getClob(int p0) throws SQLException {
        return passthru.getClob(p0);
    }
    
    public final Array getArray(int p0) throws SQLException {
        return new P6Array(passthru.getArray(p0),statement,preparedQuery,query);
    }
    
    public final Array getArray(String p0) throws SQLException {
        return new P6Array(passthru.getArray(p0),statement,preparedQuery,query);
    }
    
}

