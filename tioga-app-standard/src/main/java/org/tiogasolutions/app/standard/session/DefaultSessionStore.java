package org.tiogasolutions.app.standard.session;

import org.tiogasolutions.dev.common.ReflectUtils;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.UriInfo;
import java.util.HashMap;
import java.util.Map;

public class DefaultSessionStore implements SessionStore {

    private static final Object LOCK = new Object();

    private final long maxDuration;
    private final String cookieName;

    private final String path;
    private final String domain;
    private final String comment;
    private final boolean secure;
    private final boolean httpOnly;

    private Map<String, Session> map = new HashMap<>();

    /**
     * Creates a new session store
     * @param maxDuration the life of the session in milliseconds
     * @param cookieName the name of the cookie used to track a session
     */
    public DefaultSessionStore(long maxDuration, String cookieName) {
        this.maxDuration = maxDuration;
        this.cookieName = cookieName;

        this.path = "/";
        this.domain = null;
        this.comment = null;
        this.secure = true;
        this.httpOnly = true;
    }

    public DefaultSessionStore(long maxDuration, String cookieName, String path, String domain, String comment, boolean secure, boolean httpOnly) {
        this.maxDuration = maxDuration;
        this.cookieName = cookieName;
        this.path = path;
        this.domain = domain;
        this.comment = comment;
        this.secure = secure;
        this.httpOnly = httpOnly;
    }

    @Override
    public Session newSession() {
        removeExpiredSessions();
        synchronized (LOCK) {
            Session session = new Session(getMaxDuration());
            map.put(session.getSessionId(), session);
            return session;
        }
    }

    @Override
    public String getCookieName() {
        return cookieName;
    }

    public long getMaxDuration() {
        return maxDuration;
    }

    public String getPath() {
        return path;
    }

    public String getDomain() {
        return domain;
    }

    public String getComment() {
        return comment;
    }

    public boolean isSecure() {
        return secure;
    }

    public boolean isHttpOnly() {
        return httpOnly;
    }

    public Session getSession(String sessionId) {
        synchronized (LOCK) {
            Session session = map.get(sessionId);
            return (session != null && session.isNonExpired() ? session : null);
        }
    }

    @Override
    public void remove(Cookie cookie) {
        if (cookie == null) return;
        remove(cookie.getValue());
    }

    @Override
    public void remove(String sessionId) {
        if (sessionId == null) {
            return;
        }
        synchronized (LOCK) {
            map.remove(sessionId);
        }
    }

    public void removeExpiredSessions() {
        synchronized (LOCK) {
            Session[] sessions = ReflectUtils.toArray(Session.class, map.values());
            for (Session session : sessions) {
                if (session.isExpired()) {
                    map.remove(session.getSessionId());
                }
            }
        }
    }

    public boolean isValid(Session session) {
        if (session == null) return false;
        if (session.isExpired()) return false;
        return (map.containsKey(session.getSessionId()));
    }

    public Session getSession(ContainerRequestContext requestContext) {
        Cookie cookie = requestContext.getCookies().get(getCookieName());
        if (cookie == null) {
            return null;
        }
        String sessionId = cookie.getValue();
        return getSession(sessionId);
    }

    @Override
    public NewCookie newSessionCookie(Session session, UriInfo uriInfo) {
        int maxAge = (session == null) ? 0 : session.getSecondsToExpire();
        String sessionId = (session == null) ? null : session.getSessionId();
        return new NewCookie(getCookieName(), sessionId, path, domain, comment, maxAge, secure, httpOnly);
    }
}
