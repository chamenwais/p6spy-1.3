package com.p6spy.engine.logging.appender;

import java.io.*;

public class FileLogger extends StdoutLogger {

    public FileLogger() {
	//this("spy.log");
    }

    public void setLogfile(String fileName) {
	try {
	    qlog = new PrintStream(new FileOutputStream(fileName));
	} catch (IOException e) {
	    e.printStackTrace(System.err);
	}
    }
}

