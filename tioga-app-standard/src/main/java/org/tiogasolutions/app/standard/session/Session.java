package org.tiogasolutions.app.standard.session;

import org.tiogasolutions.dev.common.id.uuid.TimeUuid;

import java.io.Serializable;
import java.util.HashMap;

public class Session extends HashMap<String,Serializable> {

  private final String sessionId;
  private final long sessionDuration;
  private long expiresAt;

  /**
   * Creates a new session
   * @param sessionDuration the life of the session in milliseconds
   */
  public Session(long sessionDuration) {
    this.sessionDuration = sessionDuration;
    this.sessionId = TimeUuid.randomUUID().toString();
    renew();
  }

  public String getSessionId() {
    return sessionId;
  }

  public long getSessionDuration() {
    return sessionDuration;
  }

  public long getExpiresAt() {
    return expiresAt;
  }

  /**
   * Updates the session's expiration to the current time + session duration.
   */
  public synchronized void renew() {
    expiresAt = System.currentTimeMillis() + sessionDuration;
  }

  public boolean isExpired() {
    return System.currentTimeMillis() >= expiresAt;
  }

  public boolean isNonExpired() {
    return System.currentTimeMillis() < expiresAt;
  }

  public int getSecondsToExpire() {
    long remaining = expiresAt - System.currentTimeMillis();
    return (int)(remaining / 1000);
  }
}
