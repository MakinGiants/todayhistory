package com.makingiants.today.api.error_handling.exceptions.fatal;

public class BadAppVersionApiException extends FatalApiException {
  public BadAppVersionApiException(String message, String name, Throwable cause) {
    super(message, name, cause);
  }
}
