package com.makingiants.today.api.error_handling;

import com.makingiants.today.api.error_handling.exceptions.fatal.BadAppVersionApiException;
import com.makingiants.today.api.error_handling.exceptions.fatal.UnauthorizedApiException;
import com.makingiants.today.api.error_handling.exceptions.simple.BadRequestApiException;
import com.makingiants.today.api.error_handling.exceptions.simple.ConflictApiException;
import com.makingiants.today.api.error_handling.exceptions.simple.NetworkApiException;
import com.makingiants.today.api.error_handling.exceptions.simple.NotFoundApiException;
import com.makingiants.today.api.error_handling.exceptions.simple.TimeoutApiException;
import retrofit.ErrorHandler;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Translate errors from Api
 */
public class ApiErrorHandler implements ErrorHandler {

  @Override
  public Throwable handleError(RetrofitError cause) {
    Response r = cause.getResponse();
    String message = null, name = null;

    ApiException body = null;
    try {
      body = (ApiException) cause.getBodyAs(ApiException.class);
      if (body != null) {
        message = body.getMessage();
        name = body.getName();
      }
    } catch (RuntimeException e) { // Http body not with api error format.
      message = r != null ? String.format("(%d) %s", r.getStatus(), r.getReason()) : null;
    }

    if (r != null && r.getStatus() == 400) {
      return new BadRequestApiException(message, name, cause);
    } else if (r != null && r.getStatus() == 401) {
      return new UnauthorizedApiException(message, name, cause);
    } else if (r != null && r.getStatus() == 404) {
      return new NotFoundApiException(message, name, cause);
    } else if (r != null && r.getStatus() == 409) {
      return new ConflictApiException(message, name, cause);
    } else if (r != null && r.getStatus() == 412) {
      return new BadAppVersionApiException(message, name, cause);
    } else if (cause.getKind() == RetrofitError.Kind.NETWORK) {
      String localizedMessage = cause.getLocalizedMessage();
      if (localizedMessage != null && localizedMessage.contains("timeout")) {
        message = "Our servers are busy right now, please try again in a few moment.";
        return new TimeoutApiException(message, name, cause);
      } else {
        message = "Looks like you don\\'t have internet access, verify it and please try again.";
        return new NetworkApiException(message, name, cause);
      }
    } else {
      return body != null ? body : new ApiException(message, name, cause.getCause());
    }
  }
}
