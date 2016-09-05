package com.makingiants.todayhistory.api

import com.makingiants.today.api.Answer
import com.makingiants.today.api.error_handling.ApiError

object MockApiError {

  fun <T> wrappedApiError() = Answer<T>(error = apiError())

  fun apiError() = ApiError()

}