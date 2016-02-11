package com.makingiants.today.api.error_handling

import com.makingiants.today.api.error_handling.exceptions.fatal.BadAppVersionApiException
import com.makingiants.today.api.error_handling.exceptions.fatal.UnauthorizedApiException
import com.makingiants.today.api.error_handling.exceptions.simple.*
import retrofit.ErrorHandler
import retrofit.RetrofitError

/**
 * Translate errors from Api
 */
class ApiErrorHandler : ErrorHandler {

    override fun handleError(cause: RetrofitError): Throwable {
        val r = cause.getResponse()
        var message: String? = null
        var name: String? = null
        var body: ApiException? = null

        try {
            body = cause.getBodyAs(ApiException::class.java) as ApiException?
            if (body != null) {
                message = body.text
                name = body.name
            }
        } catch (e: RuntimeException) {
            // Http body not with api error format.
            message = if (r != null) String.format("(%d) %s", r.getStatus(), r.getReason()) else null
        }

        if (r != null && r.getStatus() === 400) {
            return BadRequestApiException(message, name, cause)
        } else if (r != null && r.getStatus() === 401) {
            return UnauthorizedApiException(message, name, cause)
        } else if (r != null && r.getStatus() === 404) {
            return NotFoundApiException(message, name, cause)
        } else if (r != null && r.getStatus() === 409) {
            return ConflictApiException(message, name, cause)
        } else if (r != null && r.getStatus() === 412) {
            return BadAppVersionApiException(message, name, cause)
        } else if (cause.getKind() === RetrofitError.Kind.NETWORK) {
            val localizedMessage = cause.message
            if (localizedMessage != null && localizedMessage.contains("timeout")) {
                message = "Our servers are busy right now, please try again in a few moment."
                return TimeoutApiException(message, name, cause)
            } else {
                message = "Looks like you don\\'t have internet access, verify it and please try again."
                return NetworkApiException(message, name, cause)
            }
        } else {
            return if (body != null) body else ApiException(message, name, cause.cause)
        }
    }
}
