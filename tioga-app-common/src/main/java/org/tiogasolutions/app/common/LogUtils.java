package org.tiogasolutions.app.common;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.tiogasolutions.dev.common.EnvUtils;

import java.nio.file.Files;
import java.nio.file.Path;

public abstract class LogUtils {

  private static final org.slf4j.Logger log = LoggerFactory.getLogger(LogUtils.class);

  public static void initLogback(Level level) {

    // Reroute java.util.Logger to SLF4J
    SLF4JBridgeHandler.removeHandlersForRootLogger();
    SLF4JBridgeHandler.install();

    LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

    PatternLayoutEncoder ple = new PatternLayoutEncoder();
    String pattern = "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n";
    ple.setPattern(pattern);
    ple.setContext(context);
    ple.start();

    ConsoleAppender<ILoggingEvent> appender = new ConsoleAppender<>();
    appender.setName("STDOUT");
    appender.setContext(context);
    appender.setEncoder(ple);

    Logger root = (Logger)LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
    root.setLevel(level);
    root.addAppender(appender);

    log.info("Using default logback config: {} ({})", level, pattern);
  }

  public static void initLogback(Path configDir, String propertyName, String fileName) {

    // Configure everything to initially log to the console.
    initLogback(Level.WARN);

    if (configDir == null) {
      log.warn("Cannot initialize logging, the config directory was not specified.");
      return;
    } else if (Files.exists(configDir) == false) {
      log.warn("Cannot initialize logging, the config directory does not exist: {}", configDir);
      return;
    }

    String logConfigArg = EnvUtils.findProperty(propertyName, fileName);
    Path logConfigFile = configDir.resolve(logConfigArg);
    log.info("Configure logging from  {}", logConfigFile.toString());

    LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

    try {
      JoranConfigurator configurator = new JoranConfigurator();
      configurator.setContext(context);

      // Call context.reset() to clear any previous configuration, e.g. default
      // configuration. For multi-step configuration, omit calling context.reset().
      context.reset();
      configurator.doConfigure(logConfigFile.toString());

    } catch (JoranException ignored) {/* ignored */}

    StatusPrinter.printInCaseOfErrorsOrWarnings(context);
  }

/*
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
*/
}
