package org.tiogasolutions.app.common;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.LogbackException;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;
import org.slf4j.ILoggerFactory;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.tiogasolutions.dev.common.EnvUtils;
import org.tiogasolutions.dev.common.LogbackUtils;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.slf4j.LoggerFactory.getLogger;

public abstract class AppUtils {

  public static void initLogback(Level level) {
    LogbackUtils.initLogback(level);
  }

  public static Path initLogback(Path configDir, String propertyName, String fileName) {
    return LogbackUtils.initLogback(configDir, propertyName, fileName);
  }

  public static void setLogLevel(Level level, Class<?>...types) {
    for (Class<?> type : types) {
      ((ch.qos.logback.classic.Logger) getLogger(type)).setLevel(level);
    }
  }
}
