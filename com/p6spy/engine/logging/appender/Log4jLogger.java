package com.p6spy.engine.logging.appender;

import org.apache.log4j.*;
import java.io.StringWriter;
import java.io.PrintWriter;
import com.p6spy.engine.common.*;

// adapted from Rafael Alvarez's LoggingStream class
public class Log4jLogger extends FormattedLogger implements P6Logger {
    
    protected Level level = Level.INFO;
    protected String lastEntry;
    private static Logger log;

    // By configuring log4j by this method, we control the p6spy logger behavior
    // using the same configuration file as p6spy, and any other attempt
    // to configure log4j will add to this configuration.
    public Log4jLogger() {
      PropertyConfigurator conf=new PropertyConfigurator();
      conf.configure(P6Util.loadProperties(P6SpyOptions.SPY_PROPERTIES_FILE));
      log = Logger.getLogger("p6spy");
      log.setAdditivity(false);
    }


    public void logException(Exception e) {
	StringWriter sw = new StringWriter();
	PrintWriter pw  = new PrintWriter(sw);
	e.printStackTrace(pw);
	logText(sw.toString());
    }

    public void logText(String text) {
	log.log(this.level, text);
	setLastEntry(text);
    }

    public Level getLevel() {
	return this.level;
    }

    public void setLevel(Level inVar) {
	this.level = inVar;
    }

}

