package org.tiogasolutions.dev.jerseyspring;

import org.glassfish.jersey.server.mvc.Viewable;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * The CustomJsp* Jersey classes exist only to support custom jsp suffix (such as jspf).
 */
final class TiogaRequestDispatcherWrapper implements RequestDispatcher {

  final static String BASE_PATH_ATTRIBUTE_NAME = "_basePath";
  final static String OLD_MODEL_ATTRIBUTE_NAME = "it";
  final static String MODEL_ATTRIBUTE_NAME = "model";
  final static String REQUEST_ATTRIBUTE_NAME = "_request";
  final static String RESPONSE_ATTRIBUTE_NAME = "_response";

  private final RequestDispatcher dispatcher;

  private final String basePath;

  private final Viewable viewable;

  /**
   * Creates new {@code RequestDispatcherWrapper} responsible for setting request attributes and forwarding the processing to
   * the given dispatcher.
   *
   * @param dispatcher dispatcher processing the request after all the request attributes were set.
   * @param basePath base path of all JSP set to {@value #BASE_PATH_ATTRIBUTE_NAME} request attribute.
   * @param viewable viewable to obtain model and resolving class from.
   */
  public TiogaRequestDispatcherWrapper(
    final RequestDispatcher dispatcher, final String basePath, final Viewable viewable) {
    this.dispatcher = dispatcher;
    this.basePath = basePath;
    this.viewable = viewable;
  }

  @Override
  public void forward(final ServletRequest request, final ServletResponse response) throws ServletException, IOException {
    final Object oldIt = request.getAttribute(MODEL_ATTRIBUTE_NAME);

    request.setAttribute(OLD_MODEL_ATTRIBUTE_NAME, viewable.getModel());
    request.setAttribute(MODEL_ATTRIBUTE_NAME, viewable.getModel());

    request.setAttribute(BASE_PATH_ATTRIBUTE_NAME, basePath);
    request.setAttribute(REQUEST_ATTRIBUTE_NAME, request);
    request.setAttribute(RESPONSE_ATTRIBUTE_NAME, response);

    dispatcher.forward(request, response);

    request.setAttribute(MODEL_ATTRIBUTE_NAME, oldIt);
  }

  @Override
  public void include(final ServletRequest request, final ServletResponse response) throws ServletException, IOException {
    throw new UnsupportedOperationException();
  }

}