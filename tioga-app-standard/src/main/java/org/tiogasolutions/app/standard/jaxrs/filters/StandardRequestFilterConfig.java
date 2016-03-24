package org.tiogasolutions.app.standard.jaxrs.filters;

public class StandardRequestFilterConfig {

  private String unauthorizedQueryParamName;
  private String unauthorizedQueryParamValue;
  private boolean sessionRequired;
  private String authenticationScheme;
  private String unauthorizedPath;
  private boolean redirectUnauthorized;

  public StandardRequestFilterConfig() {
  }

  public String getUnauthorizedQueryParamName() {
    return unauthorizedQueryParamName;
  }

  public void setUnauthorizedQueryParamName(String unauthorizedQueryParamName) {
    this.unauthorizedQueryParamName = unauthorizedQueryParamName;
  }

  public String getUnauthorizedQueryParamValue() {
    return unauthorizedQueryParamValue;
  }

  public void setUnauthorizedQueryParamValue(String unauthorizedQueryParamValue) {
    this.unauthorizedQueryParamValue = unauthorizedQueryParamValue;
  }

  public boolean isSessionRequired() {
    return sessionRequired;
  }

  public void setSessionRequired(boolean sessionRequired) {
    this.sessionRequired = sessionRequired;
  }

  public String getAuthenticationScheme() {
    return authenticationScheme;
  }

  public void setAuthenticationScheme(String authenticationScheme) {
    this.authenticationScheme = authenticationScheme;
  }

  public String getUnauthorizedPath() {
    return unauthorizedPath;
  }

  public void setUnauthorizedPath(String unauthorizedPath) {
    this.unauthorizedPath = unauthorizedPath;
  }

  public boolean isRedirectUnauthorized() {
    return redirectUnauthorized;
  }

  public void setRedirectUnauthorized(boolean redirectUnauthorized) {
    this.redirectUnauthorized = redirectUnauthorized;
  }
}
