package common.messaging;

public enum MessageType {
  CREATE_USER("User.Create"),
  GET_ALL_USERS("User.Get.All");

  private final String value;

  MessageType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
