package com.makingiants.todayhistory.today

import com.makingiants.today.api.repository.history.HistoryRepository
import com.makingiants.today.api.repository.history.pojo.Event
import com.makingiants.todayhistory.api.MockHistory.events
import com.makingiants.todayhistory.api.error_handling.MockApiException.apiException
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
    @Mock var mockedEventRepository: HistoryRepository? = null
    @Mock var mockedView: TodayView? = null
    @Mock var mockedDateManager: DateManager? = null
    @Mock var mockedNetworkChecker: NetworkChecker? = null
    internal var presenter: TodayPresenter? = null

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        //        Mockito.reset(mockedEventRepository!!, mockedView!!, mockedDateManager!!, mockedNetworkChecker!!)
        presenter = TodayPresenter(mockedDateManager!!)
        `when`(mockedNetworkChecker!!.isNetworkConnectionAvailable()).thenReturn(true)
    }

    @Test
    fun onLoad_ifNoInternet_showError() {
        val events = events(2)

        `when`(mockedDateManager!!.getTodayDay()).thenReturn(1)
        `when`(mockedDateManager!!.getTodayMonth()).thenReturn(2)
        `when`(mockedEventRepository!!.get(1, 2)).thenReturn(Observable.just(events))

        presenter!!.onCreate(mockedView!!, mockedEventRepository!!, mockedNetworkChecker!!)

        val newEvents = events(5)
        `when`(mockedEventRepository!!.get(1, 2)).thenReturn(Observable.just(newEvents))
        `when`(mockedNetworkChecker!!.isNetworkConnectionAvailable()).thenReturn(false)
        presenter!!.updateItems()

        // Progress is showed by pull to refresh mView automatically
        verify(mockedView!!).showErrorToast("There is no internet.")
        //verifyNoMoreInteractions(mockedView!!);
    }

    //<editor-fold desc="Basic Tests">
    @Test
    fun onCreate_setup_and_loadEvents() {
        // TODO: Fix when spy can be used with kotlin
        //        val spiedpresenter = spy(presenter)
        //
        //        `when`(mockedDateManager!!.getTodayDay()).thenReturn(1)
        //        `when`(mockedDateManager!!.getTodayMonth()).thenReturn(2)
        //        `when`(mockedEventRepository!!.get(1, 2)).thenReturn(Observable.just(events(2)))
        //
        //        spiedpresenter!!.onCreate(mockedView!!, mockedEventRepository!!, mockedNetworkChecker!!)
        //
        //        verify(spiedpresenter).loadEvents(true)
    }

    @Test
    fun onCreate_ifAlreadyHaveEvents_setup_and_updateView() {
        val events = events(2)

        `when`(mockedDateManager!!.getTodayDay()).thenReturn(1)
        `when`(mockedDateManager!!.getTodayMonth()).thenReturn(2)
        `when`(mockedEventRepository!!.get(1, 2)).thenReturn(Observable.just(events))

        presenter!!.onCreate(mockedView!!, mockedEventRepository!!, mockedNetworkChecker!!)
        presenter!!.onDestroy()
        presenter!!.onCreate(mockedView!!, mockedEventRepository!!, mockedNetworkChecker!!)

        //        verify(spiedPresenter!!, times(1)).loadEvents(true)
        verify(mockedView!!, times(2)).showEvents(events)
    }

    @Test
    fun onDestroy_freeResources() {
        `when`(mockedDateManager!!.getTodayDay()).thenReturn(1)
        `when`(mockedDateManager!!.getTodayMonth()).thenReturn(2)
        `when`(mockedEventRepository!!.get(1, 2)).thenReturn(Observable.just(events(2)))

        presenter!!.onCreate(mockedView!!, mockedEventRepository!!, mockedNetworkChecker!!)
        presenter!!.onDestroy()

        assertThat(presenter!!.mView).isNull()
        assertThat(presenter!!.mCompositeSubscription!!.hasSubscriptions()).isFalse()
    }
    //</editor-fold>

    @Test
    fun onCreate_showSongList() {
        val events = events(2)

        `when`(mockedDateManager!!.getTodayDay()).thenReturn(1)
        `when`(mockedDateManager!!.getTodayMonth()).thenReturn(2)
        `when`(mockedEventRepository!!.get(1, 2)).thenReturn(Observable.just(events))

        presenter!!.onCreate(mockedView!!, mockedEventRepository!!, mockedNetworkChecker!!)

        verify(mockedView!!).showEmptyViewProgress()
        verify(mockedView!!).dismissEmptyViewProgress()
        verify(mockedView!!).showEvents(events)
    }

    @Test
    fun onCreate_ifEmptyResponse_showEmptyView() {
        val events = events(0)
        `when`(mockedDateManager!!.getTodayDay()).thenReturn(1)
        `when`(mockedDateManager!!.getTodayMonth()).thenReturn(2)
        `when`(mockedEventRepository!!.get(1, 2)).thenReturn(Observable.just(events))

        presenter!!.onCreate(mockedView!!, mockedEventRepository!!, mockedNetworkChecker!!)

        verify(mockedView!!).showEmptyViewProgress()
        verify(mockedView!!).dismissEmptyViewProgress()
        verify(mockedView!!).showEmptyView()
    }

    @Test
    fun onCreate_onError_showErrorView() {
        val error = apiException()

        `when`(mockedDateManager!!.getTodayDay()).thenReturn(1)
        `when`(mockedDateManager!!.getTodayMonth()).thenReturn(2)
        `when`(mockedEventRepository!!.get(1, 2)).thenReturn(Observable.error<List<Event>>(error))

        presenter!!.onCreate(mockedView!!, mockedEventRepository!!, mockedNetworkChecker!!)

        verify(mockedView!!).showEmptyViewProgress()
        verify(mockedView!!).dismissEmptyViewProgress()
        verify(mockedView!!).hideEmptyView()
        verify(mockedView!!).hideEvents()
        verify(mockedView!!).showErrorView(error.name, error.text)
    }

    @Test
    fun onLoad_emptyViewVisible_showEventsList() {
        val events = events(0)
        val newEvents = events(5)

        `when`(mockedDateManager!!.getTodayDay()).thenReturn(1)
        `when`(mockedDateManager!!.getTodayMonth()).thenReturn(2)
        `when`(mockedEventRepository!!.get(1, 2)).thenReturn(Observable.just(events))

        presenter!!.onCreate(mockedView!!, mockedEventRepository!!, mockedNetworkChecker!!)

        `when`(mockedDateManager!!.getTodayDay()).thenReturn(1)
        `when`(mockedDateManager!!.getTodayMonth()).thenReturn(2)
        `when`(mockedEventRepository!!.get(1, 2)).thenReturn(Observable.just(newEvents))
        presenter!!.updateItems()

        verify(mockedView!!).showReloadProgress()
        verify(mockedView!!, times(2)).dismissReloadProgress()
        verify(mockedView!!, times(2)).hideEmptyView()
        verify(mockedView!!, times(2)).hideErrorView()
        verify(mockedView!!, times(1)).showEmptyView()
        verify(mockedView!!).showEvents(newEvents)
    }

    @Test
    fun onLoad_ifEmptyViewVisible_onError_showErrorView() {
        val events = events(0)
        val error = apiException()

        `when`(mockedDateManager!!.getTodayDay()).thenReturn(1)
        `when`(mockedDateManager!!.getTodayMonth()).thenReturn(2)
        `when`(mockedEventRepository!!.get(1, 2)).thenReturn(Observable.just(events))

        presenter!!.onCreate(mockedView!!, mockedEventRepository!!, mockedNetworkChecker!!)

        `when`(mockedEventRepository!!.get(1, 2)).thenReturn(Observable.error<List<Event>>(error))

        presenter!!.updateItems()

        // Progress is showed by pull to refresh mView automatically
        verify(mockedView!!).showReloadProgress()
        verify(mockedView!!, times(2)).dismissReloadProgress()
        verify(mockedView!!, times(2)).hideEmptyView()
        verify(mockedView!!).hideEvents()
        verify(mockedView!!).showErrorView(error.name, error.text)
    }

    @Test
    fun onLoad_ifErrorViewVisible_showeventsList() {
        val error = apiException()

        `when`(mockedDateManager!!.getTodayDay()).thenReturn(1)
        `when`(mockedDateManager!!.getTodayMonth()).thenReturn(2)
        `when`(mockedEventRepository!!.get(1, 2)).thenReturn(Observable.error<List<Event>>(error))

        presenter!!.onCreate(mockedView!!, mockedEventRepository!!, mockedNetworkChecker!!)

        val newEvents = events(5)
        `when`(mockedEventRepository!!.get(1, 2)).thenReturn(Observable.just(newEvents))
        presenter!!.updateItems()

        verify(mockedView!!).showReloadProgress()
        verify(mockedView!!, times(2)).dismissReloadProgress()
        verify(mockedView!!).hideErrorView()
        verify(mockedView!!, times(2)).hideEmptyView()
        verify(mockedView!!).showEvents(newEvents)
    }

    @Test
    fun onLoad_ifEventListVisible_loadNewItems() {
        val events = events(2)

        `when`(mockedDateManager!!.getTodayDay()).thenReturn(1)
        `when`(mockedDateManager!!.getTodayMonth()).thenReturn(2)
        `when`(mockedEventRepository!!.get(1, 2)).thenReturn(Observable.just(events))

        presenter!!.onCreate(mockedView!!, mockedEventRepository!!, mockedNetworkChecker!!)

        val newEvents = events(5)
        `when`(mockedEventRepository!!.get(1, 2)).thenReturn(Observable.just(newEvents))
        presenter!!.updateItems()

        // Progress is showed by pull to refresh mView automatically
        verify(mockedView!!).showReloadProgress()
        verify(mockedView!!, times(2)).dismissReloadProgress()
        verify(mockedView!!).showEvents(newEvents)
    }

    @Test
    fun onLoad_ifEventsListVisible_onError_showErrorDialog() {
        val events = events(2)
        val error = apiException()

        `when`(mockedDateManager!!.getTodayDay()).thenReturn(1)
        `when`(mockedDateManager!!.getTodayMonth()).thenReturn(2)
        `when`(mockedEventRepository!!.get(1, 2)).thenReturn(Observable.just(events))

        presenter!!.onCreate(mockedView!!, mockedEventRepository!!, mockedNetworkChecker!!)

        `when`(mockedEventRepository!!.get(1, 2)).thenReturn(Observable.error<List<Event>>(error))

        presenter!!.updateItems()

        // Progress is showed by pull to refresh mView automatically
        verify(mockedView!!).showReloadProgress()
        verify(mockedView!!, times(2)).dismissReloadProgress()
        verify(mockedView!!).showErrorToast(anyString())
        verify(mockedView!!, times(0)).showErrorView(anyString(), anyString())
        verify(mockedView!!, times(0)).hideEvents()
    }

    @Test
    fun onLoad_ifEventsListVisible_ifLoadEmpty_showDialogInsteadOfEmptyView() {
        val events = events(2)
        val newEvents = events(0)

        `when`(mockedDateManager!!.getTodayDay()).thenReturn(1)
        `when`(mockedDateManager!!.getTodayMonth()).thenReturn(2)
        `when`(mockedEventRepository!!.get(1, 2)).thenReturn(Observable.just(events))

        presenter!!.onCreate(mockedView!!, mockedEventRepository!!, mockedNetworkChecker!!)

        `when`(mockedEventRepository!!.get(1, 2)).thenReturn(Observable.just(newEvents))

        presenter!!.updateItems()

        verify(mockedView!!).showErrorToast(anyString())
        verify(mockedView!!).showReloadProgress()
        verify(mockedView!!, times(2)).dismissReloadProgress()
        verify(mockedView!!, times(0)).showEmptyView()
        verify(mockedView!!, times(1)).showEvents(events) // 1 time is onCreate and then on updateItems
    }
}
