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

// Description: Class file for options

package com.p6spy.engine.spy;

import java.util.*;
import java.text.SimpleDateFormat;
import java.io.File;

public class P6SpyOptions   {
    
    public static String SPY_PROPERTIES_FILE = "spy.properties";
    protected static long lastCheck = 0;
    
    static {initMethod();}
    
    public P6SpyOptions() {}
    
    private static boolean autoflush;
    private static String exclude;
    private static boolean filter;
    private static boolean help;
    private static String include;
    private static String logfile;
    private static String realdriver;
    private static String spydriver;
    private static boolean trace;
    private static boolean append;
    private static String properties;
    private static String dateformat;
    private static SimpleDateFormat dateformatter;
    private static String includecategories;
    private static String excludecategories;
    private static String stringmatcher;
    private static StringMatcher stringMatcherEngine;
    private static String sqlExpression;
    private static boolean stackTrace;
    private static String stackTraceClass;
    private static boolean reloadProperties;
    private static long reloadPropertiesInterval;
    private static long reloadMs;
    
    static String propertiesPath;
    static long propertiesLastModified = -1;
    
    
    public static void setAutoflush(boolean _autoflush) {
        autoflush = _autoflush;
    }
    public static boolean getAutoflush() {
        return autoflush;
    }
    public static void setExclude(String _exclude) {
        exclude = _exclude;
    }
    public static String getExclude() {
        return exclude;
    }
    public static void setExcludecategories(String _excludecategories) {
        excludecategories = _excludecategories;
    }
    public static String getExcludecategories() {
        return excludecategories;
    }
    public static void setFilter(boolean _filter) {
        filter = _filter;
    }
    public static boolean getFilter() {
        return filter;
    }
    public static void setHelp(boolean _help) {
        help = _help;
    }
    public static boolean getHelp() {
        return help;
    }
    public static void setInclude(String _include) {
        include = _include;
    }
    public static String getInclude() {
        return include;
    }
    public static void setIncludecategories(String _includecategories) {
        includecategories = _includecategories;
    }
    public static String getIncludecategories() {
        return includecategories;
    }
    public static void setLogfile(String _logfile) {
        logfile = _logfile;
    }
    public static String getLogfile() {
        return logfile;
    }
    public static void setRealdriver(String _realdriver) {
        realdriver = _realdriver;
    }
    public static String getRealdriver() {
        return realdriver;
    }
    public static void setAppend(boolean _append) {
        append = _append;
    }
    public static boolean getAppend() {
        return append;
    }
    public static void setSpydriver(String _spydriver) {
        spydriver = _spydriver;
    }
    public static String getSpydriver() {
        return spydriver;
    }
    public static void setTrace(boolean _trace) {
        trace = _trace;
    }
    // since getTrace is called almost everywhere, this is the
    // best property to engage reloading
    public static boolean getTrace() {
        checkReload();
        return trace;
    }
    public static void setProperties(String _properties) {
        properties = _properties;
    }
    public static String getProperties() {
        return properties;
    }
    public static void setDateformat(String _dateformat) {
        dateformat = _dateformat;
        if (_dateformat == null || _dateformat.equals("")) {
            dateformatter = null;
        } else {
            dateformatter = new SimpleDateFormat(_dateformat);
        }
    }
    public static String getDateformat() {
        return dateformat;
    }
    public static SimpleDateFormat getDateformatter() {
        return dateformatter;
    }
    public static void setStringmatcher(String _stringmatcher) {
        stringmatcher = _stringmatcher;
        if(stringmatcher == null || stringmatcher.equals("")) {
            stringmatcher = "com.p6spy.engine.spy.SubstringMatcher";
        }
        try {
            stringMatcherEngine = (StringMatcher)Class.forName(stringmatcher).newInstance();
        } catch (InstantiationException e) {
            P6Util.warn("Could not instantiate string matcher class: " + stringmatcher);
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            P6Util.warn("Could not instantiate string matcher class: " + stringmatcher);
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            P6Util.warn("Could not instantiate string matcher class: " + stringmatcher);
            e.printStackTrace();
        }
    }
    public static String getStringmatcher() {
        return stringmatcher;
    }
    public static StringMatcher getStringMatcherEngine() {
        return stringMatcherEngine;
    }
    
