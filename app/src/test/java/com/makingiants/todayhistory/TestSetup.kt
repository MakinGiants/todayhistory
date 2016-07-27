package com.makingiants.todayhistory

import rx.android.plugins.RxAndroidPlugins
import rx.android.plugins.RxAndroidSchedulersHook
import rx.plugins.RxJavaHooks
import rx.schedulers.Schedulers

fun mockSchedulers() {
  RxJavaHooks.setOnIOScheduler { Schedulers.immediate() }
  RxJavaHooks.setOnComputationScheduler { Schedulers.immediate() }
  RxJavaHooks.setOnNewThreadScheduler { Schedulers.immediate() }

  RxAndroidPlugins.getInstance().reset()
  RxAndroidPlugins.getInstance().registerSchedulersHook(object : RxAndroidSchedulersHook() {
    override fun getMainThreadScheduler() = Schedulers.immediate()
  })
}