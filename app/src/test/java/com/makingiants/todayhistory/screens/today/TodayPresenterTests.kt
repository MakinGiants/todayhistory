package com.makingiants.todayhistory.screens.today

import com.makingiants.today.api.repository.history.HistoryRepository
import com.makingiants.todayhistory.mockSchedulers
import com.makingiants.todayhistory.utils.DateManager
import com.makingiants.todayhistory.utils.NetworkChecker
import org.junit.Before
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class TodayPresenterTests {
  @Mock lateinit var eventRepository: HistoryRepository
  @Mock lateinit var view: TodayView
  @Mock lateinit var dateManager: DateManager
  @Mock lateinit var networkChecker: NetworkChecker
  lateinit var presenter: TodayPresenter

  @Before
  fun setup() {
    mockSchedulers()
    MockitoAnnotations.initMocks(this)
    presenter = TodayPresenter(dateManager, eventRepository, networkChecker)
    `when`(networkChecker.isNetworkConnectionAvailable()).thenReturn(true)
  }

  //TODO:
}
