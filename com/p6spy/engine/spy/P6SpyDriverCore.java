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
 * Description: Wrapper class for Driver
 *
 * $Author$
 * $Revision$
 * $Date$
 *
 * $Id$
 * $Log$
 * Revision 1.2  2002/10/06 18:23:25  jeffgoke
 * no message
 *
 * Revision 1.1  2002/05/24 07:31:13  jeffgoke
 * version 1 rewrite
 *
 * Revision 1.1  2002/05/16 04:58:40  jeffgoke
 * Viktor Szathmary added multi-driver support.
 * Rewrote P6SpyOptions to be easier to manage.
 * Fixed several bugs.
 *
 * Revision 1.6  2002/05/05 00:43:00  jeffgoke
 * Added Philip's reload code.
 *
 * Revision 1.5  2002/04/15 05:13:32  jeffgoke
 * Simon Sadedin added timing support.  Fixed bug where batch execute was not
 * getting logged.  Added result set timing.  Updated the log format to include
 * categories, and updated options to control the categories.  Updated
 * documentation.
 *
 * Revision 1.4  2002/04/10 06:49:26  jeffgoke
 * added more debug information and a new property for setting the log's date format
 *
 * Revision 1.3  2002/04/10 05:22:09  jeffgoke
 * included debug option and a message at driver initialization time
 *
 * Revision 1.2  2002/04/07 20:43:59  jeffgoke
 * fixed bug that caused null connection to return an empty connection instead of null.
 * added an option allowing the user to truncate.
 * added a release target to the build to create the release files.
 *
 * Revision 1.1.1.1  2002/04/07 04:52:25  jeffgoke
 * no message
 *
 * Revision 1.3  2001-08-02 07:52:44-05  andy
 * <>
 *
 * Revision 1.2  2001-07-30 23:37:33-05  andy
 * <>
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
import java.io.*;
import java.util.*;
import com.p6spy.engine.common.*;

public abstract class P6SpyDriverCore implements Driver {
    
    protected Driver passthru = null;
    protected String suffix = null;
    protected P6Factory p6factory = null;
    protected static Object lock = new Object();
    public static HashMap driverStacks = new HashMap();
    protected static HashSet driversRegistered = new HashSet();
    protected static String DRIVERSTACK_KEY = "driverstack";

    /* 
     * This core class serves to purposes:
     * 
     * (1) it acts as a bootstap class the first time
     * it is invoked and it loads not itself, but the first driver on the stack.  This is
     * important because P6SpyDriver, P6SpyDriver2, etc. extend this class and performs
     * the initial bootstrap.
     * 
     * (2) when connect or acceptURL are invoked it ensures it has a passthru driver.
     * 
     *
     */
    
    public synchronized static void initMethod(String spydriver) {
        // if this is the bootstrap driver, we need to create the driver stack
        // but we won't actually register this driver
        
        // we only want to register one p6driver per suffix and we need to create
        // an individual stack per suffix
        String suffixString = getSuffix(spydriver);
        
        Stack driverStack = null;
        synchronized (lock) {
            driverStack = (Stack)driverStacks.get(DRIVERSTACK_KEY+suffixString);
            if (driverStack == null) {
                try {
                    P6LogQuery.logDebug("Building driver stack");
                    driverStack = buildDriverStack(suffixString);
                    P6LogQuery.logDebug("Driver stack size: "+driverStack.size());
                    String createDriver = (String)driverStack.pop();
                    Class.forName(createDriver);
                    P6LogQuery.logInfo("Created instance of driver: "+createDriver);
                } catch (ClassNotFoundException e1) {
                    P6Util.warn("Error registering Driver P6SpyDriver " + spydriver + " " + e1);
                } catch (SQLException e2) {
                    P6Util.warn("Error registering Driver P6SpyDriver " + spydriver + " " + e2);
                }
            }
        }
        
        if (driversRegistered.contains(DRIVERSTACK_KEY+suffixString) == false) {
            driversRegistered.add(DRIVERSTACK_KEY+suffixString);
            P6Util.checkJavaProperties();
            P6LogQuery.logInfo("P6Spy trace is on: "+P6SpyOptions.getTrace());
            try {
                DriverManager.registerDriver((Driver)Class.forName(spydriver).newInstance());
                P6LogQuery.logInfo("Registered driver: "+spydriver);
            } catch (ClassNotFoundException e1) {
                P6Util.warn("Error registering Driver P6SpyDriver " + spydriver + " " + e1);
            } catch (SQLException e2) {
                P6Util.warn("Error registering Driver P6SpyDriver " + spydriver + " " + e2);
            } catch (InstantiationException e3) {
                P6Util.warn("Error registering Driver P6SpyDriver " + spydriver + " " + e3);
            } catch (IllegalAccessException e4) {
                P6Util.warn("Error registering Driver P6SpyDriver " + spydriver + " " + e4);
            }
        }
    }
    
