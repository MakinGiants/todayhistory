package com.makingiants.today.api.error_handling

import android.content.Context
import com.makingiants.today.api.R

open class ApiError(cause: Throwable? = null) {
  open val titleId = R.string.error
  open val messageId = R.string.error_message

  fun getTitle(context: Context): String = context.resources.getString(titleId)
  fun getMessage(context: Context): String = context.resources.getString(messageId)
}