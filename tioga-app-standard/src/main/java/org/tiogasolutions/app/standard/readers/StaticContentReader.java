package org.tiogasolutions.app.standard.readers;

import javax.ws.rs.core.UriInfo;

public interface StaticContentReader {
  byte[] readContent(UriInfo uriInfo);
  byte[] readContent(String contentPath);
}
