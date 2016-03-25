package org.tiogasolutions.app.standard.jaxrs;

import org.springframework.beans.factory.annotation.Autowired;
import org.tiogasolutions.lib.jaxrs.providers.TiogaJaxRsExceptionMapper;
import org.tiogasolutions.notify.notifier.Notifier;

public class StandardJaxRsExceptionMapper extends TiogaJaxRsExceptionMapper {

  private final Notifier notifier;

  @Autowired
  public StandardJaxRsExceptionMapper(Notifier notifier) {
    this.notifier = notifier;
  }

  @Override
  protected void log5xxException(String msg, Throwable throwable) {
    super.log5xxException(msg, throwable);

    notifier.begin()
        .summary(msg)
        .exception(throwable)
        .topic("Unhandled")
        //.trait("http-status", unknown)
        .send();
  }
}
