package org.tiogasolutions.app.standard.jaxrs;

import org.springframework.beans.factory.annotation.Autowired;
import org.tiogasolutions.app.standard.jackson.StandardObjectMapper;
import org.tiogasolutions.lib.jaxrs.providers.TiogaReaderWriterProvider;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class StandardReaderWriterProvider extends TiogaReaderWriterProvider {

  @Autowired
  public StandardReaderWriterProvider(StandardObjectMapper objectMapper) {
    super(objectMapper);
  }
}
