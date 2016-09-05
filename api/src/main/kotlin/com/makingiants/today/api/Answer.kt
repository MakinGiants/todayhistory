package com.makingiants.today.api

import com.makingiants.today.api.error_handling.ApiError
import com.makingiants.today.api.error_handling.NetworkApiError
import retrofit2.Response
import retrofit2.adapter.rxjava.HttpException
import java.io.IOException

/**
 * Proxy to read, parse and organize backend responses and errors
 *
 * @property body null if no there was a weird error (some kind of unexpected exception)
 * @property error null if no error
 */
open class Answer<out T>(val body: T? = null, val error: ApiError? = null) {

  constructor(exception: Exception) : this(null, ApiError(exception))

  companion object {

    /**
     * Initialize Answer from backend answers parsing errors with defined format.
     */
    fun <T> from(response: Response<T>): Answer<T> =
        Answer(response.body(), parseError(response))

    private fun <T> parseError(response: Response<T>): ApiError {
      val json = response.errorBody().string()
      val code = response.code()
      return ApiError(cause = Throwable("Error($code): $json"))
    }

    /**
     * Initialize Answer from retrofit error
     *
     * TODO: move to other place
     *
     * HttpException -> non 200
     * IOException -> network error
     * Other -> Unknown
     */
    fun <T> from(throwable: Throwable): Answer<T> {
      val apiError = when (throwable) {
        is HttpException -> ApiError(cause = throwable)
        is IOException -> NetworkApiError(cause = throwable)
        else -> ApiError()
      }
      return Answer(error = apiError)
    }
  }
}