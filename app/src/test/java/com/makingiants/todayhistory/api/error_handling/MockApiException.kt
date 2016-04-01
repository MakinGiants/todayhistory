package com.makingiants.todayhistory.api.error_handling

import com.makingiants.today.api.error_handling.ApiException

object MockApiException {
    fun apiException(): ApiException {
        return ApiException("message", "Name", Exception("Test"))
    }
}
