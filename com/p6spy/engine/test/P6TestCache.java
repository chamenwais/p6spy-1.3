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
 * Description: Test class for statements
 *
 * $Author$
 * $Revision$
 * $Date$
 *
 * $Id$
 * $Source$
 * $Log$
 * Revision 1.1  2002/10/06 18:24:04  jeffgoke
 * no message
 *
 * Revision 1.1  2002/05/24 07:30:46  jeffgoke
 * version 1 rewrite
 *
 * Revision 1.7  2002/05/18 06:39:52  jeffgoke
 * Peter Laird added Outage detection.  Added junit tests for outage detection.
 * Fixed multi-driver tests.
 *
 * Revision 1.6  2002/05/16 04:58:40  jeffgoke
 * Viktor Szathmary added multi-driver support.
 * Rewrote P6SpyOptions to be easier to manage.
 * Fixed several bugs.
 *
 * Revision 1.5  2002/05/05 00:43:00  jeffgoke
 * Added Philip's reload code.
 *
 * Revision 1.4  2002/04/27 20:24:01  jeffgoke
 * added logging of commit statements and rollback statements
 *
 * Revision 1.3  2002/04/25 06:51:28  jeffgoke
 * Philip Ogren of BEA contributed installation instructions for BEA WebLogic Portal and Server
 * Jakarta RegEx support (contributed by Philip Ogren)
 * Ability to print stack trace of logged statements. This is very useful to understand where a logged query is being executed in the application (contributed by Philip Ogren)
 * Simplified table monitoring property file option (contributed by Philip Ogren)
 * Updated the RegEx documentation
 *
 * Revision 1.2  2002/04/22 02:26:06  jeffgoke
 * Simon Sadedin added timing information.  Added Junit tests.
 *
 * Revision 1.1  2002/04/21 06:16:20  jeffgoke
 * added test cases, fixed batch bugs
 *
 *
 *
 */

package com.p6spy.engine.test;

import junit.framework.*;
import java.sql.*;
import java.util.Properties;
import java.io.*;
import java.util.*;

import com.p6spy.engine.spy.*;
import com.p6spy.engine.common.*;

public class P6TestCache extends P6TestFramework {
    
    public P6TestCache(java.lang.String testName) {
        super(testName);
    }
    
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(P6TestCache.class);
        return suite;
    }
    
    protected void setUp() {
        super.setUp();
        try {
            Statement statement = getStatement("drop table cache_test");
            drop(statement);
            statement.execute("create table cache_test (col1 varchar2(255), col2 number(5))");
        } catch (Exception e) {
            fail(e.getMessage()+" due to error: "+getStackTrace(e));
        }
    }
    
    public void testQueryUpdate() {
        try {
            // test a basic insert
            String update = "insert into cache_test values (\'bob\', 5)";
            Statement statement = getStatement(update);
            statement.executeUpdate(update);
            assertTrue(P6LogQuery.getLastEntry().indexOf(update) != -1);
            
            // test a basic select -- we set up a test cache in testframework
            // that takes 10 seconds so now it should be valid
            assertEquals(1, countRows());
            
            // let's insert 3 more of these & query again
            update = "insert into cache_test values (\'bob2\', 6)";
            statement.executeUpdate(update);
            update = "insert into cache_test values (\'bob3\', 7)";
            statement.executeUpdate(update);
            update = "insert into cache_test values (\'bob4\', 8)";
            statement.executeUpdate(update);
            
            // the old result should still be cached
            assertEquals(1, countRows());
            
            // finally let's wait 10 seconds and try one last time
            // we should get back the correct number
            Thread.sleep(7000L);
            assertEquals(4, countRows());
            
            // now let's try a prepared statement
            PreparedStatement ps = getPreparedStatement("select col1, col2 from cache_test where col1 != ? and col1 != ? and col1 like ?");
            ps.setString(1, "random");
            ps.setString(2, "morerandom");
            ps.setString(3, "%4");
            
            ResultSet rs = ps.executeQuery();
            rs.next();
            assertEquals("bob4", rs.getString(1));
            assertEquals(8, rs.getInt(2));
            
            // this could be an issue?
            // now let's perform a quick update
            //PreparedStatement updatePs = getPreparedStatement("update cache_test set col2 = ? where col1 = ?");
            //ps.setInt(1, 10);
            //ps.setString(2, "bob4");
            //updatePs.executeUpdate();
            
            update = "update cache_test set col2 = 10 where col1 = 'bob4'";
            statement.executeUpdate(update);
            
            // let's re-execute the first query, should still be cached
            rs = ps.executeQuery();
            rs.next();
            assertEquals("bob4", rs.getString(1));
            assertEquals(8, rs.getInt(2));
            
            Thread.sleep(7000L);
            rs = ps.executeQuery();
            rs.next();
            assertEquals("bob4", rs.getString(1));
            assertEquals(10, rs.getInt(2));           
            
        } catch (Exception e) {
            fail(e.getMessage()+" due to error: "+getStackTrace(e));
        }
    }
    
    protected int countRows() throws SQLException {
        String query = "select count(*) from cache_test";
        Statement statement = getStatement(query);
        ResultSet rs = statement.executeQuery(query);
        rs.next();
        return (rs.getInt(1));
    }
    
    protected void tearDown() {
        try {
            super.tearDown();
            Statement statement = getStatement("drop table cache_test");
            drop(statement);
        }  catch (Exception e) {
            fail(e.getMessage());
        }
    }
    
    protected void drop(Statement statement) {
        if (statement == null) { return; }
        dropStatement("drop table cache_test", statement);
    }
    
    protected void dropStatement(String sql, Statement statement) {
        try {
            statement.execute(sql);
        } catch (Exception e) {
            // we don't really care about cleanup failing
        }
    }
    
    protected Statement getStatement(String query) throws SQLException {
        return (connection.createStatement());
    }
    
    protected PreparedStatement getPreparedStatement(String statement) throws SQLException {
        return (connection.prepareStatement(statement));
    }
}
