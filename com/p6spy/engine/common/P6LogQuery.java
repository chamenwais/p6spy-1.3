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
 * Description: Some Utility routines ...
 *
 * $Author$
 * $Revision$
 * $Date$
 *
 * $Id$
 * $Log$
 * Revision 1.1  2002/05/24 07:32:01  jeffgoke
 * version 1 rewrite
 *
 * Revision 1.11  2002/05/18 06:39:52  jeffgoke
 * Peter Laird added Outage detection.  Added junit tests for outage detection.
 * Fixed multi-driver tests.
 *
 * Revision 1.10  2002/05/16 04:58:40  jeffgoke
 * Viktor Szathmary added multi-driver support.
 * Rewrote P6SpyOptions to be easier to manage.
 * Fixed several bugs.
 *
 * Revision 1.9  2002/05/05 00:43:00  jeffgoke
 * Added Philip's reload code.
 *
 * Revision 1.8  2002/04/25 06:51:28  jeffgoke
 * Philip Ogren of BEA contributed installation instructions for BEA WebLogic Portal and Server
 * Jakarta RegEx support (contributed by Philip Ogren)
 * Ability to print stack trace of logged statements. This is very useful to understand where a logged query is being executed in the application (contributed by Philip Ogren)
 * Simplified table monitoring property file option (contributed by Philip Ogren)
 * Updated the RegEx documentation
 *
 * Revision 1.7  2002/04/22 02:57:45  jeffgoke
 * fixed bug in log
 *
 * Revision 1.6  2002/04/22 02:26:06  jeffgoke
 * Simon Sadedin added timing information.  Added Junit tests.
 *
 * Revision 1.5  2002/04/21 06:15:34  jeffgoke
 * added test cases, fixed batch bugs
 *
 * Revision 1.4  2002/04/15 05:13:32  jeffgoke
 * Simon Sadedin added timing support.  Fixed bug where batch execute was not
 * getting logged.  Added result set timing.  Updated the log format to include
 * categories, and updated options to control the categories.  Updated
 * documentation.
 *
 * Revision 1.3  2002/04/10 06:49:26  jeffgoke
 * added more debug information and a new property for setting the log's date format
 *
 * Revision 1.2  2002/04/07 20:43:59  jeffgoke
 * fixed bug that caused null connection to return an empty connection instead of null.
 * added an option allowing the user to truncate.
 * added a release target to the build to create the release files.
 *
 * Revision 1.1.1.1  2002/04/07 04:52:25  jeffgoke
 * no message
 *
 * Revision 1.3  2001-08-05 09:16:03-05  andy
 * version on the website
 *
 * Revision 1.2  2001-07-30 23:38:14-05  andy
 * trim the table names in include/exclude
 *
 * Revision 1.1  2001-07-30 23:03:31-05  andy
 * <>
 *
 * Revision 1.0  2001-07-30 17:46:23-05  andy
 * Initial revision
 *
 */

package com.p6spy.engine.common;

import java.sql.*;
import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;

public class P6LogQuery {
    protected static PrintStream qlog;
    protected static String[] includeTables;
    protected static String[] excludeTables;
    protected static String[] includeCategories;
    protected static String[] excludeCategories;
    protected static String lastEntry;
    protected static String lastStack;
    
    static {
        initMethod();
    }
    
    public synchronized static void initMethod() {
        String log = P6SpyOptions.getLogfile();
        if (log == null || log.equals("stdout")) {
            qlog = System.out;
        } else {
            qlog = logPrintStream(log);
        }
        if (P6SpyOptions.getFilter()) {
            includeTables = parseCSVList(P6SpyOptions.getInclude());
            excludeTables = parseCSVList(P6SpyOptions.getExclude());
        }
        includeCategories = parseCSVList(P6SpyOptions.getIncludecategories());
        excludeCategories = parseCSVList(P6SpyOptions.getExcludecategories());
    }
    
    static PrintStream logPrintStream(String file) {
        PrintStream ps = null;
        try {
            String path = P6Util.classPathFile(file);
            file = (path == null) ? file : path;
            ps = P6Util.getPrintStream(file, P6SpyOptions.getAppend());
        } catch (IOException io) {
            P6Util.warn("Error opening " + file + ", " + io.getMessage());
            ps = null;
        }
        
        return ps;
    }
    
    static String[] parseCSVList(String csvList) {
        String array[] = null;
        if (csvList != null) {
            StringTokenizer tok = new StringTokenizer(csvList, ",");
            String item;
            ArrayList list = new ArrayList();
            while (tok.hasMoreTokens()) {
                item = tok.nextToken().toLowerCase().trim();
                if (item != "") {
                    list.add(item.toLowerCase().trim());
                }
            }
            
            int max = list.size();
            Iterator it = list.iterator();
            array = new String[max];
            int i;
            for (i = 0; i < max; i++) {
                array[i] = (String) it.next();
            }
        }
        
        return array;
    }
    
    static protected void doLog(long elapsed, String category, String prepared, String sql) {
        doLog(-1, elapsed, category, prepared, sql);
    }
    
    // this is an internal method called by logElapsed
    static protected void doLogElapsed(int connectionId, long startTime, long endTime, String category, String prepared, String sql) {
        doLog(connectionId, (endTime - startTime), category, prepared, sql);
    }
    
