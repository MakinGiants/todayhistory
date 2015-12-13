package todayhistory;

import android.support.annotation.VisibleForTesting;
import com.makingiants.today.api.error_handling.ApiException;
import com.makingiants.today.api.repository.history.HistoryRepository;
import com.makingiants.todayhistory.utils.Transformer;
import rx.subscriptions.CompositeSubscription;

public class TodayPresenter {
  private HistoryRepository mHistoryRepository;
  private DateManager mDateManager;
  private TodayView mView;
  private CompositeSubscription mCompositeSubscription;

  public TodayPresenter(HistoryRepository historyRepository, DateManager dateManager) {
    mHistoryRepository = historyRepository;
    mDateManager = dateManager;
  }

  public void onCreate(TodayView view) {
    mCompositeSubscription = new CompositeSubscription();
    mView = view;

    loadItems();
  }

  public void onDestroy() {
    mView = null;
    if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
      mCompositeSubscription.unsubscribe();
    }
  }

  public void loadItems() {

    mView.showProgress();
    mHistoryRepository.get(mDateManager.getTodayDay(), mDateManager.getTodayMonth())
        .compose(Transformer.applyIoSchedulers())
        .subscribe(events -> {
              mView.dismissProgress();
              mView.showEvents(events);
            },
            error -> {
              mView.dismissProgress();
              ApiException apiException = ApiException.from(error);
              mView.showError(apiException.getMessage(), apiException.getName());
            });
  }

  @VisibleForTesting
  TodayView getView() {
    return mView;
  }

  @VisibleForTesting
  CompositeSubscription getCompositeSubscription() {
    return mCompositeSubscription;
  }
}
