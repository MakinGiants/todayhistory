package com.makingiants.today.api;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.makingiants.today.api.error_handling.ApiErrorHandler;
import com.makingiants.today.api.error_handling.ApiException;
import com.squareup.okhttp.OkHttpClient;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

/**
 * Every SERVICE that is going to be created should be done by {@link Api#create(Class)} func.
 * and not using {@link Api#sRestAdapter}.
 * <p>
 * Each request throw an {@link ApiException} if something happens.
 *
 * @see Api#init(Context, String, String, int)
 * @see Api#create(Class)
 */
public class Api {

  public static final int LOG_LEVEL_NONE = 0;
  public static final int LOG_LEVEL_BASIC = 1;
  public static final int LOG_LEVEL_FULL = 2;

  private static RestAdapter sRestAdapter;
  private static int sLogLevel = LOG_LEVEL_NONE;

  @IntDef(flag = true, value = { LOG_LEVEL_NONE, LOG_LEVEL_BASIC, LOG_LEVEL_FULL })
  @Retention(RetentionPolicy.SOURCE)
  public @interface LogLevel {
  }

  private static WeakReference<Context> mWeakContext;

  //<editor-fold desc="Init">
  public static void init(Context context, @NonNull String host) {
    OkHttpClient okHttpClient = new OkHttpClient();
    okHttpClient.setConnectTimeout(30, TimeUnit.SECONDS); // Initial value: 10
    okHttpClient.setWriteTimeout(30, TimeUnit.SECONDS);
    okHttpClient.setReadTimeout(30, TimeUnit.SECONDS);

    mWeakContext = new WeakReference<>(context);
    sRestAdapter = new RestAdapter.Builder()
        .setEndpoint(host)
        .setClient(new OkClient(okHttpClient))
        .setConverter(new GsonConverter(gson()))
        .setErrorHandler(new ApiErrorHandler())
        .build();

    // Refresh logLevel because maybe rest adapter was null before it is setted.
    setLogLevel(sLogLevel);
  }

  @Nullable
  public static <T> T create(Class<T> service) {
    return sRestAdapter != null ? sRestAdapter.create(service) : null;
  }

  //</editor-fold>

  //<editor-fold desc="Getters">

  @VisibleForTesting
  public static Gson gson() {
    return new GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .create();
  }

  @LogLevel
  public static int getLogLevel() {
    return sLogLevel;
  }

  @Nullable
  public static Context getContext() {
    return mWeakContext.get();
  }

  //</editor-fold>

  //<editor-fold desc="Log Level">
  public static void setLogLevel(@LogLevel int logLevel) {
    sLogLevel = logLevel;
    if (sRestAdapter != null) {
      sRestAdapter.setLogLevel(getAdapterLogLevel());
    }
  }

  public static RestAdapter.LogLevel getAdapterLogLevel() {
    if (sLogLevel == LOG_LEVEL_NONE) {
      return RestAdapter.LogLevel.NONE;
    } else if (sLogLevel == LOG_LEVEL_BASIC) {
      return RestAdapter.LogLevel.BASIC;
    } else if (sLogLevel == LOG_LEVEL_FULL) {
      return RestAdapter.LogLevel.FULL;
    } else {
      return RestAdapter.LogLevel.NONE;
    }
  }

  //</editor-fold>
}
