package com.makingiants.today.api.error_handling

import com.makingiants.today.api.R

class UninitializedApiError(cause: Throwable?) : ApiError(cause) {
  override val messageId = R.string.error_uninitialized
}

class NoInternetApiError(cause: Throwable? = null): ApiError(cause){
  override val messageId = R.string.error_no_internet

}