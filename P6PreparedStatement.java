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
 * Revision 1.6  2002/04/21 06:15:34  jeffgoke
 * added test cases, fixed batch bugs
 *
 * Revision 1.5  2002/04/18 06:54:39  jeffgoke
 * added batch statement logging support
 *
 * Revision 1.4  2002/04/15 05:13:32  jeffgoke
 * Simon Sadedin added timing support.  Fixed bug where batch execute was not
 * getting logged.  Added result set timing.  Updated the log format to include
 * categories, and updated options to control the categories.  Updated
 * documentation.
 *
 * Revision 1.3  2002/04/11 04:18:03  jeffgoke
 * fixed bug where callable & prepared were not passing their ancestors the correct constructor information
 *
 * Revision 1.2  2002/04/10 04:24:26  jeffgoke
 * added support for callable statements and fixed numerous bugs that allowed the real class to be returned
 *
 * Revision 1.1.1.1  2002/04/07 04:52:25  jeffgoke
 * no message
 *
 * Revision 1.2  2001-08-05 09:16:04-05  andy
 * final version on the website
 *
 * Revision 1.1  2001-08-02 07:52:43-05  andy
 * <>
 *
 * Revision 1.0  2001-08-02 06:37:42-05  andy
 * Initial revision
 *
 *
 */

package com.p6spy.engine.spy;

import java.io.*;
import java.sql.*;
import java.math.*;

public class P6PreparedStatement extends P6Statement implements PreparedStatement {
    private  static int P6_MAX_FIELDS = 256;
    protected PreparedStatement prepStmtPassthru;
    private String preparedQuery;
    private String actualQuery;
    private Object values[];
    private boolean isString[];
    
    P6PreparedStatement(PreparedStatement statement, P6Connection conn, String query) {
        super(statement, conn, query);
        prepStmtPassthru = statement;
        this.preparedQuery = query;
        initValues();
    }
    
    private void initValues() {
        if (P6SpyOptions.getTrace()) {
            values = new Object[P6_MAX_FIELDS+1];
            isString = new boolean[P6_MAX_FIELDS+1];
        } else {
            values = null;
            isString = null;
        }
    }
    
    public void addBatch() throws SQLException {
        super.setStatementQuery(getQueryFromPreparedStatement());
        long startTime = System.currentTimeMillis();
        try {
            prepStmtPassthru.addBatch();
        }
        finally {
            if (P6SpyOptions.getTrace()) {
                P6LogQuery.logElapsed(startTime, "batch", preparedQuery, getQueryFromPreparedStatement());
            }
        }
    }
    
    public void clearParameters() throws SQLException {
        prepStmtPassthru.clearParameters();
    }
    
    public boolean execute() throws SQLException {
        long startTime = System.currentTimeMillis();
        try {
            return prepStmtPassthru.execute();
        }
        finally {
            if (P6SpyOptions.getTrace()) {
                P6LogQuery.logElapsed(startTime, "statement", preparedQuery, getQueryFromPreparedStatement());
            }
        }
    }
    
    public ResultSet executeQuery() throws SQLException {
        long startTime = System.currentTimeMillis();
        try {
            ResultSet resultSet = prepStmtPassthru.executeQuery();
            return (new P6ResultSet(resultSet, this, preparedQuery, getQueryFromPreparedStatement()));
        }
        finally {
            if (P6SpyOptions.getTrace()) {
                P6LogQuery.logElapsed(startTime, "statement", preparedQuery, getQueryFromPreparedStatement());
            }
        }
    }
    
    public int executeUpdate() throws SQLException {
        
        long startTime = System.currentTimeMillis();
        try {
            return prepStmtPassthru.executeUpdate();
        }
        finally {
            if (P6SpyOptions.getTrace()) {
                P6LogQuery.logElapsed(startTime, "statement", preparedQuery, getQueryFromPreparedStatement());
            }
        }
    }
    
    public ResultSetMetaData getMetaData() throws SQLException {
        return prepStmtPassthru.getMetaData();
    }
    
    public void setArray(int p0, Array p1) throws SQLException {
        setObjectAsString(p0, p1);
        // we need to make sure we get the real object in this case
        if (p1 instanceof P6Array) {
            prepStmtPassthru.setArray(p0,((P6Array)p1).passthru);
        } else{
            prepStmtPassthru.setArray(p0,p1);
        }
    }
    
    public void setAsciiStream(int p0, InputStream p1, int p2) throws SQLException {
        setObjectAsString(p0, p1);
        prepStmtPassthru.setAsciiStream(p0,p1,p2);
    }
    
    public void setBigDecimal(int p0, BigDecimal p1) throws SQLException {
        setObjectAsString(p0, p1);
        prepStmtPassthru.setBigDecimal(p0,p1);
    }
    
    public void setBinaryStream(int p0, InputStream p1, int p2) throws SQLException {
        setObjectAsString(p0, p1);
        prepStmtPassthru.setBinaryStream(p0,p1,p2);
    }
    
    public void setBlob(int p0, Blob p1) throws SQLException {
        setObjectAsString(p0, p1);
        prepStmtPassthru.setBlob(p0,p1);
    }
    
    public void setBoolean(int p0, boolean p1) throws SQLException {
        setObjectAsString(p0, new Boolean(p1));
        prepStmtPassthru.setBoolean(p0,p1);
    }
    
    public void setByte(int p0, byte p1) throws SQLException {
        setObjectAsString(p0, new Byte(p1));
        prepStmtPassthru.setByte(p0,p1);
    }
    
