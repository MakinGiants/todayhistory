package com.makingiants.today.api.error_handling.exceptions.fatal;

import android.support.annotation.Nullable;

public class UnauthorizedApiException extends FatalApiException {
  public UnauthorizedApiException(@Nullable String message, @Nullable String name,
      Throwable cause) {
    super(message, name, cause);
  }
}
