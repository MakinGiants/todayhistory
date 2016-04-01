package com.makingiants.today.api.error_handling.exceptions.fatal

import com.makingiants.today.api.error_handling.ApiException

open class FatalApiException
(text: String?, name: String?, cause: Throwable) : ApiException(text, name, cause)
