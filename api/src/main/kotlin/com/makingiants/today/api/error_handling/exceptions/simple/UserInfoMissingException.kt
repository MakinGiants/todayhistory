package com.makingiants.today.api.error_handling.exceptions.simple

import com.makingiants.today.api.error_handling.ApiException

class UserInfoMissingException
(text: String?, name: String?, cause: Throwable) : ApiException(text, name, cause)
