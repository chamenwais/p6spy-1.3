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
 * final version on the website
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

package com.p6spy.engine.spy;

import java.sql.*;
import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;

public class P6LogQuery {
    private static PrintStream qlog;
    private static String[] includeTables;
    private static String[] excludeTables;
    private static String[] includeCategories;
    private static String[] excludeCategories;
    
    static {
        if (P6SpyOptions.getTrace()) {
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
    
    static final String[] parseCSVList(String csvList) {
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
    
    static final void logDebug(String sql) {
        if (qlog != null && isCategoryOk("debug")) {
            doLog(-1, "debug", "", sql);
        }
    }
    
    static final synchronized void doLog(long elapsed, String category, String prepared, String sql) {
        java.util.Date now = P6Util.timeNow();
        SimpleDateFormat sdf = P6SpyOptions.getDateformatter();
        if (sdf == null) {
            qlog.print(now.getTime());
        } else {
            qlog.print(sdf.format(new java.util.Date(now.getTime())).trim());
        }
        qlog.print("|"+elapsed+"|"+category+"|"+prepared+"|");
        qlog.println(sql);
    }
    
    static final boolean isLoggable(String sql) {
        return(P6SpyOptions.getFilter() == false || queryOk(sql.toLowerCase()));
    }
    
    static final boolean isCategoryOk(String category) {
        return (includeCategories == null || includeCategories.length == 0 || foundCategory(category,includeCategories)) && !foundCategory(category,excludeCategories);
    }
    
    static final boolean foundCategory(String category, String categories[]) {
        if (categories != null) {
            for (int i = 0; i < categories.length; i++) {
                if (category.equals(categories[i])) {
                    return true;
                }
            }
        }
        return false;
    }
    
    static final boolean queryOk(String sql) {
        return (includeTables == null || includeTables.length == 0 || foundTable(sql, includeTables)) && !foundTable(sql, excludeTables);
    }
    
    static final boolean foundTable(String sql, String tables[]) {
        boolean ok = false;
        int i;
        if (tables != null) {
            for (i = 0; !ok && i < tables.length; i++) {
                ok = tableOk(sql, tables[i]);
            }
        }
        
        return ok;
    }
    
    static final boolean tableOk(String sql, String table) {
        return (sql.indexOf(table) >= 0);
    }
    
    static final void logElapsed(long startTime, String category, String prepared, String sql) {
        logElapsed(startTime,System.currentTimeMillis(), category, prepared, sql);
    }
    
    static final void logElapsed(long startTime, long endTime, String category, String prepared, String sql) {
        if (qlog != null && isLoggable(sql) && isCategoryOk(category)) {
            doLogElapsed(startTime, endTime, category, prepared, sql);
        }
    }
    
    static final synchronized void doLogElapsed(long startTime, long endTime, String category, String prepared, String sql) {
        doLog((endTime - startTime), category, prepared, sql);
    }
}
