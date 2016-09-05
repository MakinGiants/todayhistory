package com.makingiants.todayhistory.screens.today

import com.barista_v.winwin.mockSchedulers
import com.makingiants.today.api.repository.history.HistoryRepository
import com.makingiants.todayhistory.utils.DateManager
import com.makingiants.todayhistory.utils.NetworkChecker
import org.junit.Before
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class TodayPresenterTests {
  @Mock lateinit var mockedEventRepository: HistoryRepository
  @Mock lateinit var mockedView: TodayView
  @Mock lateinit var mockedDateManager: DateManager
  @Mock lateinit var mockedNetworkChecker: NetworkChecker
  lateinit var presenter: TodayPresenter

  @Before
  fun setup() {
    mockSchedulers()
    MockitoAnnotations.initMocks(this)
    presenter = TodayPresenter(mockedDateManager)
  }

  //TODO:

}