    public static boolean getStackTrace(){
        return stackTrace;
    }
    public static void setStackTrace(boolean stacktrace) {
        stackTrace = stacktrace;
    }
    public static String getStackTraceClass(){
        return stackTraceClass;
    }
    public static void setStackTraceClass(String stacktraceclass) {
        stackTraceClass = stacktraceclass;
    }
    
    public static String getSQLExpression(){
        return sqlExpression;
    }
    public static void setSQLExpression(String sqlexpression) {
        if (sqlexpression != null && sqlexpression.equals("")) {
            sqlexpression = null;
        }        
        sqlExpression = sqlexpression;
    }
    
    public static boolean getReloadProperties() {
        return reloadProperties;
    }
    
    public static void setReloadProperties(boolean reloadproperties) {
        reloadProperties = reloadproperties;
    }
    
    public static long getReloadPropertiesInterval() {
        return reloadPropertiesInterval;
    }
    
    public static void setReloadPropertiesInterval(String reloadpropertiesinterval) {
        try {
            reloadPropertiesInterval = Long.parseLong(reloadpropertiesinterval);
        }
        catch(NumberFormatException nfe) {
            P6Util.warn("NumberFormatException in P6SpyOptions.setReloadPropertiesInterval() "+
            "reloadpropertiesinterval=\""+reloadpropertiesinterval+"\"");
        }
        reloadMs = reloadPropertiesInterval * 1000l;
    }
    
    public static void setReloadPropertiesInterval(long reloadpropertiesinterval) {
        reloadPropertiesInterval = reloadpropertiesinterval;
    }
    
    public static void testP6SpyOptions() {
        new P6SpyOptions();
        help();
        help("coMMand-line");
        set(null,null);
        set("bad-flag-action",null);
        get(null);
        get("bad-flag-action");
        getOptions();
        getKeysMap();
        getValuesMap();
        setAutoflush(getAutoflush());
        setExclude(getExclude());
        setExcludecategories(getExcludecategories());
        setFilter(getFilter());
        setHelp(getHelp());
        setInclude(getInclude());
        setIncludecategories(getIncludecategories());
        setLogfile(getLogfile());
        setRealdriver(getRealdriver());
        setSpydriver(getSpydriver());
        setAppend(getAppend());
        setTrace(getTrace());
        setProperties(getProperties());
        setDateformat(getDateformat());
        setStringmatcher(getStringmatcher());
        setSQLExpression(getSQLExpression());
        setStackTrace(getStackTrace());
        setStackTraceClass(getStackTraceClass());
        setReloadProperties(getReloadProperties());
        setReloadPropertiesInterval(getReloadPropertiesInterval());
    }
    
    public static void help() {
        System.out.println("\nEngine:");
        System.out.println("    autoflush [true]                          - turn on autoflush");
        System.out.println("    exclude []                                - comma separated list of tables to exclude");
        System.out.println("    excludecategories []                      - comma separated list of categories to exclude");
        System.out.println("    filter [false]                            - turn on filtering");
        System.out.println("    help [false]                              - print help message or not");
        System.out.println("    include []                                - comma separated list of tables to include");
        System.out.println("    sqlexpression []                          - if the sqlexpression matches the sql statement, it is logged");
        System.out.println("    includecategories []                      - comma separated list of categories to include");
        System.out.println("    logfile [spy.log]                         - name of logfile if trace is on");
        System.out.println("    realdriver []                             - name of real jdbc driver to load");
        System.out.println("    spydriver [com.p6.engine.spy.P6SpyDriver] - name of SPY Driver");
        System.out.println("    trace [false]                             - turn on tracing");
        System.out.println("    append [true]                             - append to the P6Spy log file (false = truncate)");
        System.out.println("    dateformat []                             - simple date format for log file");
        System.out.println("    stringmatcher []                          - regex engine to use");
        System.out.println("    stacktrace []                             - prints out stack trace for all log statemnts if true");
        System.out.println("    stacktraceclass []                        - filters stack traces printed out to those specified");
        System.out.println("    reloadproperties []                       - turn on reload of properties");
        System.out.println("    reloadpropertiesinterval []               - set a timed interval for reloading properties");
        System.out.println("\nGlobal:");
        System.out.println("    properties ["+SPY_PROPERTIES_FILE+"]               - name of file that stores the properties info");
    }
    
