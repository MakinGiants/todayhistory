package com.makingiants.today.api.error_handling

import com.makingiants.today.api.error_handling.ApiError

class NetworkApiError(cause: Throwable) : ApiError(cause)
