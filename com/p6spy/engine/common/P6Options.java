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


package com.p6spy.engine.common;

import java.io.*;
import java.util.*;

public class P6Options {
    /*
     * Ground up re-write.  Instead of the 38 private variables and
     * their associated 75+ getter/setter methods, take a step
     * backwards into the hash map world.  This should look a lot like
     * the java Properties class to you, but with a couple of extra hacks.
     *
     * Benefits: 
     *	much easier to add a new property.  Just add it in and it
     * will get loaded into the has.  I strongly suggest that you use static
     * variables as the property names to save yourself some headache.
     *	This also makes it easier to implement module specific variables, since
     * we can easily just index into the hash based off of the factory name
     * (that is, the module) that you're coming from.
     *
     * Costs:
     *	Incur an associated hash lookup everytime you get an option
     *	Loose compile time checking of your options
     *
     */
    
    // first hack -- use a system property so that you can override the
    // name of your properties file: -Dspy.properties=someotherfile.txt
    public static final String OPTIONS_FILE_PROPERTY = "spy.properties";
    public static final String DFLT_OPTIONS_FILE     = "spy.properties";

    // have you loaded in the modules?  We still don't want to 
    // monkey around with dynaloading these suckers.  The
    // module refs get stored in the HashMap there
    protected static boolean initialized = false;
    protected static HashMap moduleList  = new HashMap();
    protected static Hashtable propertyHash = new Hashtable();
    
    public static final String MODULE_PREFIX     = "module";
    protected static final int prefixLen         = MODULE_PREFIX.length();
    public static long propertiesLastModified    = 0;
    protected static Thread reloadThread         = null;
    protected static OptionReloader reloader     = null;

    public static final String RELOAD_PROPERTY   = "reloadproperties";
    public static final String INTERVAL_PROPERTY = "reloadpropertiesinterval";


    // load the properties.  With no file, then lookup whateve
    // propertyFileName is and pass it to the other one.  This
    // lets you do some action with reloading the file.
    public static void loadProperties() {
	File properties = new File(getPropertyFileName());
	loadProperties(properties);
    }

    public static void loadProperties(File file) {
	try {
	    String name;
	    Enumeration names;
	    FileInputStream fis = new FileInputStream(file);
	    Properties props    = new Properties();

	    props.load(fis);
	    
	    // the first time through, we'll go through all of the modules
	    // this means that we'll run through the names twice
	    // the first time through, but whatever.
	    if (!initialized) {
		names = props.propertyNames();
		while (names.hasMoreElements()) {
		    name = (String) names.nextElement();
		    if (name.startsWith(MODULE_PREFIX)) {
			addModule(moduleList, name, props.getProperty(name));
		    }
		}
	    }
	    
	    // now go through and find all of the properties
	    // that are not modules and create them as appropriate
	    for (names = props.propertyNames(); names.hasMoreElements();) {
		name = (String) names.nextElement();
		    if (! name.startsWith(MODULE_PREFIX)) {
			String opt = props.getProperty(name);
			DoubleOption dbl = new DoubleOption(moduleList, name);
			propertyHash.put(dbl, opt);
		    }
	    }

	    // now you've loaded all the properties, read through the
	    // opts that are germane to Options
	    configure();
	    
	    initialized = true;

	} catch (IOException e) {
	    System.out.println("Could not load properties file " + file);
	}
    }

    protected static void configure() {
	if (getBoolean(RELOAD_PROPERTY, false)) {
	    int interval = getInt(INTERVAL_PROPERTY, 6000);
	    
	    // check to see if the thread is running.  If so,
	    // then change the sleep factor. if not, then
	    if (reloader == null) {
		reloader     = new OptionReloader(interval);
		reloadThread = new Thread(reloader);
		reloadThread.setDaemon(true);
		reloadThread.start();
	    } else {
		reloader.setRunning(true);
		reloader.setSleep(interval);
	    }
	} else {
	    // if it's false, and you're currently reloading
	    // then turn it off.
	    if (reloader != null) {
		reloader.setRunning(false);
		reloader = null;
	    }
	}
    }

    public static String getPropertyFileName() {
	String propertyFileName = System.getProperty(OPTIONS_FILE_PROPERTY, DFLT_OPTIONS_FILE);
	return propertyFileName;
    }

    public static void reloadProperties() {
	File properties = new File(getPropertyFileName());
	if(properties.exists()) {
	    long lastModified = properties.lastModified();
	    if(lastModified != propertiesLastModified) {
		propertiesLastModified = lastModified;
		clearProperties();
		loadProperties(properties);
	    }
	}
    }

    // make this public so it's easy to call from 
    // the test code
    public static void clearProperties() {
	propertyHash = new Hashtable();
    }

    // jam through until you find the first . or _
    protected static String getSuffix(String fullName, int start) {
	String module = null;
	boolean found = false;
	for (int current = start; current < fullName.length(); current++) {
	    char c = fullName.charAt(current);
	    if (found) {
		module = fullName.substring(current);
		break;
	    }
	    found = (c == '.' || c == '_');
	}
	return module;
    }

    protected static void addModule(HashMap hash, String fullName, String className) {
	// since we know the fullName starts with "module", skip
	// any . or _'s
	// so "module_foo", "module.foo" and "modulefoo" are all cool,
	// but you will choke on just "module"
	// NB that I used a primitive (i.e. non-unicode) method 
	// to determine alpha-ness
	String module = getSuffix(fullName, prefixLen);

	if (module != null) {
	    hash.put(module, className);
	}
    }
    /*----------------------------------------------------------------------*\
	here's the lookup code: now that you've gone through the
	effort of building up the options, you want to be able to
	get at them easily.
    \*----------------------------------------------------------------------*/
    public static String getProperty(String className, String prop) {
	String value = null;
	DoubleOption opt = new DoubleOption(className, prop);
	if ((value = (String) propertyHash.get(opt)) == null) {
	    if (className != "") {
		opt.setClassName("");
		value = (String) propertyHash.get(opt);
	    }
	}
	return value;
    }

    public static boolean getBoolean(String prop, boolean dflt) {
	return getBoolean ("", prop, dflt);
    }
    public static boolean getBoolean(String className, String prop, boolean dflt) {
	String value = getProperty(className, prop);
	return P6Util.isTrue(value, dflt);
    }

    public static int getInt(String prop, int dflt) {
	return getInt("", prop, dflt);
    }
    public static int getInt(String className, String prop, int dflt) {
	String value = getProperty(className, prop);
	return P6Util.parseInt(value, dflt);
    }
}
