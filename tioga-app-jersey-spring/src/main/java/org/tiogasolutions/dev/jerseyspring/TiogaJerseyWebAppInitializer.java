package org.tiogasolutions.dev.jerseyspring;

import org.tiogasolutions.dev.common.ReflectUtils;
import org.tiogasolutions.dev.common.StringUtils;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.servlet.ServletProperties;
import org.springframework.core.annotation.Order;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.*;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

// Order required to ensure this initializer is run before Jersey 2.x Spring WebApplicationInitializer
@Order(0)
public abstract class TiogaJerseyWebAppInitializer implements WebApplicationInitializer {

  /**
   * Identifies the Jersey ResourceConfig for this app
   * @param servletContext the servlet context for this app
   * @param appContext the application context for this app
   * @return the ResourceConfig for this app
   */
  public abstract Class<? extends ResourceConfig> getApplicationClass(ServletContext servletContext, WebApplicationContext appContext);

  /**
   * Defines the name of the system property that is used to
   * identify the application's current environment.
   * @param servletContext the servlet context for this app
   * @param appContext the application context for this app
   * @return the property name
   */
  public abstract String getEnvironmentPropertyName(ServletContext servletContext, WebApplicationContext appContext);

  /**
   * Defines the name of the system property that is used to
   * override the list of spring profiles for this application.
   * @param servletContext the servlet context for this app
   * @param appContext the application context for this app
   * @return the property name
   */
  protected abstract String getProfilesPropertyName(ServletContext servletContext, WebApplicationContext appContext);

  /**
   * Defines the spring config classes to use for initialization - return null if a Spring XML file is used
   * @param servletContext the servlet context for this app
   * @return the Spring configuration class.
   */
  protected abstract Class<?>[] getSpringConfigClasses(ServletContext servletContext);

  /**
   * Defines the spring xml file to use for initialization - return null if Spring Config classes are used.
   * @param servletContext the servlet context for this app
   * @return the location of the Spring configuration file.
   */
  protected abstract String getSpringConfigLocation(ServletContext servletContext);

  public TiogaJerseyWebAppInitializer() {
  }

  @Override
  public void onStartup(ServletContext servletContext) throws ServletException {

    WebApplicationContext appContext = createWebApplicationContext(servletContext);

    addListeners(servletContext, appContext);

    addFilters(servletContext, appContext);

    addServlets(servletContext, appContext);
  }

  protected void addFilters(ServletContext servletContext, WebApplicationContext appContext) {

    // Spring security filter chain
    addSpringSecurityFilter(servletContext, appContext);

    addJerseyFilter(servletContext, appContext);
  }

  protected void addListeners(ServletContext servletContext, WebApplicationContext appContext) {
    // Add context listener with refreshed appContext
    servletContext.addListener(new ContextLoaderListener(appContext));
  }

  protected WebApplicationContext createWebApplicationContext(ServletContext servletContext) {

    Class<?>[] configClasses = getSpringConfigClasses(servletContext);
    String configLocation = getSpringConfigLocation(servletContext);

    // We can only use one or the other...
    AbstractRefreshableWebApplicationContext appContext;

    if (StringUtils.isNotBlank(configLocation) && configClasses != null) {
      String msg = String.format("Only XML or Annotation configuration can be used but not both.");
      throw new IllegalArgumentException(msg);

    } else if (configClasses != null) {
      appContext = new AnnotationConfigWebApplicationContext();
      ((AnnotationConfigWebApplicationContext)appContext).register(configClasses);

    } else if (StringUtils.isNotBlank(configLocation)) {
      appContext = new XmlWebApplicationContext();
      appContext.setConfigLocation(configLocation);

    } else {
      String msg = String.format("Either an XML or Annotation configuration can be specified.");
      throw new IllegalArgumentException(msg);
    }

    String[] profiles = getSpringProfiles(servletContext, appContext);
    if (profiles != null && profiles.length > 0) {
      appContext.getEnvironment().setActiveProfiles(profiles);
    }

    // Refresh the spring context -- important, Jersey 2.x
    // Spring integration will not work with out it.
    appContext.setServletContext(servletContext);
    appContext.refresh();

    return appContext;
  }

  /**
   * Configures the Spring Security filter
   * @param servletContext the servlet context for this app
   * @param appContext the application context for this app
   */
  protected void addSpringSecurityFilter(ServletContext servletContext, WebApplicationContext appContext) {
    FilterRegistration.Dynamic securityFilter = servletContext.addFilter("springSecurityFilterChain", DelegatingFilterProxy.class);
    securityFilter.addMappingForUrlPatterns(null, false, "/*");
  }

  /**
   * Configures the Jersey filter
   * @param servletContext the servlet context for this app
   * @param appContext the application context for this app
   */
  protected void addJerseyFilter(ServletContext servletContext, WebApplicationContext appContext) {

    servletContext.setInitParameter("contextConfigLocation", ""); // Prevents Jersey Spring WebInitializer for do any work.

    FilterRegistration.Dynamic jerseyFilter = servletContext.addFilter("jersey-filter", ServletContainer.class);
    jerseyFilter.setInitParameter(ServletProperties.JAXRS_APPLICATION_CLASS, getApplicationClass(servletContext, appContext).getName());
    jerseyFilter.setInitParameter(ServletProperties.FILTER_FORWARD_ON_404, "true");
    jerseyFilter.setInitParameter(ServletProperties.FILTER_STATIC_CONTENT_REGEX, buildStaticContentRegex());
    jerseyFilter.addMappingForUrlPatterns(null, false, "/*");
  }

  protected String buildStaticContentRegex() {
    String staticPattern = "";
    for (String urlPattern: getStaticResourceUrlPatterns()) {
      staticPattern += (staticPattern.isEmpty()) ? urlPattern : "|"+urlPattern;
    }
    staticPattern = "([^\\s]+(\\.(?i)(" + staticPattern + "))$)";
    return staticPattern;
  }

  protected Collection<String> getStaticResourceUrlPatterns() {
    return Arrays.asList("html","jpg","png","gif","bmp","css","js","ico","pdf","txt");
  }

  protected void addServlets(ServletContext servletContext, WebApplicationContext appContext) {
    addJspfServlet(servletContext, appContext);
  }

  /**
   * Includes jspf as servlet
   * @param servletContext the servlet context for this app
   * @param appContext the application context for this app
   */
  protected void addJspfServlet(ServletContext servletContext, WebApplicationContext appContext) {
    servletContext.getServletRegistration("jsp").addMapping("*.jspf");
  }

  protected String[] getSpringProfiles(ServletContext servletContext, WebApplicationContext appContext) {

    String environmentName = System.getProperty(getEnvironmentPropertyName(servletContext, appContext), "null");
    String envProfileName = "env-"+environmentName;
    String defaultProfiles = StringUtils.toDelineatedString(",", "main", "live", envProfileName);

    String profilesString = System.getProperty(getProfilesPropertyName(servletContext, appContext), defaultProfiles);
    List<String> profiles = new ArrayList<>();

    for (String profile : profilesString.split(",")) {
      profiles.add(profile.trim());
    }

    return ReflectUtils.toArray(String.class, profiles);
  }
}