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
 * Revision 1.2  2002/05/05 00:43:00  jeffgoke
 * Added Philip's reload code.
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

public abstract class P6TestFramework extends TestCase {
    
    public P6TestFramework(java.lang.String testName) {
        super(testName);
    }
    
    protected Connection connection = null;
    
    protected void setUp() {
        try {
            Properties props = loadProperties("P6Test.properties");
            
            String drivername = props.getProperty("p6driver");
            String user = props.getProperty("user");
            String password = props.getProperty("password");
            String url = props.getProperty("url");
            
            Class.forName(drivername);
            connection = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            fail(e.getMessage());
        }
        
        P6SpyOptions.SPY_PROPERTIES_FILE = "spy.properties";
        P6SpyOptions.initMethod();
        P6SpyDriver.initMethod();
        P6LogQuery.initMethod();
    }
    
    protected void tearDown() {
        try {
            if (connection != null) {
                connection.close();
            }
        }  catch (Exception e) {
            fail(e.getMessage());
        }
    }
    
    protected Properties loadProperties(String filename) {
        if (filename == null) {
            System.err.println("No "+filename+" properties file specified.");
            System.exit(1);
        }
        
        Properties props = new Properties();
        
        try {
            FileInputStream fis = new FileInputStream(filename);
            if (fis == null) {
                System.err.println("Unable to find properties file: "+filename);
                System.exit(1);
            }
            props.load(fis);
            fis.close();
        }
        catch (IOException e) {
            System.err.println("Unable to read properties from properties file: "+filename+".  Exception: "+e.toString());
            System.exit(1);
        }
        return props;
    }
    
    protected static String getStackTrace(Exception e) {
        CharArrayWriter c = new CharArrayWriter();
        e.printStackTrace(new PrintWriter(c));
        return c.toString();
    }
}