    public P6SpyDriverCore(String _spydriver, P6Factory _p6factory) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        suffix = getSuffix(_spydriver);
        p6factory = _p6factory;
    }
    
    public Connection connect(String p0, java.util.Properties p1) throws SQLException {
        String realUrl = this.getRealUrl(p0);
        if (realUrl==null) {
            throw new SQLException("URL needs the p6spy prefix: "+p0);
        }
        getRealDriver();
        Connection conn = passthru.connect(realUrl,p1);
        return conn == null ? null : p6factory.getConnection(conn);
    }
    
    public boolean acceptsURL(String p0) throws SQLException {
        String realUrl = this.getRealUrl(p0);
        if (realUrl==null) {
            return false;
        }
        getRealDriver();
        return(passthru.acceptsURL(realUrl));
    }
    
    private String getRealUrl(String url) {
        if (P6SpyOptions.getUsePrefix()) {
            return url.startsWith("p6spy:") ? url.substring("p6spy:".length()) : null;
        } else {
            return url;
        }
    }
    
    public DriverPropertyInfo [] getPropertyInfo(String p0, java.util.Properties p1) throws SQLException {
        return(passthru.getPropertyInfo(p0,p1));
    }
    
    public int getMajorVersion(){
        return(passthru.getMajorVersion());
    }
    
    public int getMinorVersion(){
        return(passthru.getMinorVersion());
    }
    
    public boolean jdbcCompliant(){
        return(passthru.jdbcCompliant());
    }
    
    protected synchronized void getRealDriver() throws SQLException {
        if (passthru == null) {
            String registerDriver = null;
            try {
                synchronized (lock) {
                    Stack driverStack = (Stack)driverStacks.get(DRIVERSTACK_KEY+suffix);
                    if (driverStack == null) {
                        throw new SQLException("P6Spy: Initialization problem, cannot find what driver to load");
                    }
                    registerDriver = (String)driverStack.pop();
                }
                if (registerDriver == null) {
                    throw new SQLException("P6Spy: Cannot find what driver to load");
                }
                passthru = (Driver) Class.forName(registerDriver).newInstance();
                P6LogQuery.logInfo("P6Spy successfully registered driver "+registerDriver);
            } catch (ClassNotFoundException e1) {
                throw new SQLException("Error registering Driver <" + registerDriver + "> ClassNotFoundException " + registerDriver);
            } catch (InstantiationException e2) {
                throw new SQLException("Error registering Driver <" + registerDriver + "> InstantiationException " + registerDriver);
            } catch (IllegalAccessException e3) {
                throw new SQLException("Error registering Driver <" + registerDriver + "> IllegalAccessException " + registerDriver);
            } catch (NullPointerException e5) {
                throw new SQLException("Error registering Driver <" + registerDriver + "> NullPointerException " + registerDriver);
            } catch (EmptyStackException e) {
                throw new SQLException("P6Spy: Driver stack is empty, cannot find what driver to load");
            }
        }
    }
    
    protected static Stack buildDriverStack(String suffixString) throws SQLException {
        P6LogQuery.logDebug("Creating all modules array");
        ArrayList allModules = P6SpyOptions.allModules();
        if (allModules == null) {
            P6LogQuery.logDebug("No modules found, exiting");
            return null;
        }
        String realdriver = P6SpyOptions.dynamicGet("getRealdriver"+suffixString);
        if (realdriver == null) {
            throw new SQLException("P6Spy was specified without a realdriver.");
        }
        
        Stack stack = new Stack();
        P6LogQuery.logDebug("Pushing realdriver on stack: "+realdriver);
        stack.push(realdriver);
        
        Iterator i = allModules.iterator();
        while (i.hasNext()) {
            String module = (String)i.next();
            P6LogQuery.logDebug("Pushing module on stack: "+module);
            stack.push(module+suffixString);
        }
        
        driverStacks.put(DRIVERSTACK_KEY+suffixString, stack);
        
        return stack;
    }
    
    public static String getSuffix(String realdriver) {
        // assumes we support less than 10 virtual drivers
        String suffixString = realdriver.substring(realdriver.length()-1);
        // assume it is the first driver if not a number
        try {
            Integer.parseInt(suffixString);
        } catch (NumberFormatException e) {
            suffixString = "";
        }
        return suffixString;
    }
}
