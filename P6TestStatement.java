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

package com.p6spy.engine.spy;

import junit.framework.*;
import java.sql.*;
import java.util.Properties;
import java.io.*;
import java.util.*;

public class P6TestStatement extends P6TestFramework {
    
    public P6TestStatement(java.lang.String testName) {
        super(testName);
    }
    
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(P6TestStatement.class);
        return suite;
    }
    
    protected void setUp() {
        super.setUp();
        try {
            Statement statement = connection.createStatement();
            drop(statement);
            statement.execute("create table stmt_test (col1 varchar2(255), col2 number(5))");
            statement.close();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    
    public void testQueryUpdate() {
        try {
            // test a basic insert
            Statement statement = connection.createStatement();
            String update = "insert into stmt_test values (\'bob\', 5)";
            statement.executeUpdate(update);
            assertTrue(P6LogQuery.getLastEntry().indexOf(update) != -1);
            
            // test a basic select
            String query = "select count(*) from stmt_test";
            ResultSet rs = statement.executeQuery(query);
            assertTrue(P6LogQuery.getLastEntry().indexOf(query) != -1);
            rs.next();
            assertEquals(1, rs.getInt(1));
            
            // test batch inserts
            update = "insert into stmt_test values (\'jim\', 6)";
            statement.addBatch(update);
            update = "insert into stmt_test values (\'billy\', 7)";
            statement.addBatch(update);
            update = "insert into stmt_test values (\'bambi\', 8)";
            statement.addBatch(update);
            statement.executeBatch();
            assertTrue(P6LogQuery.getLastEntry().indexOf(update) != -1);
            
            query = "select count(*) from stmt_test";
            rs = statement.executeQuery(query);
            rs.next();
            assertEquals(4, rs.getInt(1));
            
            statement.close();
        } catch (Exception e) {
            fail(e.getMessage()+" due to error: "+getStackTrace(e));
        }
    }
    
    public void testMatcher() {
        try {
            // test the default matcher
            P6SpyOptions.setStringmatcher("");
            
            // first should match
            P6SpyOptions.setFilter(true);
            P6LogQuery.excludeTables = P6LogQuery.parseCSVList("");
            P6LogQuery.includeTables = P6LogQuery.parseCSVList("");
            Statement statement = connection.createStatement();
            String query = "select count(*) from stmt_test";
            statement.executeQuery(query);
            assertTrue(P6LogQuery.getLastEntry().indexOf(query) != -1);
            
            // now it should fail due to filter = false
            P6SpyOptions.setFilter(false);
            P6LogQuery.excludeTables = P6LogQuery.parseCSVList("");
            P6LogQuery.includeTables = P6LogQuery.parseCSVList("");
            query = "select 'w' from stmt_test";
            statement.executeQuery(query);
            assertTrue(P6LogQuery.getLastEntry().indexOf(query) != -1);
            
            // now match should still fail because table is excluded
            P6SpyOptions.setFilter(true);
            P6LogQuery.includeTables = P6LogQuery.parseCSVList("");
            P6LogQuery.excludeTables = P6LogQuery.parseCSVList("stmt_test");
            query = "select 'x' from stmt_test";
            statement.executeQuery(query);
            assertTrue(P6LogQuery.getLastEntry().indexOf(query) == -1);
            
            // use gnu regex
            P6SpyOptions.setStringmatcher("com.p6spy.engine.spy.GnuRegexMatcher");
            tryRegEx();
            
            P6SpyOptions.setStringmatcher("com.p6spy.engine.spy.JakartaRegexMatcher");
            tryRegEx();
            
        } catch (Exception e) {
            fail(e.getMessage()+getStackTrace(e));
        }
    }
    
    protected void tryRegEx() throws Exception {
        Statement statement = connection.createStatement();
        
        // should match (basic)
        P6SpyOptions.setFilter(true);
        P6LogQuery.excludeTables = P6LogQuery.parseCSVList("");
        P6LogQuery.includeTables = P6LogQuery.parseCSVList("");
        String query = "select 'y' from stmt_test";
        statement.executeQuery(query);
        assertTrue(P6LogQuery.getLastEntry().indexOf(query) != -1);
        
        // now match should match (test regex)
        P6SpyOptions.setFilter(true);
        P6LogQuery.includeTables = P6LogQuery.parseCSVList("");
        P6LogQuery.excludeTables = P6LogQuery.parseCSVList("[a-z]tmt_test");
        query = "select 'x' from stmt_test";
        statement.executeQuery(query);
        assertTrue(P6LogQuery.getLastEntry().indexOf(query) == -1);
        
        // now match should fail (test regex again)
        P6SpyOptions.setFilter(true);
        P6LogQuery.includeTables = P6LogQuery.parseCSVList("");
        P6LogQuery.excludeTables = P6LogQuery.parseCSVList("[0-9]tmt_test");
        query = "select 'z' from stmt_test";
        statement.executeQuery(query);
        assertTrue(P6LogQuery.getLastEntry().indexOf(query) != -1);
    }
    
    public void testCategories() {
        
        try {
            Statement statement = connection.createStatement();
            
            // test rollback logging
            P6SpyOptions.setFilter(true);
            P6LogQuery.excludeTables = P6LogQuery.parseCSVList("");
            P6LogQuery.includeTables = P6LogQuery.parseCSVList("");
            P6LogQuery.excludeCategories = P6LogQuery.parseCSVList("");
            P6LogQuery.includeCategories = P6LogQuery.parseCSVList("");
            String query = "select 'y' from stmt_test";
            statement.executeQuery(query);
            assertTrue(P6LogQuery.getLastEntry().indexOf(query) != -1);
            connection.rollback();
            assertTrue(P6LogQuery.getLastEntry().indexOf("rollback") != -1);
            
            // test commit logging
            P6SpyOptions.setFilter(true);
            P6LogQuery.excludeTables = P6LogQuery.parseCSVList("");
            P6LogQuery.includeTables = P6LogQuery.parseCSVList("");
            P6LogQuery.excludeCategories = P6LogQuery.parseCSVList("");
            P6LogQuery.includeCategories = P6LogQuery.parseCSVList("");
            query = "select 'y' from stmt_test";
            statement.executeQuery(query);
            assertTrue(P6LogQuery.getLastEntry().indexOf(query) != -1);
            connection.commit();
            assertTrue(P6LogQuery.getLastEntry().indexOf("commit") != -1);
            
            // test debug logging
            P6SpyOptions.setFilter(true);
            P6LogQuery.excludeTables = P6LogQuery.parseCSVList("stmt_test");
            P6LogQuery.includeTables = P6LogQuery.parseCSVList("");
            P6LogQuery.excludeCategories = P6LogQuery.parseCSVList("");
            P6LogQuery.includeCategories = P6LogQuery.parseCSVList("debug,info");
            query = "select 'y' from stmt_test";
            statement.executeQuery(query);
            assertTrue(P6LogQuery.getLastEntry().indexOf("intentionally") != -1);
            
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    
    public void testStacktrace() {
        try {
            // get a statement
            Statement statement = connection.createStatement();
            P6SpyOptions.setStackTrace(true);
            
            // perform a query & make sure we get the stack trace
            P6SpyOptions.setFilter(true);
            P6LogQuery.excludeTables = P6LogQuery.parseCSVList("");
            P6LogQuery.includeTables = P6LogQuery.parseCSVList("");
            String query = "select 'y' from stmt_test";
            statement.executeQuery(query);
            assertTrue(P6LogQuery.getLastEntry().indexOf(query) != -1);
            assertTrue(P6LogQuery.getLastStack().indexOf("Stack") != -1);
            
            // filter on stack trace that will not match
            P6LogQuery.lastStack = null;
            P6SpyOptions.setStackTraceClass("com.dont.match");
            P6SpyOptions.setFilter(true);
            P6LogQuery.excludeTables = P6LogQuery.parseCSVList("");
            P6LogQuery.includeTables = P6LogQuery.parseCSVList("");
            query = "select 'a' from stmt_test";
            statement.executeQuery(query);
            // this will actually match - just the stack trace wont fire
            assertTrue(P6LogQuery.getLastEntry().indexOf(query) != -1);
            assertNull(P6LogQuery.getLastStack());
            
            P6LogQuery.lastStack = null;
            P6SpyOptions.setStackTraceClass("com.p6spy.engine.spy");
            P6SpyOptions.setFilter(true);
            P6LogQuery.excludeTables = P6LogQuery.parseCSVList("");
            P6LogQuery.includeTables = P6LogQuery.parseCSVList("");
            query = "select 'b' from stmt_test";
            statement.executeQuery(query);
            assertTrue(P6LogQuery.getLastEntry().indexOf(query) != -1);
            assertTrue(P6LogQuery.getLastStack().indexOf("Stack") != -1);
            
        } catch (Exception e) {
            fail(e.getMessage()+getStackTrace(e));
        }
    }
    
    public void testReload() {
        try {
            Statement statement = connection.createStatement();
            
            HashMap tp = new HashMap();
            tp.put("realdriver","oracle.jdbc.driver.OracleDriver");
            tp.put("filter","false");
            tp.put("include","");
            tp.put("exclude","");
            tp.put("trace","true");
            tp.put("autoflush","true");
            tp.put("logfile","spy.log");
            tp.put("append","true");
            tp.put("dateformat","");
            tp.put("includecategories","");
            tp.put("excludecategories","debug,result,batch");
            tp.put("stringmatcher","");
            tp.put("stacktrace","false");
            tp.put("stacktraceclass","");
            tp.put("reloadproperties","true");
            tp.put("reloadpropertiesinterval","1");
            
            writeProperty(tp);
            
            P6SpyOptions.SPY_PROPERTIES_FILE = "reloadtest.properties";
            P6SpyOptions.initMethod();
            P6SpyDriver.initMethod();
            P6LogQuery.initMethod();
            
            Thread.sleep(2000);
            
            String query = "select 'b' from stmt_test";
            statement.executeQuery(query);
            
            assertEquals(P6SpyOptions.getFilter(), false);
            
            tp.put("filter","true");
            tp.put("include","bob");
            tp.put("exclude","barb");
            tp.put("trace","false");
            tp.put("autoflush","false");
            tp.put("logfile","reload.log");
            tp.put("append","false");
            tp.put("dateformat","dd-MM-yyyy");
            tp.put("includecategories","debug");
            tp.put("excludecategories","result,batch");
            tp.put("stringmatcher","com.p6spy.engine.spy.JakartaRegexMatcher");
            tp.put("stacktrace","true");
            tp.put("stacktraceclass","dummy");
            tp.put("reloadproperties","true");
            tp.put("reloadpropertiesinterval","1");
            writeProperty(tp);
            Thread.sleep(2000);
            query = "select 'c' from stmt_test";
            statement.executeQuery(query);            
            assertEquals(P6SpyOptions.getFilter(), true);
            assertEquals(P6SpyOptions.getInclude(), "bob");
            assertEquals(P6SpyOptions.getExclude(), "barb");
            assertEquals(P6SpyOptions.getTrace(), false);
            assertEquals(P6SpyOptions.getAutoflush(), false);
            assertEquals(P6SpyOptions.getLogfile(), "reload.log");
            assertEquals(P6SpyOptions.getAppend(), false);
            assertEquals(P6SpyOptions.getDateformat(), "dd-MM-yyyy");
            assertEquals(P6SpyOptions.getIncludecategories(), "debug");
            assertEquals(P6SpyOptions.getExcludecategories(), "result,batch");
            assertEquals(P6SpyOptions.getStringmatcher(), "com.p6spy.engine.spy.JakartaRegexMatcher");
            assertEquals(P6SpyOptions.getStringMatcherEngine().getClass().getName(), "com.p6spy.engine.spy.JakartaRegexMatcher");
            assertEquals(P6SpyOptions.getStackTrace(), true);
            assertEquals(P6SpyOptions.getStackTraceClass(), "dummy");
            assertEquals(P6SpyOptions.getReloadProperties(), true);
            assertEquals(P6SpyOptions.getReloadPropertiesInterval(), 1);
            
            tp.put("realdriver","oracle.jdbc.driver.OracleDriver");
            tp.put("filter","false");
            tp.put("include","");
            tp.put("exclude","");
            tp.put("trace","true");
            tp.put("autoflush","true");
            tp.put("logfile","spy.log");
            tp.put("append","true");
            tp.put("dateformat","");
            tp.put("includecategories","");
            tp.put("excludecategories","debug,result,batch");
            tp.put("stringmatcher","");
            tp.put("stacktrace","false");
            tp.put("stacktraceclass","");
            tp.put("reloadproperties","true");
            tp.put("reloadpropertiesinterval","1");
            
        } catch (Exception e) {
            fail(e.getMessage()+getStackTrace(e));
        }
    }
    
    protected void writeProperty(HashMap props) {
        try {
            File reload = new File("reloadtest.properties");
            reload.delete();
            
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(reload)));
            
            Iterator i = props.keySet().iterator();
            while (i.hasNext()) {
                String key = (String)i.next();
                String value = (String)props.get(key);
                out.println(key+"="+value);
            }
            
            out.close();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    
    protected void tearDown() {
        try {
            Statement statement = connection.createStatement();
            drop(statement);
            statement.close();
        }  catch (Exception e) {
            fail(e.getMessage());
        }
        super.tearDown();
    }
    
    protected void drop(Statement statement) {
        dropStatement("drop table stmt_test", statement);
    }
    
    protected void dropStatement(String sql, Statement statement) {
        try {
            statement.execute(sql);
        } catch (Exception e) {
            // we don't really care about cleanup failing
        }
    }
}
