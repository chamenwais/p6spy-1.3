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
 * Description: Utility classes
 *
 * $Author$
 * $Revision$
 * $Date$
 *
 * $Id$
 * $Log$
 * Revision 1.1  2002/04/07 04:52:26  jeffgoke
 * Initial revision
 *
 * Revision 1.4  2001-08-05 09:16:03-05  andy
 * final version on the website
 *
 * Revision 1.3  2001-08-02 07:52:43-05  andy
 * <>
 *
 * Revision 1.2  2001-07-30 23:37:24-05  andy
 * <>
 *
 * Revision 1.1  2001-07-30 23:03:32-05  andy
 * <>
 *
 * Revision 1.0  2001-07-30 17:49:09-05  andy
 * Initial revision
 *
 */

package com.p6spy.engine.spy;

import java.sql.*;
import java.io.*;
import java.util.*;

public class P6Util {
    public final static boolean isTrue(String s) {
        return(s != null && (s.equals("1") || s.equalsIgnoreCase("true")));
    }

    public final static int atoi(Object s) {
        int i = 0;

        if (s != null) {
            String n = s.toString();
            int dot = n.indexOf('.');
            if (dot != -1) {
                n = n.substring(0,dot);
            }

            try {
                i = new Integer(n).intValue();
            } catch (NumberFormatException e) {
                i = 0;
            }
        }

        return(i);
    }

    public static final Properties loadProperties (String file) {
        Properties props = new Properties();
        try {
            String path = classPathFile(file);
            if (path == null) {
                warn("Can't find " + file + ", java.class.path = <" + System.getProperty("java.class.path") + ">");
            } else {
                FileInputStream in = new FileInputStream(path);
                props.load(in);
                in.close();
            }
        } catch (FileNotFoundException e1) {
            warn("Can't find find " + file + " " + e1);
        } catch (IOException e2) {
            warn("IO Error reading file " + file + " " + e2);
        }
        return props;
    }


    /**
     * Here we attempt to find the file in the current dir and the classpath
     * If we can't find it then we return null
     */
    public static final String classPathFile(String file) {
        File fp             = null;
        String path         = null;
        String separator    = System.getProperty("path.separator");
        String slash        = System.getProperty("file.separator");
        String classpath    = "." + separator + System.getProperty("java.class.path");
        String local        = System.getProperty("p6.home");

        if (local != null) {
            classpath = local + separator + classpath;
        }
        StringTokenizer tok = new StringTokenizer(classpath, separator);

        do {
            String dir = tok.nextToken();
            path = dir.equals(".") ? file : dir + slash + file;
            fp = new File(path);
        } while (!fp.exists() && tok.hasMoreTokens());

        return fp.exists() ? path : null;
    }

    public static final void warn(String s) {
        System.err.println("Warning: " + s);
    }

    public static final void checkJavaProperties() {
        Collection list = P6SpyOptions.getOptions();
        Iterator it = list.iterator();

        while (it.hasNext()) {
            String opt = (String) it.next();
            String value = System.getProperty("p6" + opt);

            if (value != null) {
                P6SpyOptions.set(opt,value);
            }
        }
    }

    public final static java.util.Date timeNow() {
        return(new java.util.Date());
    }

    public static final PrintStream getPrintStream(String file, boolean append) throws IOException {
        PrintStream stream = null;
        FileOutputStream  fw  = new FileOutputStream(file, append);
        stream = new PrintStream(fw, P6SpyOptions.getAutoflush());
        return(stream);
    }
}
