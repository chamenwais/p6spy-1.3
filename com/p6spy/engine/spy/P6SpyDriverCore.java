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
 * Revision 1.7  2003/01/16 00:50:03  jeffgoke
 * changed Error call to use syntax compatible prior to 1.4
 *
 * Revision 1.6  2003/01/15 22:11:52  aarvesen
 * do some stronger error trapping and die on error
 *
 * Revision 1.5  2003/01/10 21:40:11  jeffgoke
 * changed to use new error handling facility
 *
 * Revision 1.4  2003/01/03 21:18:03  aarvesen
 * use the new P6Util.forName
 *
 * Revision 1.3  2002/12/20 00:04:09  aarvesen
 * New style of driver!
 *
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
    protected static boolean initialized = false;
    protected static ArrayList factories;

    /* 
     * This core class serves to purposes
     * 
     * (1) it acts as a bootstap class the first time
     * it is invoked and it loads not itself, but the first driver on the stack.  This is
     * important because P6SpyDriver, P6SpyDriver2, etc. extend this class and performs
     * the initial bootstrap
     * 
     * (2) when connect or acceptURL are invoked it ensures it has a passthru driver.
     * 
     *
     */
    
    public synchronized static void initMethod(String spydriver) {
	// this is the *only* p6 driver
	// we need to build two lists here:
	// one of the modules that are loaded, and one of the
	// realdriver(s) that we need

	// these are defined outside the try block for error messaging

	if (initialized) {
	    return;
	}

	String className = "no class";
	String classType  = "driver";
	try {
	    ArrayList driverNames = null;
	    ArrayList modules = null;

	    driverNames = P6SpyOptions.allDriverNames();
	    modules = P6SpyOptions.allModules();

	    boolean hasModules = modules.size() > 0;

	    Iterator i = null;

	    // register drivers and wrappers
	    classType = "driver";
	    i = driverNames.iterator();
	    while (i.hasNext()) {
		P6SpyDriver spy = null;
		// register P6 first if you are using it
		if (hasModules) {
		    spy = new P6SpyDriver();
		    DriverManager.registerDriver(spy);
		}

		className = (String) i.next();
		Driver realDriver = (Driver)P6Util.forName(className).newInstance();
		// now wrap your realDriver in the spy
		if (hasModules) {
		    spy.setPassthru(realDriver);
		}
	    }

	    // instantiate the factories, if nec.
	    if (hasModules) {
		factories = new ArrayList();
		classType = "factory";
		i = modules.iterator();
		while (i.hasNext()) {
		    className = (String) i.next();
		    P6Factory factory = (P6Factory)P6Util.forName(className).newInstance();
		    factories.add(factory);
		}
	    }

	    initialized = true;
	} catch (Exception e) {
	    String err = "Error registering " + classType + "  [" + className + "]\nCaused By: " + e.toString();
	    P6LogQuery.logError(err);
	    throw new P6DriverNotFoundError(err);
	}
	
    }
    
    public P6SpyDriverCore(String _spydriver, P6Factory _p6factory) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
	// should really change the constructor here :)
    }
      
    // these methods are the secret sauce here
    public static Connection wrapConnection(Connection realConnection) throws SQLException {
	Connection con = realConnection;
	if (factories != null) {
	    Iterator it    = factories.iterator();
	    while (it.hasNext()) {
		P6Factory factory = (P6Factory) it.next();
		con = factory.getConnection(con);
	    }
	}
	return con;
    }

    public Driver getPassthru() {
	return passthru;
    }

    public void setPassthru(Driver inVar) {
	passthru = inVar;
    }

    private String getRealUrl(String url) {
        if (P6SpyOptions.getUsePrefix()) {
            return url.startsWith("p6spy:") ? url.substring("p6spy:".length()) : null;
        } else {
            return url;
        }
    }
    


    // the remaining methods are for the Driver interface
    public Connection connect(String p0, java.util.Properties p1) throws SQLException {
        String realUrl = this.getRealUrl(p0);
        if (realUrl==null) {
            throw new SQLException("URL needs the p6spy prefix: "+p0);
        }
        Connection conn = passthru.connect(realUrl,p1);

	if (conn != null) {
	    conn = wrapConnection(conn);
	}
        return conn;
    }
    
    public boolean acceptsURL(String p0) throws SQLException {
        String realUrl = this.getRealUrl(p0);
	boolean accepts = false;

        if (realUrl != null) {
	    accepts = passthru.acceptsURL(realUrl);
        }
	return accepts;
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
    

}
