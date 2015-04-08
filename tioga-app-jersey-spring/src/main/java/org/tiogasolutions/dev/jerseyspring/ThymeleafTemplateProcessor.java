package org.tiogasolutions.dev.jerseyspring;

import java.io.*;
import java.io.File;import java.io.IOException;import java.io.OutputStream;import java.lang.*;import java.lang.Override;import java.lang.String;import java.util.*;
import java.util.HashMap;import java.util.Locale;import java.util.Map;import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.*;
import javax.servlet.http.HttpServletRequest;import javax.servlet.http.HttpServletResponse;import javax.ws.rs.core.*;
import javax.ws.rs.core.MediaType;import javax.ws.rs.core.MultivaluedMap;import org.glassfish.jersey.server.mvc.Viewable;
import org.glassfish.jersey.server.mvc.spi.TemplateProcessor;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

public class ThymeleafTemplateProcessor implements TemplateProcessor<String> {

  private static TemplateEngine templateEngine;
  static {
    ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver();
    templateResolver.setTemplateMode("XHTML");
    templateResolver.setCacheTTLMs(0L);

    templateEngine = new TemplateEngine();
    templateEngine.setTemplateResolver(templateResolver);
  }

  private HttpServletRequest request;
  private HttpServletResponse response;
  private ServletContext servletContext;

  @Inject
  public ThymeleafTemplateProcessor(ServletContext servletContext, HttpServletRequest request, HttpServletResponse response) {
    this.request = request;
    this.response = response;
    this.servletContext = servletContext;
  }

  @java.lang.Override
  public String resolve(final String name, final MediaType mediaType) {
    String path = name;

    if (path.startsWith("/") == false) {
      path = "/"+path;
    }

    path = servletContext.getRealPath(path);
    File file = new File(path);
    boolean exists = file.exists();

    return exists ? name : null;
  }

  @Override
  public void writeTo(String templateReference, Viewable viewable, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream out) throws IOException {
    if (templateReference.startsWith("/") == false) {
      templateReference = "/"+templateReference;
    }

    Map<String,Object> variables = new HashMap<>();
    variables.put("it", viewable.getModel());

    WebContext webContext = new WebContext(request, response, servletContext, Locale.getDefault(), variables);
    String output = templateEngine.process(templateReference, webContext);
    out.write(output.getBytes());
  }
}
