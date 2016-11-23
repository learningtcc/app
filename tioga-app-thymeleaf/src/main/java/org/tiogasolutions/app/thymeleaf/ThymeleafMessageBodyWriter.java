package org.tiogasolutions.app.thymeleaf;

import org.springframework.beans.factory.annotation.Autowired;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.MessageBodyWriter;
import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

public class ThymeleafMessageBodyWriter implements MessageBodyWriter<ThymeleafContent> {

  @Context
  private UriInfo uriInfo;

  private final TemplateEngine engine;

  @Autowired
  public ThymeleafMessageBodyWriter(ThymeleafMessageBodyWriterConfig config) {
    ClassPathTemplateResolver templateResolver = new ClassPathTemplateResolver();
    templateResolver.setTemplateMode("HTML5");

    templateResolver.setSuffix(config.getPathSuffix());
    templateResolver.setPrefix(config.getPathPrefix());
    templateResolver.setCacheable(config.isCacheable());

    engine = new TemplateEngine();
    engine.setTemplateResolver(templateResolver);
    engine.addDialect(new Java8TimeDialect());
  }

  @Override
  public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
    return ThymeleafContent.class.equals(type);
  }

  @Override
  public long getSize(ThymeleafContent thymeleaf, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
    return -1;
  }

  public String getBaseUri() {
    return uriInfo.getBaseUri().toASCIIString();
  }

  @Override
  public void writeTo(ThymeleafContent thymeleaf, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
    writeTo(thymeleaf, entityStream);
  }

  /**
   * Provided mainly for testing, writes the thymeleaf to the specified writer.
   * @param thymeleaf the thymeleaf instanace to be rendered
   * @param writer the writer that the thymeleaf will be rendered to
   * @throws IOException if we are having a bad day
   */
  public void writeTo(ThymeleafContent thymeleaf, Writer writer) throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    writeTo(thymeleaf, out);
    String text = new String(out.toByteArray(), Charset.forName("UTF-8"));
    writer.write(text);
  }

  /**
   * Writes the thymeleaf to the specified writer.
   * @param thymeleaf the thymeleaf instanace to be rendered
   * @param outputStream the output stream that the thymeleaf will be rendered to
   * @throws IOException if we are having a bad day
   */
  public void writeTo(ThymeleafContent thymeleaf, OutputStream outputStream) throws IOException {
    String view = thymeleaf.getView();

    org.thymeleaf.context.Context context = new org.thymeleaf.context.Context();
    context.setVariables(thymeleaf.getVariables());

    String contextRoot = getContextRoot(uriInfo);
    context.setVariable("contextRoot", contextRoot);

    StringWriter writer = new StringWriter();
    engine.process(view, context, writer);

    String content = writer.toString();
    outputStream.write(content.getBytes());
  }

  public static String getContextRoot(UriInfo uriInfo) {
    if (uriInfo == null) return "/";
    String path = uriInfo.getBaseUri().toASCIIString();
    path = path.substring(0, path.length()-1);
    return path.trim();
  }
}
