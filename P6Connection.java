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
 * Description: Wrapper class for Connection
 *
 * $Author$
 * $Revision$
 * $Date$
 *
 * $Id$
 * $Log$
 * Revision 1.3  2002/04/10 04:24:26  jeffgoke
 * added support for callable statements and fixed numerous bugs that allowed the real class to be returned
 *
 * Revision 1.2  2002/04/07 20:43:59  jeffgoke
 * fixed bug that caused null connection to return an empty connection instead of null.
 * added an option allowing the user to truncate.
 * added a release target to the build to create the release files.
 *
 * Revision 1.1.1.1  2002/04/07 04:52:25  jeffgoke
 * no message
 *
 * Revision 1.2  2001-08-02 07:52:43-05  andy
 * <>
 *
 * Revision 1.1  2001-07-30 23:03:31-05  andy
 * <>
 *
 * Revision 1.0  2001-07-30 17:46:22-05  andy
 * Initial revision
 *
 */

package com.p6spy.engine.spy;

import java.sql.*;
import java.util.*;

public class P6Connection implements java.sql.Connection {
    private Connection passthru;
    
    public P6Connection(Connection conn) throws SQLException {
        this.passthru = conn;
    }
    
    public final void setReadOnly(boolean p0) throws SQLException {
        passthru.setReadOnly(p0);
    }
    
    public final void close() throws SQLException {
        passthru.close();
    }
    
    public final boolean isClosed() throws SQLException {
        return(passthru.isClosed());
    }
    
    public final boolean isReadOnly() throws SQLException {
        return(passthru.isReadOnly());
    }
    
    public final Statement createStatement() throws SQLException {
        return(new P6Statement(passthru));
    }
    
    public final Statement createStatement(int p0, int p1) throws SQLException {
        return(new P6Statement(passthru,p0,p1));
    }
    
    public final PreparedStatement prepareStatement(String p0) throws SQLException {
        return new P6PreparedStatement(passthru,p0);
    }
    
    public final PreparedStatement prepareStatement(String p0, int p1, int p2) throws SQLException {
        return new P6PreparedStatement(passthru,p0,p1,p2);
    }
    
    public final CallableStatement prepareCall(String p0) throws SQLException {
        return new P6CallableStatement(passthru,p0);
    }
    
    public final CallableStatement prepareCall(String p0, int p1, int p2) throws SQLException {
        return new P6CallableStatement(passthru,p0,p1,p2);
    }
    
    public final String nativeSQL(String p0) throws SQLException {
        return(passthru.nativeSQL(p0));
    }
    
    public final void setAutoCommit(boolean p0) throws SQLException {
        passthru.setAutoCommit(p0);
    }
    
    public final boolean getAutoCommit() throws SQLException {
        return(passthru.getAutoCommit());
    }
    
    public final void commit() throws SQLException {
        passthru.commit();
    }
    
    public final void rollback() throws SQLException {
        passthru.rollback();
    }
    
    public final DatabaseMetaData getMetaData() throws SQLException {
        return new P6DatabaseMetaData(passthru.getMetaData(), this);
    }
    
    public final void setCatalog(String p0) throws SQLException {
        passthru.setCatalog(p0);
    }
    
    public final String getCatalog() throws SQLException {
        return(passthru.getCatalog());
    }
    
    public final void setTransactionIsolation(int p0) throws SQLException {
        passthru.setTransactionIsolation(p0);
    }
    
    public final int getTransactionIsolation() throws SQLException {
        return(passthru.getTransactionIsolation());
    }
    
    public final SQLWarning getWarnings() throws SQLException {
        return(passthru.getWarnings());
    }
    
    public final void clearWarnings() throws SQLException {
        passthru.clearWarnings();
    }
    
    public final java.util.Map getTypeMap() throws SQLException {
        return(passthru.getTypeMap());
    }
    
    public final void setTypeMap(java.util.Map p0) throws SQLException {
        passthru.setTypeMap(p0);
    }
}
