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

package com.p6spy.engine.test;

import junit.framework.*;
import java.io.*;
import java.util.*;

import com.p6spy.engine.common.*;

public class P6TestOptions extends TestCase {

    public static final String TEST_OPTION = "testset";
    protected String testFile1 = "unittest1.properties";
    protected String testFile2 = "unittest2.properties";
    protected String testFile3 = "unittest3.properties";

    protected String[] testValues1 = new String[] { "1", "2", "3", "4"};
    protected String[] testValues2 = new String[] { null, "a", "b", "c"};
    protected String[] testValues3 = testValues2;

    public P6TestOptions(String name) {
	super(name);
    }

    public void testOptions() {
	try {

	    chkProperties(testFile1, testValues1);
	    P6Options.clearProperties();
	    chkProperties(testFile2, testValues2);

        } catch (Exception e) {
	    e.printStackTrace(System.err);
            fail(e.getMessage()+" due to error: ");
        }
    }

    public void testReloadThread() {
	
	try {
	    // load in the third properties file, which
	    // will spawn the reload thread.  Then reload the first
	    // properties file and check.  The first file will turn
	    // off the reloading thread.  So set the file to get the
	    // second file, and it should NOT reload (since the first
	    // file turns off the thread)
	    chkProperties(testFile3, testValues3);

	    // reload property file 1
	    System.setProperty(P6Options.OPTIONS_FILE_PROPERTY, testFile1);
	    Thread.sleep(1000);
	    chkProperties(testValues1);

	    // now set to file2, but it should not reload, so check
	    // on value1 again
	    System.setProperty(P6Options.OPTIONS_FILE_PROPERTY, testFile1);
	    Thread.sleep(1000);
	    chkProperties(testValues1);

        } catch (Exception e) {
	    e.printStackTrace(System.err);
            fail(e.getMessage()+" due to error: ");
        }
    }

    public void testReload() {
	
	try {
	    // load in the first properties file
	    // then touch the second file, call reload, and test again
	    chkProperties(testFile1, testValues1);

	    File file = new File(testFile2);
	    file.setLastModified(System.currentTimeMillis());
	    System.setProperty(P6Options.OPTIONS_FILE_PROPERTY, testFile2);

	    P6Options.reloadProperties();

	    chkProperties(testValues2);

        } catch (Exception e) {
	    e.printStackTrace(System.err);
            fail(e.getMessage()+" due to error: ");
        }
    }
    
    // load the file and call chkProperties
    protected void chkProperties(String file, String[] props) {
	System.setProperty(P6Options.OPTIONS_FILE_PROPERTY, file);
	P6Options.loadProperties();
	chkProperties(props);
    }

    // check a string of standard properties
    protected void chkProperties(String[] props) {
	String logClass  = "com.p6spy.engine.logging.P6LogSpyDriver";
	String outClass  = "com.p6spy.engine.outage.P6OutageSpyDriver";

	chkProperty("", props[0]);
	chkProperty("gobbledygook", props[0]);

	chkProperty(logClass, props[1]);
	chkProperty(outClass, props[2]);
	chkProperty("", "nomodule." + TEST_OPTION, props[3]);
    }

    // check the TEST_OPTION for a specific class
    protected void chkProperty(String className, String exp) {
	chkProperty(className, TEST_OPTION, exp);
    }

    // check a particular class and option
    protected void chkProperty(String className, String optionName, String exp) {
	String value = P6Options.getProperty(className, optionName);
	if (exp == null ) {
	    assertNull("Expected null value for " + className + "." + optionName + " but found '" + value + "'", value);
	} else {
	    assertEquals(exp, value);
	}
    }

    public void setUp() {
	P6Options.propertiesLastModified = 0;
    }
}
