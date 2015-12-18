package com.makingiants.todayhistory.api.error_handling;

import com.makingiants.today.api.error_handling.ApiException;

public class MockApiException {
  public static ApiException apiException() {
    return new ApiException("message", "Name", new Exception("Test"));
  }
}
