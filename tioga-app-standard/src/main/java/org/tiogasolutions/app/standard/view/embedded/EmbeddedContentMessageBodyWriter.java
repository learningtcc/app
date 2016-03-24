package org.tiogasolutions.app.standard.view.embedded;

import org.springframework.beans.factory.annotation.Autowired;
import org.tiogasolutions.app.standard.readers.StaticContentReader;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.MessageBodyWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

public class EmbeddedContentMessageBodyWriter implements MessageBodyWriter<EmbeddedContent> {

  @Context UriInfo uriInfo;

  @Autowired
  private StaticContentReader contentReader;

  public EmbeddedContentMessageBodyWriter() {
  }

  @Override
  public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
    return EmbeddedContent.class.equals(type);
  }

  @Override
  public long getSize(EmbeddedContent embeddedContent, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
    return -1;
  }

  @Override
  public void writeTo(EmbeddedContent embeddedContent, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
    byte[] bytes = contentReader.readContent(embeddedContent.getView());
    entityStream.write(bytes);
  }
}
