package com.makingiants.todayhistory

import android.app.Application
import android.os.Build
import com.makingiants.today.api.Api
import com.makingiants.todayhistory.utils.log.CrashlyticsTree
import com.makingiants.todayhistory.utils.log.DebugTree
import timber.log.Timber
import java.util.*

class TodayApp : Application() {

  val applicationComponent: ApplicationComponent by lazy {
    DaggerApplicationComponent.builder()
        .applicationModule(ApplicationModule(this))
        .build()
  }

  override fun onCreate() {
    super.onCreate()

    if (BuildConfig.ANALYTICS_ENABLED) {
      Timber.plant(CrashlyticsTree(this, properties))
    } else {
      Timber.plant(DebugTree())
    }
    Timber.tag("TodayHistory")

    Api.init(applicationContext, BuildConfig.HOST, Api.LOG_LEVEL_FULL)
  }

  companion object {

    /**
     * Return a map with keys and values referencing environment variables
     */
    val properties: Map<String, String>
      get() = object : LinkedHashMap<String, String>() {
        init {
          put("Host", BuildConfig.HOST)
          put("BuildType", BuildConfig.BUILD_TYPE)
          put("Flavor", BuildConfig.FLAVOR)
          put("AppVersion", BuildConfig.VERSION_NAME)
          put("BuildNumber", BuildConfig.VERSION_CODE.toString())
          put("AndroidVersion", Build.VERSION.RELEASE)
          put("GitSHA", BuildConfig.GIT_SHA)
          put("Manufacturer", Build.MANUFACTURER)
          put("Model", Build.MODEL)
        }
      }
  }
}
