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

public class P6SpyOptions   {
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
    private static boolean debug;
    private static String dateformat;
    private static SimpleDateFormat dateformatter;
    
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
    public static boolean getTrace() {
        return trace;
    }
    public static void setDebug(boolean _debug) {
        debug = _debug;
    }
    public static boolean getDebug() {
        return debug;
    }
    public static void setProperties(String _properties) {
        properties = _properties;
    }
    public static String getProperties() {
        return properties;
    }
    public static void setDateformat(String _dateformat) {
        dateformat = _dateformat;
        if (_dateformat == null) {
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
        setFilter(getFilter());
        setHelp(getHelp());
        setInclude(getInclude());
        setLogfile(getLogfile());
        setRealdriver(getRealdriver());
        setSpydriver(getSpydriver());
        setAppend(getAppend());
        setTrace(getTrace());
        setProperties(getProperties());
        setDebug(getDebug());
        setDateformat(getDateformat());
    }
    
    public static void help() {
        System.out.println("\nEngine:");
        System.out.println("    autoflush [true]                          - turn on autoflush");
        System.out.println("    exclude []                                - comma separated list of tables to exclude");
        System.out.println("    filter [false]                            - turn on filtering");
        System.out.println("    help [false]                              - print help message or not");
        System.out.println("    include []                                - comma separated list of tables to include");
        System.out.println("    logfile [spy.log]                         - name of logfile if trace is on");
        System.out.println("    realdriver []                             - name of real jdbc driver to load");
        System.out.println("    spydriver [com.p6.engine.spy.P6SpyDriver] - name of SPY Driver");
        System.out.println("    trace [false]                             - turn on tracing");
        System.out.println("    append [true]                             - append to the P6Spy log file (false = truncate)");
        System.out.println("    debug [false]                             - outputs P6Spy debug statements");
        System.out.println("    dateformat []                             - simple date format for log file");
        System.out.println("\nGlobal:");
        System.out.println("    properties [spy.properties]               - name of file that stores the properties info");
    }
    
    public static void help(String category) {
        if (category.equalsIgnoreCase("Engine")) {            System.out.println("\nEngine:");            System.out.println("    autoflush [true]                          - turn on autoflush");        }
        if (category.equalsIgnoreCase("Engine")) {            System.out.println("    exclude []                                - comma separated list of tables to exclude");        }
        if (category.equalsIgnoreCase("Engine")) {            System.out.println("    filter [false]                            - turn on filtering");        }
        if (category.equalsIgnoreCase("Engine")) {            System.out.println("    help [false]                              - print help message or not");        }
        if (category.equalsIgnoreCase("Engine")) {            System.out.println("    include []                                - comma separated list of tables to include");        }
        if (category.equalsIgnoreCase("Engine")) {            System.out.println("    logfile [spy.log]                         - name of logfile if trace is on");        }
        if (category.equalsIgnoreCase("Engine")) {            System.out.println("    realdriver []                             - name of real jdbc driver to load");        }
        if (category.equalsIgnoreCase("Engine")) {            System.out.println("    spydriver [com.p6.engine.spy.P6SpyDriver] - name of SPY Driver");        }
        if (category.equalsIgnoreCase("Engine")) {            System.out.println("    trace [false]                             - turn on tracing");        }
        if (category.equalsIgnoreCase("Engine")) {            System.out.println("    append [true]                             - append to the P6Spy log file (false = truncate)");        }
        if (category.equalsIgnoreCase("Engine")) {            System.out.println("    debug [false]                             - output P6Spy debug statements");        }
        if (category.equalsIgnoreCase("Engine")) {            System.out.println("    dateformat []                             - simple date format for log file");        }
        if (category.equalsIgnoreCase("Global")) {            System.out.println("\nGlobal:");            System.out.println("    properties [spy.properties]               - name of file that stores the properties info");        }
    }
    
    public static void initMethod() {
        Properties props  = P6Util.loadProperties("spy.properties");
        String value;
        value = props.getProperty("autoflush");
        if (value == null) value = "true";
        setAutoflush(P6Util.isTrue(value));
        value = props.getProperty("exclude");
        setExclude(value);
        value = props.getProperty("filter");
        if (value == null) value = "false";
        setFilter(P6Util.isTrue(value));
        value = props.getProperty("help");
        if (value == null) value = "false";
        setHelp(P6Util.isTrue(value));
        value = props.getProperty("include");
        setInclude(value);
        value = props.getProperty("logfile");
        if (value == null) value = "spy.log";
        setLogfile(value);
        value = props.getProperty("realdriver");
        setRealdriver(value);
        value = props.getProperty("spydriver");
        if (value == null) value = "com.p6.engine.spy.P6SpyDriver";
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
        value = props.getProperty("debug");
        if (value == null) value = "true";
        setDebug(P6Util.isTrue(value));
        value = props.getProperty("dateformat");
        setDateformat(value);
    }
    
    public static boolean set(String name, String value) {
        boolean ret = true;
        String lc = name == null ? null : name.toLowerCase();
        if (name == null) ;// do nothing
        else if (lc.equals("autoflush")) setAutoflush(P6Util.isTrue(value));
        else if (lc.equals("exclude")) setExclude(value);
        else if (lc.equals("filter")) setFilter(P6Util.isTrue(value));
        else if (lc.equals("help")) setHelp(P6Util.isTrue(value));
        else if (lc.equals("include")) setInclude(value);
        else if (lc.equals("logfile")) setLogfile(value);
        else if (lc.equals("realdriver")) setRealdriver(value);
        else if (lc.equals("spydriver")) setSpydriver(value);
        else if (lc.equals("append")) setAppend(P6Util.isTrue(value));
        else if (lc.equals("trace")) setTrace(P6Util.isTrue(value));
        else if (lc.equals("properties")) setProperties(value);
        else if (lc.equals("debug")) setDebug(P6Util.isTrue(value));
        else if (lc.equals("properties")) setProperties(value);
        else if (lc.equals("dateformat")) setDateformat(value);
        else ret = false;
        return ret;
    }
    
    public static String get(String name) {
        String lc = name == null ? null : name.toLowerCase();
        if (name == null) return null;// do nothing
        else if (lc.equals("autoflush")) return getAutoflush() ? "true" : "false";
        else if (lc.equals("exclude")) return getExclude();
        else if (lc.equals("filter")) return getFilter() ? "true" : "false";
        else if (lc.equals("help")) return getHelp() ? "true" : "false";
        else if (lc.equals("include")) return getInclude();
        else if (lc.equals("logfile")) return getLogfile();
        else if (lc.equals("realdriver")) return getRealdriver();
        else if (lc.equals("spydriver")) return getSpydriver();
        else if (lc.equals("append")) return getAppend() ? "true" : "false";
        else if (lc.equals("trace")) return getTrace() ? "true" : "false";
        else if (lc.equals("properties")) return getProperties();
        else if (lc.equals("debug")) return getDebug() ? "true" : "false";
        else if (lc.equals("dateformat")) return getDateformat();
        else return null;
    }
    
    public static Map getKeysMap() {
        HashMap keys = new HashMap();
        keys.put("autoflush", getAutoflush() ? "true" : "false");
        keys.put("exclude", getExclude());
        keys.put("filter", getFilter() ? "true" : "false");
        keys.put("help", getHelp() ? "true" : "false");
        keys.put("include", getInclude());
        keys.put("logfile", getLogfile());
        keys.put("realdriver", getRealdriver());
        keys.put("spydriver", getSpydriver());
        keys.put("append", getAppend() ? "true" : "false");
        keys.put("trace", getTrace() ? "true" : "false");
        keys.put("properties", getProperties());
        keys.put("debug", getDebug() ? "true" : "false");
        keys.put("dateformat", getDateformat());
        return keys;
    }
    
    public static Map getValuesMap() {
        HashMap values = new HashMap();
        values.put(getAutoflush() ? "true" : "false","autoflush");
        values.put(getExclude(),"exclude");
        values.put(getFilter() ? "true" : "false","filter");
        values.put(getHelp() ? "true" : "false","help");
        values.put(getInclude(),"include");
        values.put(getLogfile(),"logfile");
        values.put(getRealdriver(),"realdriver");
        values.put(getSpydriver(),"spydriver");
        values.put(getAppend() ? "true" : "false","append");
        values.put(getTrace() ? "true" : "false","trace");
        values.put(getProperties(),"properties");
        values.put(getDebug() ? "true" : "false","debug");
        values.put(getDateformat(),"dateformat");
        return values;
    }
    
    public static Collection getOptions() {
        ArrayList list = new ArrayList();
        list.add("autoflush");
        list.add("exclude");
        list.add("filter");
        list.add("help");
        list.add("include");
        list.add("logfile");
        list.add("realdriver");
        list.add("spydriver");
        list.add("append");
        list.add("trace");
        list.add("properties");
        list.add("debug");
        list.add("dateformat");
        return list;
    }
}
