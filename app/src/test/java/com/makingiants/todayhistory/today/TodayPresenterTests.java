package com.makingiants.todayhistory.today;

import com.makingiants.today.api.error_handling.ApiException;
import com.makingiants.today.api.repository.history.HistoryRepository;
import com.makingiants.today.api.repository.history.pojo.Event;
import com.makingiants.todayhistory.utils.DateManager;
import com.makingiants.todayhistory.utils.NetworkChecker;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import rx.Observable;

import static com.makingiants.todayhistory.api.MockHistory.events;
import static com.makingiants.todayhistory.api.error_handling.MockApiException.apiException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TodayPresenterTests {
  @Mock HistoryRepository mockedEventRepository;
  @Mock TodayView mockedView;
  @Mock DateManager mockedDateManager;
  @Mock NetworkChecker mockedNetworkChecker;

  TodayPresenter presenter;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    presenter = new TodayPresenter(mockedDateManager);
    when(mockedNetworkChecker.isNetworkConnectionAvailable()).thenReturn(true);
  }

  @Test
  public void onLoad_ifNoInternet_showError() {
    List<Event> events = events(2);

    when(mockedDateManager.getTodayDay()).thenReturn(1);
    when(mockedDateManager.getTodayMonth()).thenReturn(2);
    when(mockedEventRepository.get(1, 2)).thenReturn(Observable.just(events));

    presenter.onCreate(mockedView, mockedEventRepository, mockedNetworkChecker);

    List<Event> newEvents = events(5);
    when(mockedEventRepository.get(1, 2)).thenReturn(Observable.just(newEvents));
    when(mockedNetworkChecker.isNetworkConnectionAvailable()).thenReturn(false);
    presenter.updateItems();

    // Progress is showed by pull to refresh mView automatically
    verify(mockedView).showErrorToast("There is no internet.");
    //verifyNoMoreInteractions(mockedView);
  }

  //<editor-fold desc="Basic Tests">
  @Test
  public void onCreate_setup_and_loadEvents() {
    TodayPresenter spiedPresenter = spy(presenter);

    when(mockedDateManager.getTodayDay()).thenReturn(1);
    when(mockedDateManager.getTodayMonth()).thenReturn(2);
    when(mockedEventRepository.get(1, 2)).thenReturn(Observable.just(events(2)));

    spiedPresenter.onCreate(mockedView, mockedEventRepository, mockedNetworkChecker);

    verify(spiedPresenter).loadEvents(true);
  }

  @Test
  public void onCreate_ifAlreadyHaveevents_setup_and_updateView() {
    TodayPresenter spiedPresenter = spy(presenter);
    List<Event> events = events(2);

    when(mockedDateManager.getTodayDay()).thenReturn(1);
    when(mockedDateManager.getTodayMonth()).thenReturn(2);
    when(mockedEventRepository.get(1, 2)).thenReturn(Observable.just(events));

    spiedPresenter.onCreate(mockedView, mockedEventRepository, mockedNetworkChecker);
    spiedPresenter.onDestroy();

    spiedPresenter.onCreate(mockedView, mockedEventRepository, mockedNetworkChecker);

    verify(spiedPresenter, times(1)).loadEvents(true);
    verify(mockedView, times(2)).showEvents(events);
  }

  @Test
  public void onDestroy_freeResources() {
    when(mockedDateManager.getTodayDay()).thenReturn(1);
    when(mockedDateManager.getTodayMonth()).thenReturn(2);
    when(mockedEventRepository.get(1, 2)).thenReturn(Observable.just(events(2)));

    presenter.onCreate(mockedView, mockedEventRepository, mockedNetworkChecker);
    presenter.onDestroy();

    assertThat(presenter.getView()).isNull();
    assertThat(presenter.getCompositeSubscription().hasSubscriptions()).isFalse();
  }
  //</editor-fold>

  @Test
  public void onCreate_showSongList() {
    List<Event> events = events(2);

    when(mockedDateManager.getTodayDay()).thenReturn(1);
    when(mockedDateManager.getTodayMonth()).thenReturn(2);
    when(mockedEventRepository.get(1, 2)).thenReturn(Observable.just(events));

    presenter.onCreate(mockedView, mockedEventRepository, mockedNetworkChecker);

    verify(mockedView).showEmptyViewProgress();
    verify(mockedView).dismissEmptyViewProgress();
    verify(mockedView).showEvents(events);
  }

  @Test
  public void onCreate_ifEmptyResponse_showEmptyView() {
    List<Event> events = events(0);
    when(mockedDateManager.getTodayDay()).thenReturn(1);
    when(mockedDateManager.getTodayMonth()).thenReturn(2);
    when(mockedEventRepository.get(1, 2)).thenReturn(Observable.just(events));

    presenter.onCreate(mockedView, mockedEventRepository, mockedNetworkChecker);

    verify(mockedView).showEmptyViewProgress();
    verify(mockedView).dismissEmptyViewProgress();
    verify(mockedView).showEmptyView();
  }

  @Test
  public void onCreate_onError_showErrorView() {
    ApiException error = apiException();

    when(mockedDateManager.getTodayDay()).thenReturn(1);
    when(mockedDateManager.getTodayMonth()).thenReturn(2);
    when(mockedEventRepository.get(1, 2)).thenReturn(Observable.error(error));

    presenter.onCreate(mockedView, mockedEventRepository, mockedNetworkChecker);

    verify(mockedView).showEmptyViewProgress();
    verify(mockedView).dismissEmptyViewProgress();
    verify(mockedView).hideEmptyView();
    verify(mockedView).hideEvents();
    verify(mockedView).showErrorView(error.getName(), error.getMessage());
  }

  @Test
  public void onLoad_emptyViewVisible_showeventsList() {
    List<Event> events = events(0);
    List<Event> newEvents = events(5);

    when(mockedDateManager.getTodayDay()).thenReturn(1);
    when(mockedDateManager.getTodayMonth()).thenReturn(2);
    when(mockedEventRepository.get(1, 2)).thenReturn(Observable.just(events));

    presenter.onCreate(mockedView, mockedEventRepository, mockedNetworkChecker);

    when(mockedDateManager.getTodayDay()).thenReturn(1);
    when(mockedDateManager.getTodayMonth()).thenReturn(2);
    when(mockedEventRepository.get(1, 2)).thenReturn(Observable.just(newEvents));
    presenter.updateItems();

    verify(mockedView).showReloadProgress();
    verify(mockedView).dismissReloadProgress();
    verify(mockedView, times(2)).hideEmptyView();
    verify(mockedView, times(2)).hideErrorView();
    verify(mockedView, times(1)).showEmptyView();
    verify(mockedView).showEvents(newEvents);
  }

  @Test
  public void onLoad_ifEmptyViewVisible_onError_showErrorView() {
    List<Event> events = events(0);
    ApiException error = apiException();

    when(mockedDateManager.getTodayDay()).thenReturn(1);
    when(mockedDateManager.getTodayMonth()).thenReturn(2);
    when(mockedEventRepository.get(1, 2)).thenReturn(Observable.just(events));

    presenter.onCreate(mockedView, mockedEventRepository, mockedNetworkChecker);

    when(mockedEventRepository.get(1, 2)).thenReturn(Observable.error(error));

    presenter.updateItems();

    // Progress is showed by pull to refresh mView automatically
    verify(mockedView).showReloadProgress();
    verify(mockedView).dismissReloadProgress();
    verify(mockedView, times(2)).hideEmptyView();
    verify(mockedView).hideEvents();
    verify(mockedView).showErrorView(error.getName(), error.getMessage());
  }

  @Test
  public void onLoad_ifErrorViewVisible_showeventsList() {
    ApiException error = apiException();

    when(mockedDateManager.getTodayDay()).thenReturn(1);
    when(mockedDateManager.getTodayMonth()).thenReturn(2);
    when(mockedEventRepository.get(1, 2)).thenReturn(Observable.error(error));

    presenter.onCreate(mockedView, mockedEventRepository, mockedNetworkChecker);

    List<Event> newEvents = events(5);
    when(mockedEventRepository.get(1, 2)).thenReturn(Observable.just(newEvents));
    presenter.updateItems();

    verify(mockedView).showReloadProgress();
    verify(mockedView).dismissReloadProgress();
    verify(mockedView).hideErrorView();
    verify(mockedView, times(2)).hideEmptyView();
    verify(mockedView).showEvents(newEvents);
  }

  @Test
  public void onLoad_ifEventListVisible_loadNewItems() {
    List<Event> events = events(2);

    when(mockedDateManager.getTodayDay()).thenReturn(1);
    when(mockedDateManager.getTodayMonth()).thenReturn(2);
    when(mockedEventRepository.get(1, 2)).thenReturn(Observable.just(events));

    presenter.onCreate(mockedView, mockedEventRepository, mockedNetworkChecker);

    List<Event> newEvents = events(5);
    when(mockedEventRepository.get(1, 2)).thenReturn(Observable.just(newEvents));
    presenter.updateItems();

    // Progress is showed by pull to refresh mView automatically
    verify(mockedView).showReloadProgress();
    verify(mockedView).dismissReloadProgress();
    verify(mockedView).showEvents(newEvents);
  }

  @Test
  public void onLoad_ifEventsListVisible_onError_showErrorDialog() {
    List<Event> events = events(2);
    ApiException error = apiException();

    when(mockedDateManager.getTodayDay()).thenReturn(1);
    when(mockedDateManager.getTodayMonth()).thenReturn(2);
    when(mockedEventRepository.get(1, 2)).thenReturn(Observable.just(events));

    presenter.onCreate(mockedView, mockedEventRepository, mockedNetworkChecker);

    when(mockedEventRepository.get(1, 2)).thenReturn(Observable.error(error));

    presenter.updateItems();

    // Progress is showed by pull to refresh mView automatically
    verify(mockedView).showReloadProgress();
    verify(mockedView).dismissReloadProgress();
    verify(mockedView).showErrorToast(anyString());
    verify(mockedView, times(0)).showErrorView(anyString(), anyString());
    verify(mockedView, times(0)).hideEvents();
  }

  @Test
  public void onLoad_ifEventsListVisible_ifLoadEmpty_showDialogInsteadOfEmptyView() {
    List<Event> events = events(2);
    List<Event> newEvents = events(0);

    when(mockedDateManager.getTodayDay()).thenReturn(1);
    when(mockedDateManager.getTodayMonth()).thenReturn(2);
    when(mockedEventRepository.get(1, 2)).thenReturn(Observable.just(events));

    presenter.onCreate(mockedView, mockedEventRepository, mockedNetworkChecker);

    when(mockedEventRepository.get(1, 2)).thenReturn(Observable.just(newEvents));

    presenter.updateItems();

    verify(mockedView).showErrorToast(anyString());
    verify(mockedView).showReloadProgress();
    verify(mockedView).dismissReloadProgress();
    verify(mockedView, times(0)).showEmptyView();
    verify(mockedView, times(1)).showEvents(
        events); // 1 time is onCreate and then on updateItems
  }
}
