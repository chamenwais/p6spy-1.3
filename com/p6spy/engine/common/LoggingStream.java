package com.p6spy.engine.common;

import org.apache.log4j.*;

import java.io.OutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

/**
 * A PrintStream that outputs to a log4j logger called p6spy
 *
 * @author Rafael Alvarez
 * @version 1.0
 */
public class LoggingStream extends java.io.PrintStream {
   Level level= Level.INFO; //Set INFO as the default level
   private static Logger log;

   // By configuring log4j by this method, we control the p6spy logger behavior
   // using the same configuration file as p6spy, and any other atempt to configure
   // log4j will add to this configuration.
   static {
      PropertyConfigurator conf=new PropertyConfigurator();
      conf.configure(P6Util.loadProperties(P6SpyOptions.SPY_PROPERTIES_FILE));
      log = Logger.getLogger("p6spy");
      log.setAdditivity(false);
   }


   /**
    * Creates a LoggingStream associated with a given level
    */
   public LoggingStream(Level level) {
      super(System.out); // This is a dummy value.
      this.level = level;
      // By setting the additivity of this logger to false we get complete control
      // over the behavior of the p6spy logger.

   }

   /**
    * Creates a LoggingStream with a default level (INFO)
    */
   public LoggingStream() {
      super(System.out); // This is a dummy value.
   }

   public LoggingStream(OutputStream stream) {
      super(stream);
   }

   public int hashCode() {
      return super.hashCode();
   }

   public LoggingStream(OutputStream stream, boolean b) {
      super(stream, b);
   }

   public boolean equals(Object o) {
      return super.equals(o);
   }

   public void flush() {
      super.flush();
   }

   protected Object clone() throws CloneNotSupportedException {
      return super.clone();
   }

   public void close() {
      super.close();
   }

   public String toString() {
      return super.toString();
   }

   public boolean checkError() {
      return super.checkError();
   }

   protected void finalize() throws Throwable {
      super.finalize();
   }

   protected void setError() {
      super.setError();
   }

   public void write(int i) {
      if (level != null) {
         log.log(this.level, Integer.toString(i));
      } else {
         super.write(i);
      }
   }

   public void write(byte[] bytes) throws IOException {
      if (level != null) {
         log.log(this.level, new String(bytes));
      } else {
         super.write(bytes);
      }
   }

   public void write(byte[] bytes, int i, int i1) {
      if (level != null) {
         log.log(this.level, new String(bytes, i, i1));
      } else {
         super.write(bytes, i, i1);
      }
   }

   public void print(boolean b) {
      this.logObject(new Boolean(b));
   }

   public void print(char c) {
      this.logObject(new Character(c));
   }

   public void print(int i) {
      this.logObject(new Integer(i));
   }

   public void print(long l) {
      this.logObject(new Long(l));
   }

   public void print(float v) {
      this.logObject(new Float(v));
   }

   public void print(double v) {
      this.logObject(new Double(v));
   }

   public void print(char[] chars) {
      this.logObject(new String(chars));
   }

   public void print(String s) {
      this.logObject(s);
   }

   public void print(Object o) {
      this.logObject(o);
   }

   /**
    * Sends an object to the logger.
    * The leve assigned to this Stream is used as the leve to be logged.
    */
   public void logObject(Object o) {
      if (level != null) {
         log.log(this.level, o);
      } else {
         super.print(o);
      }
   }

   public void println() {
      this.print("");
   }

   public void println(boolean b) {
      this.print(b);
      this.println();
   }

   public void println(char c) {
      print(c);

   }

   public void println(int i) {
      print(i);

   }

   public void println(long l) {
      print(l);

   }

   public void println(float v) {
      print(v);

   }

   public void println(double v) {
      print(v);

   }

   public void println(char[] chars) {
      print(chars);

   }

   public void println(String s) {
      print(s);
   }

   public void println(Object o) {
      print(o);
   }
}
