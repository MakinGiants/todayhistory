package com.makingiants.today.api.error_handling.exceptions.fatal

class UnauthorizedApiException
(text: String?, name: String?, cause: Throwable) : FatalApiException(text, name, cause)
