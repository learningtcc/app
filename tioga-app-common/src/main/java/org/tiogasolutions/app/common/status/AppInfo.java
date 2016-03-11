package org.tiogasolutions.app.common.status;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AppInfo {

  private final AppStatus status;
  private final String message;
  private final String testEmailAddress;
  private final String testPassword;

  @JsonCreator
  public AppInfo(@JsonProperty("status") AppStatus status,
                 @JsonProperty("message") String message,
                 @JsonProperty("testEmailAddress") String testEmailAddress,
                 @JsonProperty("testPassword") String testPassword) {

    this.status = status;
    this.message = message;
    this.testEmailAddress = testEmailAddress;
    this.testPassword = testPassword;
  }

  public AppStatus getStatus() {
    return status;
  }

  public String getMessage() {
    return message;
  }

  public String getTestEmailAddress() {
    return testEmailAddress;
  }

  public String getTestPassword() {
    return testPassword;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    AppInfo that = (AppInfo) o;

    if (status != that.status) return false;
    if (message != null ? !message.equals(that.message) : that.message != null) return false;
    if (testEmailAddress != null ? !testEmailAddress.equals(that.testEmailAddress) : that.testEmailAddress != null)
      return false;
    if (testPassword != null ? !testPassword.equals(that.testPassword) : that.testPassword != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = status != null ? status.hashCode() : 0;
    result = 31 * result + (message != null ? message.hashCode() : 0);
    result = 31 * result + (testEmailAddress != null ? testEmailAddress.hashCode() : 0);
    result = 31 * result + (testPassword != null ? testPassword.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "AppInfo{" +
        "status=" + status +
        ", message='" + message + '\'' +
        ", testEmailAddress='" + testEmailAddress + '\'' +
        ", testPassword='" + testPassword + '\'' +
        '}';
  }
}
