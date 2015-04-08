package org.tiogasolutions.app.logging;

import java.io.*;
import org.apache.log4j.*;

public class LogUtils {

  public static final Level DEFAULT_LEVEL = Level.WARN;
  public static final String DEFAULT_PATTERN = "%-5p: %d{MM-dd-yy HH:mm:ss} [%t] %c %x- %m%n";

  public void initAppenders(File logFile) {
    initConsoleAppender(DEFAULT_LEVEL, DEFAULT_PATTERN);
    initDailyRollingFileAppender(DEFAULT_LEVEL, DEFAULT_PATTERN, logFile);
  }

  public void initConsoleAppender() {
    initConsoleAppender(DEFAULT_LEVEL, DEFAULT_PATTERN);
  }

  public void initConsoleAppender(Level level, String pattern) {

    ConsoleAppender consoleAppender = new ConsoleAppender(new PatternLayout(pattern));
    consoleAppender.setThreshold(level);
    Logger.getRootLogger().addAppender(consoleAppender);

    String msg = String.format("%s configured with log level %s.", ConsoleAppender.class.getSimpleName(), level);
    info(LogUtils.class, msg);
  }

  public void initDailyRollingFileAppender(File logFile) {
    initDailyRollingFileAppender(DEFAULT_LEVEL, DEFAULT_PATTERN, logFile);
  }

  public void initDailyRollingFileAppender(Level level, String pattern, File logFile) {
    try {
      if (logFile.getParentFile().exists()) {
        DailyRollingFileAppender fileAppender = new DailyRollingFileAppender(new PatternLayout(pattern), logFile.getAbsolutePath(), "yyyy-MM-dd");
        fileAppender.setThreshold(level);
        Logger.getRootLogger().addAppender(fileAppender);

        String msg = String.format("%s configured with log level %s appending to %s.", DailyRollingFileAppender.class.getSimpleName(), level, logFile.getAbsolutePath());
        info(LogUtils.class, msg);

      } else {
        String msg = String.format("Unable to configure %s, file not found: %s", DailyRollingFileAppender.class.getSimpleName(), logFile.getParentFile().getAbsolutePath());
        fatal(LogUtils.class, msg);
      }

    } catch (IOException ex) {
      fatal(LogUtils.class, "Unable to configure " + DailyRollingFileAppender.class.getSimpleName(), ex);
    }
  }

  public void info(Class type, String msg) {
    Logger.getLogger(type).info(msg);
  }
  public void info(Class type, String msg, Throwable ex) {
    Logger.getLogger(type).info(msg, ex);
  }

  public void warn(Class type, String msg) {
    Logger.getLogger(type).warn(msg);
  }
  public void warn(Class type, String msg, Throwable ex) {
    Logger.getLogger(type).warn(msg, ex);
  }

  public void fatal(Class type, String msg) {
    Logger.getLogger(type).fatal(msg);
  }
  public void fatal(Class type, String msg, Throwable ex) {
    Logger.getLogger(type).fatal(msg, ex);
  }

  public void trace(Class type, String msg) {
    Logger.getLogger(type).trace(msg);
  }
  public void trace(Class type, String msg, Throwable ex) {
    Logger.getLogger(type).trace(msg, ex);
  }

  public void debug(Class type, String msg) {
    Logger.getLogger(type).debug(msg);
  }
  public void debug(Class type, String msg, Throwable ex) {
    Logger.getLogger(type).debug(msg, ex);
  }
}
