package com.p6spy.engine.logging.appender;

public interface P6Logger {
    public void logSQL(int connectionId, String now, long elapsed, String category, String prepared, String sql);
    public void logException (Exception e);
    public void logText (String text);
    public String getLastEntry();
}
