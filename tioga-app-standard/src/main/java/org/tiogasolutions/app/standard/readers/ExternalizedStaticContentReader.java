package org.tiogasolutions.app.standard.readers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiogasolutions.dev.common.exceptions.ApiException;

import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ExternalizedStaticContentReader implements StaticContentReader {

  private static final Logger logger = LoggerFactory.getLogger(StaticContentReader.class);

  private final Path rootPath;

  public ExternalizedStaticContentReader(String path) {
    this(Paths.get(path));
  }

  public ExternalizedStaticContentReader(Path rootPath) {
    this.rootPath = rootPath;
    logger.info("Reading static resources from: " + rootPath);
  }

  public byte[] readContent(UriInfo uriInfo) {
    String contentPath = uriInfo.getPath();
    return readContent(contentPath);
  }

  public byte[] readContent(String contentPath) {
    contentPath = contentPath.startsWith("/") ? contentPath.substring(1) : contentPath;

    Path fullPath = rootPath.resolve(contentPath);

    try {
      return Files.readAllBytes(fullPath);
    } catch (IOException e) {
      throw ApiException.badRequest("Error reading externalized static content " + fullPath);
    }
  }
}
