package org.tiogasolutions.app.common.status;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ChangeAppStatus {

  private final AppStatus status;
  private final String message;

  @JsonCreator
  public ChangeAppStatus(@JsonProperty("status") AppStatus status,
                         @JsonProperty("message") String message) {
    this.status = status;
    this.message = message;
  }

  public AppStatus getStatus() {
    return status;
  }

  public String getMessage() {
    return message;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ChangeAppStatus that = (ChangeAppStatus) o;

    if (status != that.status) return false;
    if (message != null ? !message.equals(that.message) : that.message != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = status != null ? status.hashCode() : 0;
    result = 31 * result + (message != null ? message.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "ChangeAppStatus{" +
        "status=" + status +
        ", message='" + message + '\'' +
        '}';
  }
}
