package org.tiogasolutions.app.standard.readers;

import javax.ws.rs.core.UriInfo;

public class MockContentReader implements StaticContentReader {

  @Override
  public byte[] readContent(UriInfo uriInfo) {
    return "<html><body><h1>Hello World</h1><p>It's me, dummy site.</p></body></html>".getBytes();
  }

  @Override
  public byte[] readContent(String contentPath) {
    return "<html><body><h1>Hello World</h1><p>It's me, dummy site.</p></body></html>".getBytes();
  }
}
