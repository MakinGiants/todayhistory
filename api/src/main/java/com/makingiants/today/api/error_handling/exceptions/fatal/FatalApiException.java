package com.makingiants.today.api.error_handling.exceptions.fatal;

import android.support.annotation.Nullable;
import com.makingiants.today.api.error_handling.ApiException;

public class FatalApiException extends ApiException {
  public FatalApiException(@Nullable String aMessage, @Nullable String aName, Throwable cause) {
    super(aMessage, aName, cause);
  }
}
