package com.makingiants.todayhistory

import android.app.Application
import android.content.Context
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MyApplicationModule(val application: Application) {

  @Provides @Singleton
  fun provideAppContext(): Context = application

}

@Singleton
@Component(modules = arrayOf(MyApplicationModule::class))
interface MyApplicationComponent {

  fun appContext(): Context

}