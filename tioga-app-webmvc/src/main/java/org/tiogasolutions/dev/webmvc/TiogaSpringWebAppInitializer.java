package org.tiogasolutions.dev.webmvc;

import java.util.*;
import javax.servlet.*;
import org.tiogasolutions.dev.common.*;
import org.springframework.core.annotation.Order;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.*;
import org.springframework.web.context.support.AbstractRefreshableWebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

// Order required to ensure this initializer is run before Jersey 2.x Spring WebApplicationInitializer
@Order(0)
public abstract class TiogaSpringWebAppInitializer implements WebApplicationInitializer {

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
  protected abstract java.lang.Class<?>[] getSpringConfigClasses(ServletContext servletContext);

  /**
   * Defines the spring xml file to use for initialization - return null if Spring Config classes are used.
   * @param servletContext the servlet context for this app
   * @return the location of the Spring configuration file.
   */
  protected abstract String getSpringConfigLocation(ServletContext servletContext);

  public TiogaSpringWebAppInitializer() {
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

  protected void addServlets(ServletContext servletContext, WebApplicationContext appContext) {
    addDispatcherServlet(servletContext, appContext);
    addJspfServlet(servletContext, appContext);
    registerStaticResourceUrlPatterns(servletContext, appContext);
  }

  /**
   * Registers the static resources that Jersey should not process.
   * @param servletContext the servlet context for this app
   * @param appContext the application context for this app
   */
  protected void registerStaticResourceUrlPatterns(ServletContext servletContext, WebApplicationContext appContext) {
    for (String urlPattern : getStaticResourceUrlPatterns()) {
      urlPattern = "*."+urlPattern;
      servletContext.getServletRegistration("default").addMapping(urlPattern);
    }
  }

  protected Collection<String> getStaticResourceUrlPatterns() {
    return Arrays.asList("html","jpg","png","gif","bmp","css","js","ico","pdf","txt");
  }

  protected void addDispatcherServlet(ServletContext servletContext, WebApplicationContext appContext) {
    ServletRegistration.Dynamic servlet = servletContext.addServlet("dispatcherServlet", new DispatcherServlet(appContext));
    servlet.addMapping("/");
    servlet.setLoadOnStartup(1);
  }

  /**
   * Configures the application to support JSP files.
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