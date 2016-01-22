package org.tiogasolutions.app.common;

import org.slf4j.LoggerFactory;
import org.tiogasolutions.dev.common.EnvUtils;
import org.tiogasolutions.dev.common.IoUtils;
import org.tiogasolutions.dev.common.StringUtils;
import org.tiogasolutions.dev.common.exceptions.ExceptionUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.String;import java.lang.System;import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AppPathResolver {

  private static final org.slf4j.Logger log = LoggerFactory.getLogger(AppUtils.class);

  private final String springPropertyName;
  private final String configPropertyName;
  private final String runtimePropertyName;
  private final String profilesPropertyName;

  public AppPathResolver(String prefix) {
    this.springPropertyName = prefix + "spring.config";
    this.configPropertyName = prefix + "config.dir";
    this.runtimePropertyName = prefix + "runtime.dir";
    this.profilesPropertyName = prefix + "active.profiles";
  }

  public Path requireRuntimePath() throws FileNotFoundException {
    return resolveRuntimePath("runtime", true);
  }

  public Path requireRuntimePath(String directoryName) throws FileNotFoundException {
    return resolveRuntimePath(directoryName, true);
  }

  public Path resolveRuntimePath() throws FileNotFoundException {
    return resolveRuntimePath("runtime");
  }

  public Path resolveRuntimePath(String directoryName) throws FileNotFoundException {
    return resolveRuntimePath(directoryName, false);
  }

  public Path resolveRuntimePath(String directoryName, boolean required) throws FileNotFoundException {
    ExceptionUtils.assertNotNull(directoryName, "directoryName");

    // Identify runtimeDir from system property or program argument
    String runtimeDirArg = EnvUtils.findProperty(runtimePropertyName);

    if (runtimeDirArg == null) {
      // Look for a directory named "runtime" near the current directory -- useful for development
      File currentDir = IoUtils.currentDir();
      File moduleDir = IoUtils.findDirNear(currentDir, directoryName);

      if (moduleDir == null) {
        if (required == false) {
          return null;
        }
        String msg = String.format("The runtime directory (%s) was not found relative to the current directory (%s). Please change the working directory or specify the system property or environment variable %s.", directoryName, currentDir, runtimePropertyName);
        throw new FileNotFoundException(msg);
      }

      runtimeDirArg = moduleDir.getAbsolutePath();
    }

    Path runtimeDir = Paths.get(runtimeDirArg).toAbsolutePath();

    // Verify the runtimeDir
    if (Files.notExists(runtimeDir)) {
      if (required == false) {
        return null;
      }
      String msg = String.format("Runtime directory %s does not exist", runtimeDir);
      throw new FileNotFoundException(msg);

    } else if (Files.isDirectory(runtimeDir) == false) {
      String msg = String.format("Runtime directory %s is not a directory", runtimeDir);
      throw new IllegalArgumentException(msg);
    }

    System.setProperty(runtimePropertyName, runtimeDir.toString());
    log.info("Runtime dir: {}", runtimeDir);

    return runtimeDir;
  }

  public Path requireConfigDir(Path runtimeDir) throws FileNotFoundException {
    return resolveConfigDir(runtimeDir, "config", true);
  }

  public Path requireConfigDir(Path runtimeDir, String directoryName) throws FileNotFoundException {
    return resolveConfigDir(runtimeDir, directoryName, true);
  }

  public Path resolveConfigDir(Path runtimeDir) throws FileNotFoundException {
    return resolveConfigDir(runtimeDir, "config", false);
  }

  public Path resolveConfigDir(Path runtimeDir, String directoryName) throws FileNotFoundException {
    return resolveConfigDir(runtimeDir, directoryName, false);
  }

  public Path resolveConfigDir(Path runtimeDir, String directoryName, boolean required) throws FileNotFoundException {
    ExceptionUtils.assertNotNull(directoryName, "directoryName");

    if (runtimeDir == null || Files.exists(runtimeDir) == false) {
      // Cannot find the config directory without a runtime directory.
      if (required == false) {
        return null;
      }
      throw new IllegalArgumentException("Runtime directory required to resolve config directory.");
    }

    Path configDir = runtimeDir.resolve(directoryName);

    if (Files.notExists(configDir)) {
      if (required == false) {
        return null;
      }
      String msg = String.format("Config directory %s does not exist", configDir);
      throw new FileNotFoundException(msg);

    } else if (!Files.isDirectory(runtimeDir)) {
      String msg = String.format("Config directory %s is not a directory", configDir);
      throw new IllegalArgumentException(msg);
    }

    System.setProperty(configPropertyName, configDir.toString());
    log.info("Config dir: {}", configDir);

    return configDir;
  }

  public String resolveSpringPath(Path configDir, String internalFile) throws FileNotFoundException {
    return resolveSpringPath(configDir, "spring-config.xml", internalFile);
  }

  public String resolveSpringPath(Path configDir, String fileName, String internalFile) throws FileNotFoundException {
    ExceptionUtils.assertNotNull(fileName, "fileName");

    String springFileName = EnvUtils.findProperty(springPropertyName);
    String springConfigPath;

    if (springFileName != null) {

      Path springConfig = Paths.get(springFileName);

      if (springConfig.isAbsolute() == false) {
        if (configDir == null) {
          String msg = String.format("The config directory must be specified for relative spring files (%s).", springFileName);
          throw new IllegalArgumentException(msg);
        }
        springConfig = configDir.resolve(springFileName);
      }

      if (Files.notExists(springConfig)) {
        String msg = "The specified spring config file does not exist: " + springConfig;
        throw new FileNotFoundException(msg);

      } else {
        springConfigPath = springConfig.toUri().toString();
        log.info("Spring file: {}", springConfigPath);
        log.info("  Using the specified spring config file");
      }

    } else {
      Path springConfig = (configDir == null) ? null : configDir.resolve(fileName);

      if (configDir != null && springConfig.toFile().exists()) {
        springConfigPath = springConfig.toUri().toString();
        log.info("Spring file: {}", springConfigPath);
        log.info("  Using the external spring config file");

      } else {
        springConfigPath = internalFile;
        log.info("Spring file: {}", springConfigPath);
        log.info("  Using the internal spring config file");
        log.info("  Override by using the external spring config file: {}", springConfig);
        log.info("  Override by specifying the location of the external spring config file with the system property \"{}\"", springPropertyName);
      }
    }

    if (springConfigPath != null) {
      System.setProperty(springPropertyName, springConfigPath);
    }

    return springConfigPath;
  }

  public String resolveSpringProfiles() {
    return resolveSpringProfiles("hosted");
  }

  public String resolveSpringProfiles(String additionalProfiles) {

    String activeProfiles = EnvUtils.findProperty(profilesPropertyName, "");

    if (StringUtils.isNotBlank(activeProfiles)) activeProfiles += ",";
    activeProfiles += additionalProfiles;

    System.setProperty(profilesPropertyName, activeProfiles);
    log.info("Active spring profiles: {}", activeProfiles);

    return activeProfiles;
  }
}
