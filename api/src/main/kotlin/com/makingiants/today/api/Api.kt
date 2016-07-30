package com.makingiants.today.api

import android.content.Context
import android.support.annotation.IntDef
import android.support.annotation.VisibleForTesting
import com.google.gson.FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.ref.WeakReference

/**
 * Every SERVICE that is going to be created should be done by [Api.create] func.
 * and not using [Api.sRestAdapter].
 *
 *
 * Each request throw an [ApiException] if something happens.

 * @see Api.init
 * @see Api.create
 */
object Api {
  const val LOG_LEVEL_NONE = 0L
  const val LOG_LEVEL_BASIC = 1L
  const val LOG_LEVEL_FULL = 2L

  @IntDef(LOG_LEVEL_NONE, LOG_LEVEL_BASIC, LOG_LEVEL_FULL)
  @Retention(AnnotationRetention.SOURCE)
  annotation class LogLevel

  private var sRestAdapter: Retrofit? = null
  private var mWeakContext: WeakReference<Context>? = null

  fun init(context: Context, host: String, @LogLevel logLevel: Long = LOG_LEVEL_NONE) {
    mWeakContext = WeakReference(context)

    val interceptor = HttpLoggingInterceptor().apply {
      level = when (logLevel) {
        LOG_LEVEL_BASIC -> HttpLoggingInterceptor.Level.BASIC
        LOG_LEVEL_FULL -> HttpLoggingInterceptor.Level.BODY
        else -> HttpLoggingInterceptor.Level.NONE
      }
    }

    val okHttpClient = OkHttpClient.Builder()
        .addNetworkInterceptor(interceptor)
        .build()

    sRestAdapter = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(if (host.endsWith("/")) host else "$host/")
        .addConverterFactory(GsonConverterFactory.create(gson()))
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .build()
  }

  fun <T> create(service: Class<T>): T? = sRestAdapter?.create(service)

  @VisibleForTesting
  fun gson(): Gson = GsonBuilder().setFieldNamingPolicy(LOWER_CASE_WITH_UNDERSCORES).create()
}
