package com.makingiants.todayhistory

import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers


open class RxJavaTestHelper {

  companion object {

    fun setUp() {
      RxJavaPlugins.setSingleSchedulerHandler { Schedulers.trampoline() }
      RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
      RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
      RxJavaPlugins.setNewThreadSchedulerHandler { Schedulers.trampoline() }

      RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    fun tearDown() {
      RxJavaPlugins.reset()
      RxAndroidPlugins.reset()
    }

  }

}