    public void setBytes(int p0, byte[] p1) throws SQLException {
        setObjectAsString(p0, p1);
        prepStmtPassthru.setBytes(p0,p1);
    }
    
    public void setCharacterStream(int p0, Reader p1, int p2) throws SQLException {
        setObjectAsString(p0, p1);
        prepStmtPassthru.setCharacterStream(p0,p1,p2);
    }
    
    public void setClob(int p0, Clob p1) throws SQLException {
        setObjectAsString(p0, p1);
        prepStmtPassthru.setClob(p0,p1);
    }
    
    public void setDate(int p0, Date p1) throws SQLException {
        setObjectAsString(p0, p1);
        prepStmtPassthru.setDate(p0,p1);
    }
    
    public void setDate(int p0, Date p1, java.util.Calendar p2) throws SQLException {
        setObjectAsString(p0, p1);
        prepStmtPassthru.setDate(p0,p1,p2);
    }
    
    public void setDouble(int p0, double p1) throws SQLException {
        setObjectAsInt(p0, new Double(p1));
        prepStmtPassthru.setDouble(p0,p1);
    }
    
    public void setFloat(int p0, float p1) throws SQLException {
        setObjectAsInt(p0, new Float(p1));
        prepStmtPassthru.setFloat(p0,p1);
    }
    
    public void setInt(int p0, int p1) throws SQLException {
        setObjectAsInt(p0, new Integer(p1));
        prepStmtPassthru.setInt(p0,p1);
    }
    
    public void setLong(int p0, long p1) throws SQLException {
        setObjectAsInt(p0, new Long(p1));
        prepStmtPassthru.setLong(p0,p1);
    }
    
    public void setNull(int p0, int p1, String p2) throws SQLException {
        setObjectAsString(p0, null);
        prepStmtPassthru.setNull(p0,p1,p2);
    }
    
    public void setNull(int p0, int p1) throws SQLException {
        setObjectAsString(p0, null);
        prepStmtPassthru.setNull(p0,p1);
    }
    
    public void setObject(int p0, Object p1, int p2, int p3) throws SQLException {
        setObjectAsString(p0, p1);
        prepStmtPassthru.setObject(p0,p1,p2,p3);
    }
    
    public void setObject(int p0, Object p1, int p2) throws SQLException {
        setObjectAsString(p0, p1);
        prepStmtPassthru.setObject(p0,p1,p2);
    }
    
    public void setObject(int p0, Object p1) throws SQLException {
        setObjectAsString(p0, p1);
        prepStmtPassthru.setObject(p0,p1);
    }
    
    public void setRef(int p0, Ref p1) throws SQLException {
        setObjectAsString(p0, p1);
        prepStmtPassthru.setRef(p0,p1);
    }
    
    public void setShort(int p0, short p1) throws SQLException {
        setObjectAsString(p0, new Short(p1));
        prepStmtPassthru.setShort(p0,p1);
    }
    
    public void setString(int p0, String p1) throws SQLException {
        setObjectAsString(p0, p1);
        prepStmtPassthru.setString(p0,p1);
    }
    
    public void setTime(int p0, Time p1, java.util.Calendar p2) throws SQLException {
        setObjectAsString(p0, p1);
        prepStmtPassthru.setTime(p0,p1,p2);
    }
    
    public void setTime(int p0, Time p1) throws SQLException {
        setObjectAsString(p0, p1);
        prepStmtPassthru.setTime(p0,p1);
    }
    
    public void setTimestamp(int p0, Timestamp p1, java.util.Calendar p2) throws SQLException {
        setObjectAsString(p0, p1);
        prepStmtPassthru.setTimestamp(p0,p1,p2);
    }
    
    public void setTimestamp(int p0, Timestamp p1) throws SQLException {
        setObjectAsString(p0, p1);
        prepStmtPassthru.setTimestamp(p0,p1);
    }
    
    public void setUnicodeStream(int p0, InputStream p1, int p2) throws SQLException {
        setObjectAsString(p0, p1);
        prepStmtPassthru.setUnicodeStream(p0,p1,p2);
    }
    
    /* we override this because the p6statement version will not be able to return
     * the accurate prepared statement or query information */
    public java.sql.ResultSet getResultSet() throws java.sql.SQLException {
        return (new P6ResultSet(passthru.getResultSet(), this, preparedQuery, getQueryFromPreparedStatement()));
    }
    
    /*
     * P6Spy specific functionality
     */
    public String getQueryFromPreparedStatement() {
        String t = new String(preparedQuery);
        
        if (values != null) {
            int i = 1, found;
            
            while ((found = t.indexOf('?')) != -1) {
                if (isString[i]) {
                    t = t.substring(0,found) + "'" + values[i] + "'" + t.substring(found+1);
                } else {
                    t = t.substring(0,found) + values[i] + t.substring(found+1);
                }
                i++;
            }
        }
        
        return(t);
    }
    
    protected  void setObjectAsString(int i, Object o) {
        if (values != null) {
            if (i >= 0 && i <= P6_MAX_FIELDS) {
                values[i] = (o == null) ? "" : o.toString();
                isString[i]  = true;
            }
        }
    }
    
    protected  void setObjectAsInt(int i, Object o) {
        if (values != null) {
            if (i >= 0 && i <= P6_MAX_FIELDS) {
                values[i] = (o == null) ? "" : o.toString();
                isString[i]  = false;
            }
        }
    }
}
