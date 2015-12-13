package com.makingiants.today.api.error_handling;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.text.TextUtils;

/**
 * Api Error representation.
 * {
 * "message": "UnAuthorized Order",
 * "name": "UnAuthorized",
 * "value": "401",
 * }
 */

public class ApiException extends Throwable {

  @VisibleForTesting static final String DEFAULT_NAME = "Error";
  @VisibleForTesting static final String DEFAULT_MESSAGE = "There was an error, please try again.";

  public String value;
  @NonNull private String message;
  @NonNull private String name;

  /**
   * @param aMessage if null will have R.string.error_basic as message
   * @param aName if null will have R.string.error as name
   */
  public ApiException(@Nullable String aMessage, @Nullable String aName, Throwable cause) {
    super(aMessage, cause);

    this.message = TextUtils.isEmpty(aMessage) ? DEFAULT_MESSAGE : aMessage;
    this.name = TextUtils.isEmpty(aName) ? DEFAULT_NAME : aName;
  }

  private ApiException(Throwable cause) {
    super("Error", cause);

    this.message = DEFAULT_MESSAGE;
    this.name = DEFAULT_NAME;
  }

  /**
   * Helper function to transform a throwable into ApiException
   */
  public static ApiException from(Throwable throwable) {
    return throwable instanceof ApiException ? (ApiException) throwable
        : new ApiException(throwable);
  }

  //<editor-fold desc="Getters">

  @NonNull
  public String getMessage() {
    return message;
  }

  @NonNull
  public String getName() {
    return name;
  }

  //</editor-fold>
}
