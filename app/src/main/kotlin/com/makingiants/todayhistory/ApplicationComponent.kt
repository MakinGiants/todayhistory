package com.makingiants.todayhistory

import com.makingiants.todayhistory.screens.today.TodayActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(ApplicationModule::class))
interface ApplicationComponent {
  fun inject(application: TodayApp)
  fun inject(todayActivity: TodayActivity)
}