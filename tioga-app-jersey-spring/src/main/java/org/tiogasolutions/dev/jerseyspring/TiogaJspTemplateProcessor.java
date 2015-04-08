package org.tiogasolutions.dev.jerseyspring;

import java.io.*;
import javax.inject.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.ws.rs.core.*;
import org.glassfish.jersey.internal.util.collection.Ref;
import org.glassfish.jersey.message.internal.TracingLogger;
import org.glassfish.jersey.server.*;
import org.glassfish.jersey.server.mvc.Viewable;
import org.glassfish.jersey.server.mvc.jsp.internal.LocalizationMessages;
import org.glassfish.jersey.server.mvc.spi.AbstractTemplateProcessor;

/**
 * The CustomJsp* Jersey classes exist only to support custom jsp suffix (such as jspf).
 */
public class TiogaJspTemplateProcessor extends AbstractTemplateProcessor<String> {

  /**
   * Determines the supported extensions by looking at the CustomJspMvcFeature.SUPPORTED_EXTENSIONS
   * property in the Jersey configuration. If none found uses "jsp".
   * @param config
   * @return
   */
  private static String[] determineSupportedExtensions(Configuration config) {
    Object supportedExtensionProperty = config.getProperty(TiogaJspMvcFeature.SUPPORTED_EXTENSIONS);

    // If nothing identified in config use defaults.
    if (supportedExtensionProperty == null) {
      return new String[]{"jsp"};
    } else {
      String[] extensions = supportedExtensionProperty.toString().split(",");//  StringUtil.tokenize(, ',');
      for (int i=0; i<extensions.length; i++) {
        extensions[i] = extensions[i].trim();
      }
      return extensions;
    }
  }

  @Inject
  private Provider<Ref<HttpServletRequest>> requestProviderRef;
  @Inject
  private Provider<Ref<HttpServletResponse>> responseProviderRef;
  @Inject
  private Provider<ContainerRequest> containerRequestProvider;

  /**
   * Create an instance of this processor with injected {@link javax.ws.rs.core.Configuration config} and
   * (optional) {@link javax.servlet.ServletContext servlet context}.
   *
   * @param config configuration to configure this processor from.
   * @param servletContext (optional) servlet context to obtain template resources from.
   */
  @Inject
  public TiogaJspTemplateProcessor(final Configuration config, final ServletContext servletContext) {
    super(config, servletContext, "jsp", determineSupportedExtensions(config));
  }

  @Override
  protected String resolve(final String templatePath, final Reader reader) throws Exception {
    return templatePath;
  }


  @Override
  public void writeTo(String templateReference, Viewable viewable, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, final OutputStream out) throws IOException {
    TracingLogger tracingLogger = TracingLogger.getInstance(containerRequestProvider.get().getPropertiesDelegate());
    if (tracingLogger.isLogEnabled(MvcJspEvent.JSP_FORWARD)) {
      tracingLogger.log(MvcJspEvent.JSP_FORWARD, templateReference, viewable.getModel());
    }

    final RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(templateReference);
    if (dispatcher == null) {
      throw new ContainerException(LocalizationMessages.NO_REQUEST_DISPATCHER_FOR_RESOLVED_PATH(templateReference));
    }

    final RequestDispatcher wrapper = new TiogaRequestDispatcherWrapper(dispatcher, getBasePath(), viewable);

    // OutputStream and Writer for HttpServletResponseWrapper.
    final ServletOutputStream responseStream = new ServletOutputStream() {
      @Override
      public boolean isReady() {
        return true;
      }
      @Override
      public void setWriteListener(WriteListener writeListener) {
        // new to Servlet 3.1, don't know what to put here.
      }
      @Override
      public void write(final int b) throws IOException {
        out.write(b);
      }
    };
    final PrintWriter responseWriter = new PrintWriter(new OutputStreamWriter(responseStream));

    try {
      wrapper.forward(requestProviderRef.get().get(), new HttpServletResponseWrapper(responseProviderRef.get().get()) {

        @Override
        public ServletOutputStream getOutputStream() throws IOException {
          return responseStream;
        }

        @Override
        public PrintWriter getWriter() throws IOException {
          return responseWriter;
        }
      });
    } catch (Exception e) {
      throw new ContainerException(e);
    } finally {
      responseWriter.flush();
    }
  }

  /**
   * MVC-JSP side tracing events.
   */
  private static enum MvcJspEvent implements TracingLogger.Event {
    JSP_FORWARD(TracingLogger.Level.SUMMARY, "MVC", "Forwarding view to JSP page [%s], model %s");

    private final TracingLogger.Level level;
    private final String category;
    private final String messageFormat;

    private MvcJspEvent(TracingLogger.Level level, String category, String messageFormat) {
      this.level = level;
      this.category = category;
      this.messageFormat = messageFormat;
    }

    @Override
    public String category() {
      return category;
    }

    @Override
    public TracingLogger.Level level() {
      return level;
    }

    @Override
    public String messageFormat() {
      return messageFormat;
    }
  }

}