package com.makingiants.todayhistory.base.dependency_injection


import android.support.v4.app.FragmentActivity
import com.makingiants.today.api.repository.history.HistoryRepository
import com.makingiants.todayhistory.screens.today.TodayActivity
import com.makingiants.todayhistory.screens.today.TodayPresenter
import com.makingiants.todayhistory.utils.DateManager
import com.makingiants.todayhistory.utils.NetworkChecker
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Scope

@Module
class MyActivityModule(val fragmentActivity: FragmentActivity) {

  @Provides
  fun providesActivity(): FragmentActivity = fragmentActivity

  @Provides @MyActivityScope
  fun provideTodayPresenter(dateManager: DateManager,
                            historyRepository: HistoryRepository,
                            networkChecker: NetworkChecker) =
      TodayPresenter(dateManager, historyRepository, networkChecker)

  @Provides
  fun provideDateManager() = DateManager()

  @Provides
  fun provideHistoryRepository() = HistoryRepository()

  @Provides
  fun provideNetworkChecker(activity: FragmentActivity) = NetworkChecker(activity)
}

@MyActivityScope
@Component(modules = arrayOf(MyActivityModule::class),
    dependencies = arrayOf(MyApplicationComponent::class))
interface MyActivityComponent : MyApplicationComponent {

  fun inject(target: TodayActivity)

}

@Scope
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class MyActivityScope