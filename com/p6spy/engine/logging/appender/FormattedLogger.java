package com.p6spy.engine.logging.appender;

// log4j, stdout, and file logger all use the same format
// so we descend from this class.
// also, they all have to do the get and setLastEntry stuff
// so we go ahead and do the work here.
public abstract class FormattedLogger {
    protected String lastEntry;

    public void logSQL(int connectionId, String now, long elapsed, String category, String prepared, String sql) {
	String logEntry = now + "|"+ elapsed + "|"+(connectionId==-1 ? "" : String.valueOf(connectionId))+"|"+category+"|"+prepared+"|"+sql;
	logText(logEntry);
    }

    public abstract void logText(String text);

    // they also all need to have the last entry thing
    public void setLastEntry(String inVar) {
	lastEntry = inVar;
    }

    public String getLastEntry() {
	return lastEntry;
    }
}

