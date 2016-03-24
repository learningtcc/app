package org.tiogasolutions.app.standard.session;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.NewCookie;

public class NoSessionSessionStore implements SessionStore {

    @Override
    public String getCookieName() {
        return "session-id";
    }

    @Override
    public Session newSession() {
        return null;
    }

    @Override
    public void remove(String sessionId) {
        // uhh.. sure.
    }

    @Override
    public void remove(Cookie cookie) {
        // uhh.. sure.
    }

    @Override
    public Session getSession(ContainerRequestContext requestContext) {
        return null;
    }

    @Override
    public NewCookie newSessionCookie(ContainerRequestContext requestContext) {
        return new NewCookie(getCookieName(), "no-cookie", "/", null, null, 0, true, true);
    }
}
