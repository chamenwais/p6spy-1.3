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

public abstract class P6SpyDriverCore implements Driver {
    
    protected Driver passthru = null;
    protected String realdriver = null;
    
    public synchronized static void initMethod(String spydriver) {
        P6Util.checkJavaProperties();
        P6LogQuery.logInfo("P6Spy trace is on: "+P6SpyOptions.getTrace());
        try {
            DriverManager.registerDriver((Driver)Class.forName(spydriver).newInstance());
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
    
    public P6SpyDriverCore(String _realdriver) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        realdriver = _realdriver;
    }
    
    public final Connection connect(String p0, java.util.Properties p1) throws SQLException {
        String realUrl = this.getRealUrl(p0);
        if (realUrl==null) {
            throw new SQLException("URL needs the p6spy prefix: "+p0);
        }
        registerRealDriver();
        Connection conn = passthru.connect(realUrl,p1);
        return conn == null ? null : new P6Connection(conn);
    }
    
    public final boolean acceptsURL(String p0) throws SQLException {
        String realUrl = this.getRealUrl(p0);
        if (realUrl==null) {
            return false;
        }
        registerRealDriver();
        return(passthru.acceptsURL(realUrl));
    }
    
    private String getRealUrl(String url) {
        if (P6SpyOptions.getUsePrefix()) {
            return url.startsWith("p6spy:") ? url.substring("p6spy:".length()) : null;
        } else {
            return url;
        }
    }
    
    public final DriverPropertyInfo [] getPropertyInfo(String p0, java.util.Properties p1) throws SQLException {
        return(passthru.getPropertyInfo(p0,p1));
    }
    
    public final int getMajorVersion(){
        return(passthru.getMajorVersion());
    }
    
    public final int getMinorVersion(){
        return(passthru.getMinorVersion());
    }
    
    public final boolean jdbcCompliant(){
        return(passthru.jdbcCompliant());
    }
    
    protected synchronized final void registerRealDriver() throws SQLException {
        if (passthru == null) {
            try {
                if (realdriver == null) {
                    throw new SQLException("P6Spy: Cannot find what driver to load");
                }
                Class driverClass = Class.forName(realdriver);
                passthru = (Driver) driverClass.newInstance();
                P6LogQuery.logInfo("P6Spy successfully registered driver "+realdriver);
            } catch (ClassNotFoundException e1) {
                throw new SQLException("Error registering Driver <" + realdriver + "> ClassNotFoundException " + realdriver);
            } catch (InstantiationException e2) {
                throw new SQLException("Error registering Driver <" + realdriver + "> InstantiationException " + realdriver);
            } catch (IllegalAccessException e3) {
                throw new SQLException("Error registering Driver <" + realdriver + "> IllegalAccessException " + realdriver);
            } catch (NullPointerException e5) {
                throw new SQLException("Error registering Driver <" + realdriver + "> NullPointerException " + realdriver);
            }
        }
    }
}
