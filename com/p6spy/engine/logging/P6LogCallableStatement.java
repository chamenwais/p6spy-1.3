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
 * Description: JDBC Driver Extension implementing CallableStatement.
 *
 * $Author$
 * $Revision$
 * $Date$
 *
 * $Id$
 * $Source$
 * $Log$
 * Revision 1.2  2002/10/06 18:22:12  jeffgoke
 * no message
 *
 * Revision 1.1  2002/05/24 07:31:45  jeffgoke
 * version 1 rewrite
 *
 *
 *
 */

package com.p6spy.engine.logging;

import com.p6spy.engine.spy.*;
import com.p6spy.engine.common.*;
import java.io.*;
import java.sql.*;
import java.math.*;

public class P6LogCallableStatement extends P6CallableStatement implements java.sql.CallableStatement {
    
    protected P6Factory getP6Factory() {
        return new P6LogFactory();
    }
    
    // ---------------------------------------------------------------------------------------
    // considered delegation for this, but that doesn't quite work because P6CallableStatement
    // manipulates some values - so we would have to make P6CallableStatement delegate as well,
    // which really defeats the purpose.  this means we do have to copy all of the methods
    // we want to use in P6Statement and P6PreparedStatement.  to understand why we are doing this
    // realize that P6LogCallableStatement inherits from P6Callabletatement which inherits from
    // P6PreparedStatement, which in turn inherits from P6Statement.  So P6LogCallableStatement
    // never inherits from P6LogPreparedStatement and therefore it does not inherit any of the
    // functionality we define in P6PreparedLogStatement.
    // ---------------------------------------------------------------------------------------
    
    public P6LogCallableStatement(CallableStatement statement, P6Connection conn, String query) {
        super(statement, conn, query);
    }
    
    public void addBatch() throws SQLException {
        P6SpyOptions.checkReload();
        statementQuery = getQueryFromPreparedStatement();
        long startTime = System.currentTimeMillis();
        try {
            prepStmtPassthru.addBatch();
        }
        finally {
            if (P6SpyOptions.getTrace()) {
                P6LogQuery.logElapsed(this.connection.getId(), startTime, "batch", preparedQuery, getQueryFromPreparedStatement());
            }
        }
    }
    
    public boolean execute() throws SQLException {
        P6SpyOptions.checkReload();
        long startTime = System.currentTimeMillis();
        
        try {
            return prepStmtPassthru.execute();
        }
        finally {
            if (P6SpyOptions.getTrace()) {
                P6LogQuery.logElapsed(this.connection.getId(), startTime, "statement", preparedQuery, getQueryFromPreparedStatement());
            }
        }
    }
    
    public ResultSet executeQuery() throws SQLException {
        P6SpyOptions.checkReload();
        long startTime = System.currentTimeMillis();
        
        try {
            ResultSet resultSet = prepStmtPassthru.executeQuery();
            return (new P6ResultSet(resultSet, this, preparedQuery, getQueryFromPreparedStatement()));
        }
        finally {
            if (P6SpyOptions.getTrace()) {
                P6LogQuery.logElapsed(this.connection.getId(), startTime, "statement", preparedQuery, getQueryFromPreparedStatement());
            }
        }
    }
    
    public int executeUpdate() throws SQLException {
        P6SpyOptions.checkReload();
        long startTime = System.currentTimeMillis();
        
        try {
            return prepStmtPassthru.executeUpdate();
        }
        finally {
            if (P6SpyOptions.getTrace()) {
                P6LogQuery.logElapsed(this.connection.getId(), startTime, "statement", preparedQuery, getQueryFromPreparedStatement());
            }
        }
    }
    
    public boolean execute(String p0) throws java.sql.SQLException {
        P6SpyOptions.checkReload();
        statementQuery = p0;
        long startTime = System.currentTimeMillis();
        
        try {
            return passthru.execute(p0);
        }
        finally {
            if (P6SpyOptions.getTrace()) {
                P6LogQuery.logElapsed(this.connection.getId(), startTime, "statement", "", p0);
            }
        }
    }
    
    public ResultSet executeQuery(String p0) throws java.sql.SQLException {
        P6SpyOptions.checkReload();
        statementQuery = p0;
        long startTime = System.currentTimeMillis();
        
        try {
            return (new P6ResultSet(passthru.executeQuery(p0), this, "", p0));
        }
        finally {
            if (P6SpyOptions.getTrace()) {
                P6LogQuery.logElapsed(this.connection.getId(), startTime, "statement", "", p0);
            }
        }
    }
    
    public int executeUpdate(String p0) throws java.sql.SQLException {
        P6SpyOptions.checkReload();
        statementQuery = p0;
        long startTime = System.currentTimeMillis();
        
        try {
            return(passthru.executeUpdate(p0));
        }
        finally {
            if (P6SpyOptions.getTrace()) {
                P6LogQuery.logElapsed(this.connection.getId(), startTime, "statement", "", p0);
            }
        }
    }
    
    public void addBatch(String p0) throws java.sql.SQLException {
        P6SpyOptions.checkReload();
        statementQuery = p0;
        long startTime = System.currentTimeMillis();
        
        try {
            passthru.addBatch(p0);
        }
        finally {
            if (P6SpyOptions.getTrace()) {
                P6LogQuery.logElapsed(this.connection.getId(), startTime, "batch", "", p0);
            }
        }
    }
    
    public int[] executeBatch() throws java.sql.SQLException {
        P6SpyOptions.checkReload();
        long startTime = System.currentTimeMillis();
        
        try {
            return(passthru.executeBatch());
        }
        finally {
            if (P6SpyOptions.getTrace()) {
                P6LogQuery.logElapsed(this.connection.getId(), startTime, "statement", preparedQuery, statementQuery);
            }
        }
    }
    
}