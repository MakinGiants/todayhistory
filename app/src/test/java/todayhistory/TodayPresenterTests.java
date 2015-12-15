package todayhistory;

import com.makingiants.today.api.error_handling.ApiException;
import com.makingiants.today.api.repository.history.HistoryRepository;
import com.makingiants.today.api.repository.history.pojo.Event;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import rx.Observable;
import todayhistory.api.MockHistory;
import todayhistory.api.error_handling.MockApiException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TodayPresenterTests {
  @Mock TodayView mockedView;
  @Mock HistoryRepository mockedRepository;
  @Mock DateManager mockedDateManager;
  private TodayPresenter presenter;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    presenter = new TodayPresenter(mockedRepository, mockedDateManager);
  }

  @Test
  public void onDestroy_releaseResources() {
    List<Event> events = MockHistory.events(2);

    when(mockedRepository.get(anyInt(), anyInt())).thenReturn(Observable.just(events));
    when(mockedDateManager.getTodayDay()).thenReturn(1);
    when(mockedDateManager.getTodayMonth()).thenReturn(10);

    presenter.onCreate(mockedView);
    presenter.onDestroy();

    assertThat(presenter.getView()).isNull();
    assertThat(presenter.getCompositeSubscription().hasSubscriptions()).isFalse();
  }

  @Test
  public void onCreate_loadData_ifSuccess_showList() {
    List<Event> events = MockHistory.events(2);

    when(mockedRepository.get(anyInt(), anyInt())).thenReturn(Observable.just(events));
    when(mockedDateManager.getTodayDay()).thenReturn(1);
    when(mockedDateManager.getTodayMonth()).thenReturn(10);

    presenter.onCreate(mockedView);

    verify(mockedView).showProgress();
    verify(mockedView).dismissProgress();
    verify(mockedView).showEvents(events);
  }

  @Test
  public void onCreate_loadData_ifError_showError() {
    ApiException apiException = MockApiException.apiException();

    when(mockedRepository.get(anyInt(), anyInt())).thenReturn(Observable.error(apiException));
    when(mockedDateManager.getTodayDay()).thenReturn(1);
    when(mockedDateManager.getTodayMonth()).thenReturn(10);

    presenter.onCreate(mockedView);

    verify(mockedRepository).get(1, 10);
    verify(mockedView).showProgress();
    verify(mockedView).dismissProgress();
    verify(mockedView).showError(apiException.getMessage(), apiException.getName());
  }
}
