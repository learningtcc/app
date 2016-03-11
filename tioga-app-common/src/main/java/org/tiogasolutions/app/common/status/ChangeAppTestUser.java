package org.tiogasolutions.app.common.status;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ChangeAppTestUser {

  private final String testEmailAddress;
  private final String testPassword;

  @JsonCreator
  public ChangeAppTestUser(@JsonProperty("testEmailAddress") String testEmailAddress,
                           @JsonProperty("testEmailAddress") String testPassword) {

    this.testEmailAddress = testEmailAddress;
    this.testPassword = testPassword;
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

    ChangeAppTestUser that = (ChangeAppTestUser) o;

    if (testEmailAddress != null ? !testEmailAddress.equals(that.testEmailAddress) : that.testEmailAddress != null)
      return false;
    if (testPassword != null ? !testPassword.equals(that.testPassword) : that.testPassword != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = testEmailAddress != null ? testEmailAddress.hashCode() : 0;
    result = 31 * result + (testPassword != null ? testPassword.hashCode() : 0);
    return result;
  }

  @Override
  public String
  toString() {
    return "ChangeAppTestUser{" +
        "testEmailAddress='" + testEmailAddress + '\'' +
        ", testPassword='" + testPassword + '\'' +
        '}';
  }
}
