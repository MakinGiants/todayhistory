package com.makingiants.today.api.error_handling.exceptions.simple;

import android.support.annotation.Nullable;
import com.makingiants.today.api.error_handling.ApiException;

public class NotFoundApiException extends ApiException {
  public NotFoundApiException(@Nullable String message, @Nullable String name, Throwable cause) {
    super(message, name, cause);
  }
}
