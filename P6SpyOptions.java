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
import java.lang.reflect.*;
import java.beans.*;

public class P6SpyOptions   {
    
    /* how to add a new property:
     *
     * attempted to make this as easy as possible.  just treat this like a normal
     * bean and create a get/set method.  the primary difference is:
     * (1) the set method should always accept a string [since we are reading
     * from a property file], parse this as necessary.  use p6util.isTrue
     * for boolean values.  (2) make sure to set a default value, if you
     * need one, within the set method.
     *
     * the rest is handled automatically.  introspection is used, but since
     * it is only incurred when a property file is loaded, which is at start
     * time and if you change it and have reload properties = true, the
     * expected overhead is minimal
     *
     */
    
    public static String SPY_PROPERTIES_FILE = "spy.properties";
    protected static long lastCheck = 0;
    
    static {initMethod();}
    
    public P6SpyOptions() {}
    
    private static boolean usePrefix;
    private static boolean autoflush;
    private static String exclude;
    private static boolean filter;
    private static String include;
    private static String logfile;
    private static String realdriver;
    private static String realdriver2;
    private static String realdriver3;
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
    private static boolean outageDetection;
    private static long outageDetectionInterval;
    private static long outageMs;
    
    static String propertiesPath;
    static long propertiesLastModified = -1;
    
    public static void setUsePrefix(String _usePrefix) {
        usePrefix = P6Util.isTrue(_usePrefix, false);
    }
    
    public static boolean getUsePrefix() {
        return usePrefix;
    }
    
    public static void setAutoflush(String _autoflush) {
        autoflush = P6Util.isTrue(_autoflush, false);
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
    
    public static void setFilter(String _filter) {
        filter = P6Util.isTrue(_filter, false);
    }
    
    public static boolean getFilter() {
        return filter;
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
        if (logfile == null) {
            logfile = "spy.log";
        }
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
    
    public static void setRealdriver2(String _realdriver2) {
        realdriver2 = _realdriver2;
    }
    
    public static String getRealdriver2() {
        return realdriver2;
    }
    
    public static void setRealdriver3(String _realdriver3) {
        realdriver3 = _realdriver3;
    }
    
    public static String getRealdriver3() {
        return realdriver3;
    }
    
    public static void setAppend(String _append) {
        append = P6Util.isTrue(_append, true);
    }
    
    public static boolean getAppend() {
        return append;
    }
    
    public static void setSpydriver(String _spydriver) {
        spydriver = _spydriver;
        if (spydriver == null) {
            spydriver = "com.p6spy.engine.spy.P6SpyDriver";
        }
    }
    
    public static String getSpydriver() {
        return spydriver;
    }
    
    public static void setTrace(String _trace) {
        trace = P6Util.isTrue(_trace, false);
    }
    
    // since getTrace is called almost everywhere, this is the
    // best property to engage reloading
    public static boolean getTrace() {
        checkReload();
        return trace;
    }
    
    public static void setProperties(String _properties) {
        properties = _properties;
        if (properties == null) {
            properties = SPY_PROPERTIES_FILE;
        }
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
    
    public static void setStackTrace(String _stacktrace) {
        stackTrace = P6Util.isTrue(_stacktrace, false);
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
    
    public static void setReloadProperties(String _reloadproperties) {
        reloadProperties = P6Util.isTrue(_reloadproperties, false);
    }
    
    public static long getReloadPropertiesInterval() {
        return reloadPropertiesInterval;
    }
    
    public static void setReloadPropertiesInterval(String _reloadpropertiesinterval) {
        reloadPropertiesInterval = P6Util.parseLong(_reloadpropertiesinterval,-1l);
        reloadMs = reloadPropertiesInterval * 1000l;
    }
    
    public static boolean getOutageDetection() {
        return outageDetection;
    }
    
    public static void setOutageDetection(String _outagedetection) {
        outageDetection = P6Util.isTrue(_outagedetection, false);
    }
    
    public static long getOutageDetectionInterval() {
        return outageDetectionInterval;
    }
    
    public static long getOutageDetectionIntervalMS() {
        return outageMs;
    }
    
    public static void setOutageDetectionInterval(String _outagedetectioninterval) {
        outageDetectionInterval = P6Util.parseLong(_outagedetectioninterval,-1l);
        outageMs = outageDetectionInterval * 1000l;
    }
    
    // --------------------------------------------------------------------------
    // END GET/SET METHODS, BEGIN HELPER CODE
    // --------------------------------------------------------------------------
    
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
    
    public static void reloadProperties() {
        if(reloadProperties && propertiesPath != null) {
            P6LogQuery.logDebug("checking property file to see if it needs to be reloaded");
            File propertiesFile = new File(propertiesPath);
            if(propertiesFile.exists()) {
                long lastModified = propertiesFile.lastModified();
                if(lastModified != propertiesLastModified) {
                    P6LogQuery.logDebug("reloading properties from file "+SPY_PROPERTIES_FILE);
                    setValues(P6Util.loadProperties(SPY_PROPERTIES_FILE));
                    propertiesLastModified = lastModified;
                    
                    // finally recheck the environment properties
                    P6Util.checkJavaProperties();
                    
                    P6LogQuery.initMethod();
                    P6LogQuery.logInfo("reloadProperties() successful");
                }
            }
        }
    }
    
    public static void checkReload() {
        long currentTime = System.currentTimeMillis();
        if (currentTime > lastCheck + reloadMs) {
            reloadProperties();
            lastCheck = currentTime;
        }
    }
    
    public static void setValues(Properties props) {
        try {
            ArrayList allMethods = P6Util.findAllMethods(P6SpyOptions.class);
            Iterator i = allMethods.iterator();
            while (i.hasNext()) {
                // lowercase and strip the end
                String methodName = ((String)i.next()).substring(3);
                dynamicSet("set"+methodName, (String)props.get(methodName.toLowerCase()));
            }
        } catch (IntrospectionException e) {
            P6Util.warn("Could not set property values due to IntrospectionException");
        }
    }
    
    public static void dynamicSet(String property, String value) {
        try {
            P6Util.set(P6SpyOptions.class, property, new String[] {value});
        } catch (IntrospectionException e) {
            P6Util.warn("Could not set property "+property+" due to IntrospectionException");
        } catch (IllegalAccessException e) {
            P6Util.warn("Could not set property "+property+" due to IllegalAccessException");
        } catch (NoSuchMethodException e) {
            // we are avoid this because it is perfectly okay for there to be get methods
            // we do not really want to set
        } catch (InvocationTargetException e) {
            P6Util.warn("Could not set property "+property+" due to InvoicationTargetException");
        }
    }
    
    public static String dynamicGet(String property) {
        try {
            Object value = P6Util.get(P6SpyOptions.class, property);
            return value == null ? null : value.toString();
        } catch (IntrospectionException e) {
            P6Util.warn("Could not get property "+property+" due to IntrospectionException");
        } catch (IllegalAccessException e) {
            P6Util.warn("Could not get property "+property+" due to IllegalAccessException");
        } catch (NoSuchMethodException e) {
            P6Util.warn("Could not get property "+property+" due to NoSuchMethodException");
        } catch (InvocationTargetException e) {
            P6Util.warn("Could not get property "+property+" due to InvoicationTargetException");
        }
        return null;
    }
    
    public static Collection dynamicGetOptions() {
        try {
            return (P6Util.findAllMethods(P6SpyOptions.class));
        } catch (IntrospectionException e) {
            P6Util.warn("Could not get options list due to IntrospectionException");
        }
        return null;
    }
}