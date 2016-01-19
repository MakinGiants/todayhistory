package com.makingiants.today.api.error_handling.exceptions.simple;

import android.support.annotation.Nullable;
import com.makingiants.today.api.error_handling.ApiException;

public class UserInfoMissingException extends ApiException {
  /**
   * @param aMessage if null will have R.string.error_basic as message
   * @param aName if null will have R.string.error as name
   */
  public UserInfoMissingException(@Nullable String aMessage, @Nullable String aName,
      Throwable cause) {
    super(aMessage, aName, cause);
  }
}
