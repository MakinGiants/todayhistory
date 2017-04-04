package com.makingiants.todayhistory

import android.app.Application
import android.os.Build
import com.makingiants.today.api.Api
import com.makingiants.todayhistory.base.dependency_injection.DaggerMyApplicationComponent
import com.makingiants.todayhistory.base.dependency_injection.MyApplicationComponent
import com.makingiants.todayhistory.base.dependency_injection.MyApplicationModule
import com.makingiants.todayhistory.utils.log.CrashlyticsTree
import com.makingiants.todayhistory.utils.log.DebugTree
import timber.log.Timber
import java.util.*



class TodayApp : Application() {

  val applicationComponent: MyApplicationComponent by lazy {
    DaggerMyApplicationComponent.builder()
        .myApplicationModule(MyApplicationModule(this))
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

//    StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
//        .detectDiskReads()
//        .detectDiskWrites()
//        .detectNetwork()   // or .detectAll() for all detectable problems
//        .penaltyLog()
//        .build())
//
//    StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder()
//        .detectLeakedSqlLiteObjects()
//        .detectLeakedClosableObjects()
//        .penaltyLog()
//        .penaltyDeath()
//        .build())

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
