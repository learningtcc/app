package org.tiogasolutions.app.thymeleaf;

public class ThymeleafMessageBodyWriterConfig {
  private String pathPrefix;
  private String pathSuffix;
  private boolean cacheable;

  public String getPathPrefix() {
    return pathPrefix;
  }

  public void setPathPrefix(String pathPrefix) {
    this.pathPrefix = pathPrefix;
  }

  public String getPathSuffix() {
    return pathSuffix;
  }

  public void setPathSuffix(String pathSuffix) {
    this.pathSuffix = pathSuffix;
  }

  public boolean isCacheable() {
    return cacheable;
  }

  public void setCacheable(boolean cacheable) {
    this.cacheable = cacheable;
  }
}
