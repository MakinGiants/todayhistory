package com.makingiants.todayhistory

import android.content.Context
import com.makingiants.today.api.repository.history.HistoryRepository
import com.makingiants.todayhistory.screens.today.TodayPresenter
import com.makingiants.todayhistory.utils.DateManager
import com.makingiants.todayhistory.utils.NetworkChecker
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(val application: TodayApp) {

  @Provides @Singleton
  fun provideContext(): Context = application

  @Provides @Singleton
  fun provideTodayPresenter() = TodayPresenter(provideDateManager(), provideHistoryRepository(),
      provideNetworkChecker())

  @Provides
  fun provideDateManager() = DateManager()

  @Provides
  fun provideHistoryRepository() = HistoryRepository()

  @Provides
  fun provideNetworkChecker() = NetworkChecker(application)

}