    public static void help(String category) {
        if (category.equalsIgnoreCase("Engine")) {            System.out.println("\nEngine:");            System.out.println("    autoflush [true]                          - turn on autoflush");        }
        if (category.equalsIgnoreCase("Engine")) {            System.out.println("    excludecategories []                      - comma separated list of categories to exclude");        }
        if (category.equalsIgnoreCase("Engine")) {            System.out.println("    exclude []                                - comma separated list of tables to exclude");        }
        if (category.equalsIgnoreCase("Engine")) {            System.out.println("    filter [false]                            - turn on filtering");        }
        if (category.equalsIgnoreCase("Engine")) {            System.out.println("    help [false]                              - print help message or not");        }
        if (category.equalsIgnoreCase("Engine")) {            System.out.println("    include []                                - comma separated list of tables to include");        }
        if (category.equalsIgnoreCase("Engine")) {            System.out.println("    sqlexpression []                          - if the sqlexpression matches the sql statement, it is logged");        }
        if (category.equalsIgnoreCase("Engine")) {            System.out.println("    logfile [spy.log]                         - name of logfile if trace is on");        }
        if (category.equalsIgnoreCase("Engine")) {            System.out.println("    realdriver []                             - name of real jdbc driver to load");        }
        if (category.equalsIgnoreCase("Engine")) {            System.out.println("    spydriver [com.p6spy.engine.spy.P6SpyDriver] - name of SPY Driver");        }
        if (category.equalsIgnoreCase("Engine")) {            System.out.println("    trace [false]                             - turn on tracing");        }
        if (category.equalsIgnoreCase("Engine")) {            System.out.println("    append [true]                             - append to the P6Spy log file (false = truncate)");        }
        if (category.equalsIgnoreCase("Engine")) {            System.out.println("    dateformat []                             - simple date format for log file");        }
        if (category.equalsIgnoreCase("Engine")) {            System.out.println("    stringmatcher []                          - string matcher engine to use");        }
        if (category.equalsIgnoreCase("Engine")) {            System.out.println("    stacktrace []                             - prints out stack trace for all log statemnts if true");        }
        if (category.equalsIgnoreCase("Engine")) {            System.out.println("    stacktraceclass []                        - filters stack traces printed out to those specified");        }
        if (category.equalsIgnoreCase("Engine")) {            System.out.println("    reloadproperties []                       - turn on reload of properties");        }
        if (category.equalsIgnoreCase("Engine")) {            System.out.println("    reloadpropertiesinterval []               - set a timed interval for reloading properties");        }
        if (category.equalsIgnoreCase("Global")) {            System.out.println("\nGlobal:");            System.out.println("    properties ["+SPY_PROPERTIES_FILE+"]               - name of file that stores the properties info");        }
    }
    
    public static void reloadProperties() {
        if(reloadProperties && propertiesPath != null) {
            P6LogQuery.logDebug("checking property file to see if it needs to be reloaded");
            File propertiesFile = new File(propertiesPath);
            if(propertiesFile.exists()) {
                long lastModified = propertiesFile.lastModified();
                if(lastModified != propertiesLastModified) {
                    setValues(P6Util.loadProperties(SPY_PROPERTIES_FILE));
                    propertiesLastModified = lastModified;
                    
                    // finally reinitialize the drivers
                    P6SpyDriver.initMethod();
                    P6LogQuery.initMethod();
                    
                    P6LogQuery.logInfo("reloadProperties() successful");
                }
            }
        }
    }
    
    public static void initMethod() {
        Properties props  = P6Util.loadProperties(SPY_PROPERTIES_FILE);
        setValues(props);
        
        propertiesPath = P6Util.classPathFile(SPY_PROPERTIES_FILE);
        if(propertiesPath != null) {
            File propertiesFile = new File(propertiesPath);
            if(propertiesFile.exists()) {
                propertiesLastModified = propertiesFile.lastModified();
            }
        }
    }
    
