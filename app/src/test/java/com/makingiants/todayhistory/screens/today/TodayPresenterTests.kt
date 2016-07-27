package com.makingiants.todayhistory.screens.today

import com.makingiants.today.api.repository.history.HistoryRepository
import com.makingiants.today.api.repository.history.pojo.Event
import com.makingiants.todayhistory.api.MockHistory.events
import com.makingiants.todayhistory.api.error_handling.MockApiException.apiException
import com.makingiants.todayhistory.mockSchedulers
import com.makingiants.todayhistory.utils.DateManager
import com.makingiants.todayhistory.utils.NetworkChecker
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Matchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import rx.Observable

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

  @Test
  fun onLoad_ifNoInternet_showError() {
    val events = events(2)

    `when`(dateManager.getTodayDay()).thenReturn(1)
    `when`(dateManager.getTodayMonth()).thenReturn(2)
    `when`(eventRepository.get(1, 2)).thenReturn(Observable.just(events))

    presenter.attach(view)

    val newEvents = events(5)
    `when`(eventRepository.get(1, 2)).thenReturn(Observable.just(newEvents))
    `when`(networkChecker.isNetworkConnectionAvailable()).thenReturn(false)
    presenter.onRefresh()

    // Progress is showed by pull to refresh mView automatically
    verify(view).showErrorToast("There is no internet.")
    //verifyNoMoreInteractions(view);
  }

  //<editor-fold desc="Basic Tests">
  @Test
  fun onCreate_setup_and_loadEvents() {
    // TODO: Fix when spy can be used with kotlin
    //        val spiedpresenter = spy(presenter)
    //
    //        `when`(dateManager.getTodayDay()).thenReturn(1)
    //        `when`(dateManager.getTodayMonth()).thenReturn(2)
    //        `when`(eventRepository.get(1, 2)).thenReturn(Observable.just(events(2)))
    //
    //        spiedpresenter.attach(view)
    //
    //        verify(spiedpresenter).loadEvents(true)
  }

  @Test
  fun onCreate_ifAlreadyHaveEvents_setup_and_updateView() {
    val events = events(2)

    `when`(dateManager.getTodayDay()).thenReturn(1)
    `when`(dateManager.getTodayMonth()).thenReturn(2)
    `when`(eventRepository.get(1, 2)).thenReturn(Observable.just(events))

    presenter.attach(view)
    presenter.unAttach()
    presenter.attach(view)

    //        verify(spiedPresenter, times(1)).loadEvents(true)
    verify(view, times(2)).showEvents(events)
  }

  @Test
  fun onDestroy_freeResources() {
    `when`(dateManager.getTodayDay()).thenReturn(1)
    `when`(dateManager.getTodayMonth()).thenReturn(2)
    `when`(eventRepository.get(1, 2)).thenReturn(Observable.just(events(2)))

    presenter.attach(view)
    presenter.unAttach()

    assertThat(presenter.view).isNull()
    assertThat(presenter.compositeSubscription?.hasSubscriptions()).isFalse()
  }
  //</editor-fold>

  @Test
  fun onCreate_showSongList() {
    val events = events(2)

    `when`(dateManager.getTodayDay()).thenReturn(1)
    `when`(dateManager.getTodayMonth()).thenReturn(2)
    `when`(eventRepository.get(1, 2)).thenReturn(Observable.just(events))

    presenter.attach(view)

    verify(view).showEmptyViewProgress()
    verify(view).dismissEmptyViewProgress()
    verify(view).showEvents(events)
  }

  @Test
  fun onCreate_ifEmptyResponse_showEmptyView() {
    val events = events(0)
    `when`(dateManager.getTodayDay()).thenReturn(1)
    `when`(dateManager.getTodayMonth()).thenReturn(2)
    `when`(eventRepository.get(1, 2)).thenReturn(Observable.just(events))

    presenter.attach(view)

    verify(view).showEmptyViewProgress()
    verify(view).dismissEmptyViewProgress()
    verify(view).showEmptyView()
  }

  @Test
  fun onCreate_onError_showErrorView() {
    val error = apiException()

    `when`(dateManager.getTodayDay()).thenReturn(1)
    `when`(dateManager.getTodayMonth()).thenReturn(2)
    `when`(eventRepository.get(1, 2)).thenReturn(Observable.error<List<Event>>(error))

    presenter.attach(view)

    verify(view).showEmptyViewProgress()
    verify(view).dismissEmptyViewProgress()
    verify(view).hideEmptyView()
    verify(view).hideEvents()
    verify(view).showErrorView(error.name, error.text)
  }

  @Test
  fun onLoad_emptyViewVisible_showEventsList() {
    val events = events(0)
    val newEvents = events(5)

    `when`(dateManager.getTodayDay()).thenReturn(1)
    `when`(dateManager.getTodayMonth()).thenReturn(2)
    `when`(eventRepository.get(1, 2)).thenReturn(Observable.just(events))

    presenter.attach(view)

    `when`(dateManager.getTodayDay()).thenReturn(1)
    `when`(dateManager.getTodayMonth()).thenReturn(2)
    `when`(eventRepository.get(1, 2)).thenReturn(Observable.just(newEvents))
    presenter.onRefresh()

    verify(view).showReloadProgress()
    verify(view, times(2)).dismissReloadProgress()
    verify(view, times(2)).hideEmptyView()
    verify(view, times(2)).hideErrorView()
    verify(view, times(1)).showEmptyView()
    verify(view).showEvents(newEvents)
  }

  @Test
  fun onLoad_ifEmptyViewVisible_onError_showErrorView() {
    val events = events(0)
    val error = apiException()

    `when`(dateManager.getTodayDay()).thenReturn(1)
    `when`(dateManager.getTodayMonth()).thenReturn(2)
    `when`(eventRepository.get(1, 2)).thenReturn(Observable.just(events))

    presenter.attach(view)

    `when`(eventRepository.get(1, 2)).thenReturn(Observable.error<List<Event>>(error))

    presenter.onRefresh()

    // Progress is showed by pull to refresh mView automatically
    verify(view).showReloadProgress()
    verify(view, times(2)).dismissReloadProgress()
    verify(view, times(2)).hideEmptyView()
    verify(view).hideEvents()
    verify(view).showErrorView(error.name, error.text)
  }

  @Test
  fun onLoad_ifErrorViewVisible_showeventsList() {
    val error = apiException()

    `when`(dateManager.getTodayDay()).thenReturn(1)
    `when`(dateManager.getTodayMonth()).thenReturn(2)
    `when`(eventRepository.get(1, 2)).thenReturn(Observable.error<List<Event>>(error))

    presenter.attach(view)

    val newEvents = events(5)
    `when`(eventRepository.get(1, 2)).thenReturn(Observable.just(newEvents))
    presenter.onRefresh()

    verify(view).showReloadProgress()
    verify(view, times(2)).dismissReloadProgress()
    verify(view).hideErrorView()
    verify(view, times(2)).hideEmptyView()
    verify(view).showEvents(newEvents)
  }

  @Test
  fun onLoad_ifEventListVisible_loadNewItems() {
    val events = events(2)

    `when`(dateManager.getTodayDay()).thenReturn(1)
    `when`(dateManager.getTodayMonth()).thenReturn(2)
    `when`(eventRepository.get(1, 2)).thenReturn(Observable.just(events))

    presenter.attach(view)

    val newEvents = events(5)
    `when`(eventRepository.get(1, 2)).thenReturn(Observable.just(newEvents))
    presenter.onRefresh()

    // Progress is showed by pull to refresh mView automatically
    verify(view).showReloadProgress()
    verify(view, times(2)).dismissReloadProgress()
    verify(view).showEvents(newEvents)
  }

  @Test
  fun onLoad_ifEventsListVisible_onError_showErrorDialog() {
    val events = events(2)
    val error = apiException()

    `when`(dateManager.getTodayDay()).thenReturn(1)
    `when`(dateManager.getTodayMonth()).thenReturn(2)
    `when`(eventRepository.get(1, 2)).thenReturn(Observable.just(events))

    presenter.attach(view)

    `when`(eventRepository.get(1, 2)).thenReturn(Observable.error<List<Event>>(error))

    presenter.onRefresh()

    // Progress is showed by pull to refresh mView automatically
    verify(view).showReloadProgress()
    verify(view, times(2)).dismissReloadProgress()
    verify(view).showErrorToast(anyString())
    verify(view, times(0)).showErrorView(anyString(), anyString())
    verify(view, times(0)).hideEvents()
  }

  @Test
  fun onLoad_ifEventsListVisible_ifLoadEmpty_showDialogInsteadOfEmptyView() {
    val events = events(2)
    val newEvents = events(0)

    `when`(dateManager.getTodayDay()).thenReturn(1)
    `when`(dateManager.getTodayMonth()).thenReturn(2)
    `when`(eventRepository.get(1, 2)).thenReturn(Observable.just(events))

    presenter.attach(view)

    `when`(eventRepository.get(1, 2)).thenReturn(Observable.just(newEvents))

    presenter.onRefresh()

    verify(view).showErrorToast(anyString())
    verify(view).showReloadProgress()
    verify(view, times(2)).dismissReloadProgress()
    verify(view, times(0)).showEmptyView()
    verify(view, times(1)).showEvents(events) // 1 time is attach and then on updateItems
  }
}