    // this is an internal procedure used to actually write the log information
    static protected synchronized void doLog(int connectionId, long elapsed, String category, String prepared, String sql) {
        java.util.Date now = P6Util.timeNow();
        SimpleDateFormat sdf = P6SpyOptions.getDateformatter();
        String logEntry;
        if (sdf == null) {
            logEntry = Long.toString(now.getTime());
        } else {
            logEntry = sdf.format(new java.util.Date(now.getTime())).trim();
        }
        logEntry += "|"+elapsed+"|"+(connectionId==-1 ? "" : String.valueOf(connectionId))+"|"+category+"|"+prepared+"|"+sql;
        qlog.println(logEntry);
        boolean stackTrace = P6SpyOptions.getStackTrace();
        String stackTraceClass = P6SpyOptions.getStackTraceClass();
        if(stackTrace) {
            if(stackTraceClass == null) {
                Exception e = new Exception();
                e.printStackTrace(qlog);
            }
            else {
                ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                PrintStream printStream = new PrintStream(byteStream);
                Exception e = new Exception("P6Spy Stack Trace Log");
                e.printStackTrace(printStream);
                String stack = byteStream.toString();
                if(stack.indexOf(stackTraceClass) != -1) {
                    lastStack = stack;
                    e.printStackTrace(qlog);
                }
            }
        }
        
        lastEntry = logEntry;
        
    }
    
    static boolean isLoggable(String sql) {
        return(P6SpyOptions.getFilter() == false || queryOk(sql));
    }
    
    static boolean isCategoryOk(String category) {
        return (includeCategories == null || includeCategories.length == 0 || foundCategory(category,includeCategories)) && !foundCategory(category,excludeCategories);
    }
    
    static boolean foundCategory(String category, String categories[]) {
        if (categories != null) {
            for (int i = 0; i < categories.length; i++) {
                if (category.equals(categories[i])) {
                    return true;
                }
            }
        }
        return false;
    }
    
    static boolean queryOk(String sql) {
        if(P6SpyOptions.getStringMatcherEngine() != null && P6SpyOptions.getSQLExpression() != null) {
            return sqlOk(sql);
        }
        else
            return ((includeTables == null || includeTables.length == 0 || foundTable(sql, includeTables))) && !foundTable(sql, excludeTables);
    }
    
    static boolean sqlOk(String sql) {
        String sqlexpression = P6SpyOptions.getSQLExpression();
        try {
            return P6SpyOptions.getStringMatcherEngine().match(sqlexpression, sql);
        }
        catch (MatchException e) {
            P6Util.warn("Exception during matching sqlexpression [" + sqlexpression + "] to sql [" + sql + "]: ");
            return false;
        }
        
    }
    static boolean foundTable(String sql, String tables[]) {
        sql = sql.toLowerCase();
        boolean ok = false;
        int i;
        if (tables != null) {
            for (i = 0; !ok && i < tables.length; i++) {
                ok = tableOk(sql, tables[i]);
            }
        }
        
        return ok;
    }
    
    static boolean tableOk(String sql, String table) {
        try {
            return P6SpyOptions.getStringMatcherEngine().match(table, sql);
        }
        catch (MatchException e) {
            P6Util.warn("Exception during matching expression [" + table + "] to sql [" + sql + "]: ");
            return false;
        }
    }
    
    // ----------------------------------------------------------------------------------------------------------
    // public accessor methods for logging and viewing query data
    // ----------------------------------------------------------------------------------------------------------
    
    static public void clearLastStack() {
        lastStack = null;
    }
    
    static public String getLastEntry() {
        return lastEntry;
    }
    
    static public String getLastStack() {
        return lastStack;
    }
    
    static public String[] getIncludeTables() {
        return includeTables;
    }
    
    static public String[] getExcludeTables() {
        return excludeTables;
    }
    
    static public void setIncludeTables(String _includeTables) {
        P6LogQuery.includeTables = P6LogQuery.parseCSVList(_includeTables);
    }
    
    static public void setExcludeTables(String _excludeTables) {
        P6LogQuery.excludeTables = P6LogQuery.parseCSVList(_excludeTables);
    }
    
    static public void setIncludeCategories(String _includeCategories) {
        P6LogQuery.includeCategories = P6LogQuery.parseCSVList(_includeCategories);
    }
    
    static public void setExcludeCategories(String _excludeCategories) {
        P6LogQuery.excludeCategories = P6LogQuery.parseCSVList(_excludeCategories);
    }
    
    // this a way for an external to dump an unrestricted line of text into the log
    // useful for the JSP demarcation tool
    static public void logText(String text) {
        qlog.println(text);
    }
    
    static public void logElapsed(int connectionId, long startTime, String category, String prepared, String sql) {
        logElapsed(connectionId, startTime, System.currentTimeMillis(), category, prepared, sql);
    }
    
    static public void logElapsed(int connectionId, long startTime, long endTime, String category, String prepared, String sql) {
        if (qlog != null && isLoggable(sql) && isCategoryOk(category)) {
            doLogElapsed(connectionId, startTime, endTime, category, prepared, sql);
        } else if (isCategoryOk("debug")) {
            logDebug("P6Spy intentionally did not log category: "+category+", statement: "+sql+"  Reason: Qlog="+qlog+", isLoggable="+isLoggable(sql)+", isCategoryOk="+isCategoryOk(category));
        }
    }
    
    static public void logInfo(String sql) {
        if (qlog != null && isCategoryOk("info")) {
            doLog(-1, "info", "", sql);
        }
    }
    
    static public void logDebug(String sql) {
        if (isCategoryOk("debug")) {
            if (qlog != null) {
                doLog(-1, "debug", "", sql);
            } else {
                System.err.println(sql);
            }
        }
    }
    
}
