package org.tiogasolutions.app.standard.session;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.NewCookie;

public interface SessionStore {

    Session newSession();

    String getCookieName();

    void remove(Cookie cookie);
    void remove(String sessionId);

    Session getSession(ContainerRequestContext requestContext);
    NewCookie newSessionCookie(ContainerRequestContext requestContext);

}
