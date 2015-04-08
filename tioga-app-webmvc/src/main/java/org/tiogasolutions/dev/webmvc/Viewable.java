package org.tiogasolutions.dev.webmvc;

import org.springframework.web.servlet.ModelAndView;

public class Viewable extends ModelAndView {

  public Viewable() {
  }

  public Viewable(String viewName) {
    super(viewName);
  }

  public Viewable(org.springframework.web.servlet.View view) {
    super(view);
  }

//  public Viewable(String viewName, Map<String, ?> model) {
//    super(viewName, model);
//  }

//  public Viewable(org.springframework.web.servlet.View view, Map<String, ?> model) {
//    super(view, model);
//  }

  public Viewable(String viewName, Object modelObject) {
    super(viewName, "it", modelObject);
  }

  public Viewable(org.springframework.web.servlet.View view, Object modelObject) {
    super(view, "it", modelObject);
  }
}
