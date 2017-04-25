package org.tiogasolutions.app.common;

import ch.qos.logback.classic.Level;
import org.tiogasolutions.dev.common.LogbackUtils;

import java.nio.file.Path;

import static org.slf4j.LoggerFactory.getLogger;

public abstract class AppUtils {

    public static void initLogback(Level level) {
        LogbackUtils.initLogback(level);
    }

    @Deprecated
    public static Path initLogback(Path configDir, String propertyName, String fileName) {
        return LogbackUtils.initLogback(configDir, propertyName, fileName);
    }

    public static Path initLogback(Level level, Path configDir, String propertyName, String fileName) {
        return LogbackUtils.initLogback(level, configDir, propertyName, fileName);
    }

    public static void setLogLevel(Level level, Class<?>... types) {
        for (Class<?> type : types) {
            ((ch.qos.logback.classic.Logger) getLogger(type)).setLevel(level);
        }
    }

    public static void setLogLevel(Level level, String... names) {
        for (String name : names) {
            ((ch.qos.logback.classic.Logger) getLogger(name)).setLevel(level);
        }
    }
}
