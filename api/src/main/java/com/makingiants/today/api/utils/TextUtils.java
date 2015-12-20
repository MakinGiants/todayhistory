package com.makingiants.today.api.utils;

import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Used to check string values.
 */
public final class TextUtils {

  public static final int ERROR_EMPTY = 0;

  @IntDef(value = { ERROR_EMPTY })
  @Retention(RetentionPolicy.SOURCE)
  public @interface ValidationError {
  }

  private TextUtils() {

  }

  public static boolean isEmpty(@Nullable CharSequence charSequence) {
    return charSequence == null || charSequence.toString().trim().isEmpty();
  }
}
