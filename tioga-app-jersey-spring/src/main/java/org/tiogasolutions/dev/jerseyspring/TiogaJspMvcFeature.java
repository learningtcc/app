package org.tiogasolutions.dev.jerseyspring;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import org.glassfish.jersey.server.mvc.MvcFeature;

@ConstrainedTo(RuntimeType.SERVER)
public class TiogaJspMvcFeature implements Feature {

  public static final String SUPPORTED_EXTENSIONS = "org.tiogasolutions.dev.jersey-spring.supportedExtensions";

  @Override
  public boolean configure(FeatureContext context) {

    final Configuration config = context.getConfiguration();

    if (config.isRegistered(TiogaJspTemplateProcessor.class) == false) {
      context.register(TiogaJspTemplateProcessor.class);

      if (config.isRegistered(MvcFeature.class) == false) {
        context.register(MvcFeature.class);
      }

      return true;
    }

    return false;
  }
}
