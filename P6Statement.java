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
 * Description: Wrapper class for Statement
 *
 * $Author$
 * $Revision$
 * $Date$
 *
 * $Id$
 * $Log$
 * Revision 1.1.1.1  2002/04/07 04:52:26  jeffgoke
 * no message
 *
 * Revision 1.3  2001-08-05 09:16:04-05  andy
 * final version on the website
 *
 * Revision 1.2  2001-08-02 07:52:44-05  andy
 * <>
 *
 * Revision 1.1  2001-07-30 23:03:31-05  andy
 * <>
 *
 * Revision 1.0  2001-07-30 17:46:23-05  andy
 * Initial revision
 *
 *
 */

package com.p6spy.engine.spy;

import java.sql.*;

public class P6Statement implements Statement {
    protected Statement passthru;

    public P6Statement () {}

    public P6Statement(Connection conn) throws SQLException {
        passthru = conn.createStatement();
    }

    public P6Statement(Connection conn, int p0, int p1) throws SQLException {
        passthru = conn.createStatement(p0, p1);
    }

    public final void close() throws java.sql.SQLException {
        passthru.close();
    }

    public boolean execute(String p0) throws java.sql.SQLException {
        if (P6SpyOptions.getTrace()) {
            P6LogQuery.log(p0);
        }
        return(passthru.execute(p0));
    }

    public java.sql.ResultSet executeQuery(String p0) throws java.sql.SQLException {
        if (P6SpyOptions.getTrace()) {
            P6LogQuery.log(p0);
        }
        return(passthru.executeQuery(p0));
    }

    public int executeUpdate(String p0) throws java.sql.SQLException {
        if (P6SpyOptions.getTrace()) {
            P6LogQuery.log(p0);
        }
        return(passthru.executeUpdate(p0));
    }

    public final int getMaxFieldSize() throws java.sql.SQLException {
        return(passthru.getMaxFieldSize());
    }

    public final void setMaxFieldSize(int p0) throws java.sql.SQLException {
        passthru.setMaxFieldSize(p0);
    }

    public final int getMaxRows() throws java.sql.SQLException {
        return(passthru.getMaxRows());
    }

    public final void setMaxRows(int p0) throws java.sql.SQLException {
        passthru.setMaxRows(p0);
    }

    public final void setEscapeProcessing(boolean p0) throws java.sql.SQLException {
        passthru.setEscapeProcessing(p0);
    }

    public final int getQueryTimeout() throws java.sql.SQLException {
        return(passthru.getQueryTimeout());
    }

    public final void setQueryTimeout(int p0) throws java.sql.SQLException {
        passthru.setQueryTimeout(p0);
    }

    public final void cancel() throws java.sql.SQLException {
        passthru.cancel();
    }

    public final java.sql.SQLWarning getWarnings() throws java.sql.SQLException {
        return(passthru.getWarnings());
    }

    public final void clearWarnings() throws java.sql.SQLException {
        passthru.clearWarnings();
    }

    public final void setCursorName(String p0) throws java.sql.SQLException {
        passthru.setCursorName(p0);
    }

    public final java.sql.ResultSet getResultSet() throws java.sql.SQLException {
        return(passthru.getResultSet());
    }

    public final int getUpdateCount() throws java.sql.SQLException {
        return(passthru.getUpdateCount());
    }

    public final boolean getMoreResults() throws java.sql.SQLException {
        return(passthru.getMoreResults());
    }

    public final void setFetchDirection(int p0) throws java.sql.SQLException {
        passthru.setFetchDirection(p0);
    }

    public final int getFetchDirection() throws java.sql.SQLException {
        return(passthru.getFetchDirection());
    }

    public final void setFetchSize(int p0) throws java.sql.SQLException {
        passthru.setFetchSize(p0);
    }

    public final int getFetchSize() throws java.sql.SQLException {
        return(passthru.getFetchSize());
    }

    public final int getResultSetConcurrency() throws java.sql.SQLException {
        return(passthru.getResultSetConcurrency());
    }

    public final int getResultSetType() throws java.sql.SQLException {
        return(passthru.getResultSetType());
    }

    public final void addBatch(String p0) throws java.sql.SQLException {
        passthru.addBatch(p0);
    }

    public final void clearBatch() throws java.sql.SQLException {
        passthru.clearBatch();
    }

    public final int[] executeBatch() throws java.sql.SQLException {
        return(passthru.executeBatch());
    }

    public final java.sql.Connection getConnection() throws java.sql.SQLException {
        return(passthru.getConnection());
    }
}
