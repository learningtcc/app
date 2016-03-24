package org.tiogasolutions.app.standard.readers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiogasolutions.dev.common.IoUtils;
import org.tiogasolutions.dev.common.exceptions.ApiException;

import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.io.InputStream;

public class BundledStaticContentReader implements StaticContentReader {

  private static final Logger logger = LoggerFactory.getLogger(StaticContentReader.class);

  private final String classPathRoot;

  public BundledStaticContentReader(String classPathRoot) {
    this.classPathRoot = classPathRoot;
  }

  public byte[] readContent(UriInfo uriInfo) {
    String contentPath = uriInfo.getPath();
    return readContent(contentPath);
  }

  public byte[] readContent(String contentPath) {

    String resource;

    if (contentPath.startsWith("/")) {
      resource = classPathRoot+contentPath;
    } else {
      resource = classPathRoot + "/" + contentPath;
    }

    try {
      InputStream in = getClass().getResourceAsStream(resource);

      if (in == null && getClass().getResource(classPathRoot) == null) {
        String msg = String.format("Content root not found (%s). Build project to update resources.", classPathRoot);
        throw ApiException.notFound(msg);

      } else if (in == null) {
        String msg = String.format("The resource was not found: %s", contentPath);
        throw ApiException.notFound(msg);
      }

      return IoUtils.toBytes(in);

    } catch (IOException e) {
      throw ApiException.internalServerError("Error reading embedded static content: " + resource);
    }
  }
}
