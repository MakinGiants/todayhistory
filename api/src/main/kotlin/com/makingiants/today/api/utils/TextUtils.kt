package com.makingiants.today.api.utils

import android.support.annotation.IntDef

/**
 * Used to check string values.
 */
object TextUtils {
  const val ERROR_EMPTY = 0L

  @IntDef(ERROR_EMPTY)
  @Retention(AnnotationRetention.SOURCE)
  annotation class ValidationError

  fun isEmpty(charSequence: CharSequence?): Boolean {
    return charSequence == null || charSequence.toString().trim { it <= ' ' }.isEmpty()
  }
}