    public static void setValues(Properties props) {
        String value;
        value = props.getProperty("autoflush");
        if (value == null) value = "true";
        setAutoflush(P6Util.isTrue(value));
        value = props.getProperty("exclude");
        setExclude(value);
        value = props.getProperty("excludecategories");
        setExcludecategories(value);
        value = props.getProperty("filter");
        if (value == null) value = "false";
        setFilter(P6Util.isTrue(value));
        value = props.getProperty("help");
        if (value == null) value = "false";
        setHelp(P6Util.isTrue(value));
        value = props.getProperty("include");
        setInclude(value);
        value = props.getProperty("includecategories");
        setIncludecategories(value);
        value = props.getProperty("logfile");
        if (value == null) value = "spy.log";
        setLogfile(value);
        value = props.getProperty("realdriver");
        setRealdriver(value);
        value = props.getProperty("spydriver");
        if (value == null) value = "com.p6spy.engine.spy.P6SpyDriver";
        setSpydriver(value);
        value = props.getProperty("trace");
        if (value == null) value = "false";
        setTrace(P6Util.isTrue(value));
        value = props.getProperty("properties");
        if (value == null) value = "spy.properties";
        setProperties(value);
        value = props.getProperty("append");
        if (value == null) value = "true";
        setAppend(P6Util.isTrue(value));
        value = props.getProperty("dateformat");
        setDateformat(value);
        value = props.getProperty("stringmatcher");
        setStringmatcher(value);
        value = props.getProperty("sqlexpression");
        setSQLExpression(value);
        value = props.getProperty("stacktrace");
        if(value == null) value = "false";
        setStackTrace(P6Util.isTrue(value));
        value = props.getProperty("stacktraceclass");
        setStackTraceClass(value);
        value = props.getProperty("reloadproperties");
        if(value == null) value = "false";
        setReloadProperties(P6Util.isTrue(value));
        value = props.getProperty("reloadpropertiesinterval");
        if(value == null) value = "-1";
        setReloadPropertiesInterval(value);
    }
    
    public static boolean set(String name, String value) {
        boolean ret = true;
        String lc = name == null ? null : name.toLowerCase();
        if (name == null) ;// do nothing
        else if (lc.equals("autoflush")) setAutoflush(P6Util.isTrue(value));
        else if (lc.equals("exclude")) setExclude(value);
        else if (lc.equals("excludecategories")) setExcludecategories(value);
        else if (lc.equals("filter")) setFilter(P6Util.isTrue(value));
        else if (lc.equals("help")) setHelp(P6Util.isTrue(value));
        else if (lc.equals("include")) setInclude(value);
        else if (lc.equals("includecategories")) setIncludecategories(value);
        else if (lc.equals("logfile")) setLogfile(value);
        else if (lc.equals("realdriver")) setRealdriver(value);
        else if (lc.equals("spydriver")) setSpydriver(value);
        else if (lc.equals("append")) setAppend(P6Util.isTrue(value));
        else if (lc.equals("trace")) setTrace(P6Util.isTrue(value));
        else if (lc.equals("properties")) setProperties(value);
        else if (lc.equals("properties")) setProperties(value);
        else if (lc.equals("dateformat")) setDateformat(value);
        else if (lc.equals("stringmatcher")) setStringmatcher(value);
        else if (lc.equals("sqlexpression")) setSQLExpression(value);
        else if (lc.equals("stacktrace")) setStackTrace(P6Util.isTrue(value));
        else if (lc.equals("stacktraceclass")) setStackTraceClass(value);
        else if (lc.equals("reloadproperties")) setReloadProperties(P6Util.isTrue(value));
        else if (lc.equals("reloadpropertiesinterval")) setReloadPropertiesInterval(value);
        else ret = false;
        return ret;
    }
    
    public static String get(String name) {
        String lc = name == null ? null : name.toLowerCase();
        if (name == null) return null;// do nothing
        else if (lc.equals("autoflush")) return getAutoflush() ? "true" : "false";
        else if (lc.equals("exclude")) return getExclude();
        else if (lc.equals("excludecategories")) return getExcludecategories();
        else if (lc.equals("filter")) return getFilter() ? "true" : "false";
        else if (lc.equals("help")) return getHelp() ? "true" : "false";
        else if (lc.equals("include")) return getInclude();
        else if (lc.equals("includecategories")) return getIncludecategories();
        else if (lc.equals("logfile")) return getLogfile();
        else if (lc.equals("realdriver")) return getRealdriver();
        else if (lc.equals("spydriver")) return getSpydriver();
        else if (lc.equals("append")) return getAppend() ? "true" : "false";
        else if (lc.equals("trace")) return getTrace() ? "true" : "false";
        else if (lc.equals("properties")) return getProperties();
        else if (lc.equals("dateformat")) return getDateformat();
        else if (lc.equals("stringmatcher")) return getStringmatcher();
        else if (lc.equals("sqlexpression")) return getSQLExpression();
        else if (lc.equals("stacktrace")) return getStackTrace() ? "true" : "false";
        else if (lc.equals("stacktraceclass")) return getStackTraceClass();
        else if (lc.equals("reloadproperties")) return getReloadProperties() ? "true":"false";
        else if (lc.equals("reloadpropertiesinterval")) return ""+getReloadPropertiesInterval();
        else return null;
    }
    
    public static Map getKeysMap() {
        HashMap keys = new HashMap();
        keys.put("autoflush", getAutoflush() ? "true" : "false");
        keys.put("excludecategories", getExcludecategories());
        keys.put("exclude", getExclude());
        keys.put("filter", getFilter() ? "true" : "false");
        keys.put("help", getHelp() ? "true" : "false");
        keys.put("include", getInclude());
        keys.put("includecategories", getIncludecategories());
        keys.put("logfile", getLogfile());
        keys.put("realdriver", getRealdriver());
        keys.put("spydriver", getSpydriver());
        keys.put("append", getAppend() ? "true" : "false");
        keys.put("trace", getTrace() ? "true" : "false");
        keys.put("properties", getProperties());
        keys.put("dateformat", getDateformat());
        keys.put("stringmatcher", getStringmatcher());
        keys.put("sqlexpression", getSQLExpression());
        keys.put("stacktrace", getStackTrace()?"true":"false");
        keys.put("stacktraceclass", getStackTraceClass());
        keys.put("reloadproperties",getReloadProperties()?"true":"false");
        keys.put("reloadpropertiesinterval",new Long(getReloadPropertiesInterval()));
        return keys;
    }
    
    public static Map getValuesMap() {
        HashMap values = new HashMap();
        values.put(getAutoflush() ? "true" : "false","autoflush");
        values.put(getExcludecategories(),"excludecategories");
        values.put(getExclude(),"exclude");
        values.put(getFilter() ? "true" : "false","filter");
        values.put(getHelp() ? "true" : "false","help");
        values.put(getInclude(),"include");
        values.put(getIncludecategories(),"includecategories");
        values.put(getLogfile(),"logfile");
        values.put(getRealdriver(),"realdriver");
        values.put(getSpydriver(),"spydriver");
        values.put(getAppend() ? "true" : "false","append");
        values.put(getTrace() ? "true" : "false","trace");
        values.put(getProperties(),"properties");
        values.put(getDateformat(),"dateformat");
        values.put(getStringmatcher(),"stringmatcher");
        values.put(getSQLExpression(),"sqlexpression");
        values.put(getStackTrace()?"true":"false","stacktrace");
        values.put(getStackTraceClass(),"stacktraceclass");
        values.put(getReloadProperties()?"true":"false","reloadproperties");
        values.put(new Long(getReloadPropertiesInterval()),"reloadpropertiesinterval");
        return values;
    }
    
    public static Collection getOptions() {
        ArrayList list = new ArrayList();
        list.add("autoflush");
        list.add("excludecategories");
        list.add("exclude");
        list.add("filter");
        list.add("help");
        list.add("include");
        list.add("includecategories");
        list.add("logfile");
        list.add("realdriver");
        list.add("spydriver");
        list.add("append");
        list.add("trace");
        list.add("properties");
        list.add("dateformat");
        list.add("stringmatcher");
        list.add("sqlexpression");
        list.add("stacktrace");
        list.add("stacktraceclass");
        list.add("reloadproperties");
        list.add("reloadpropertiesinterval");
        return list;
    }
    
    public static void checkReload() {
        long currentTime = System.currentTimeMillis();
        if (currentTime > lastCheck + reloadMs) {
            reloadProperties();
            lastCheck = currentTime;
        }
    }
}
