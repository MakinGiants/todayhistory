package com.makingiants.todayhistory.today;

import android.support.annotation.VisibleForTesting;
import com.makingiants.today.api.error_handling.ApiException;
import com.makingiants.today.api.repository.history.HistoryRepository;
import com.makingiants.today.api.repository.history.pojo.Event;
import com.makingiants.todayhistory.utils.DateManager;
import com.makingiants.todayhistory.utils.Transformer;
import java.util.List;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class TodayPresenter {
  private HistoryRepository mHistoryRepository;
  private DateManager mDateManager;
  private CompositeSubscription mCompositeSubscription;
  private List<Event> mEvents;
  private TodayView mView;

  public TodayPresenter(HistoryRepository historyRepository, DateManager dateManager) {
    super();
    mHistoryRepository = historyRepository;
    mDateManager = dateManager;
  }

  public void onCreate(TodayView view) {
    mView = view;
    mCompositeSubscription = new CompositeSubscription();

    if (mEvents == null) {
      loadSongs(true);
    } else {
      view.showEvents(mEvents);
    }
  }

  public void onDestroy() {
    mView = null;

    if (mCompositeSubscription.hasSubscriptions()) {
      mCompositeSubscription.unsubscribe();
    }
  }

  public void updateItems() {
    loadSongs(false);
  }

  @VisibleForTesting
  TodayView getView() {
    return mView;
  }

  @VisibleForTesting
  CompositeSubscription getCompositeSubscription() {
    return mCompositeSubscription;
  }

  @VisibleForTesting
  void loadSongs(boolean isFistTime) {
    if (isFistTime) {
      mView.showEmptyViewProgress();
    } else {
      mView.showReloadProgress();
    }

    Subscription subscription =
        mHistoryRepository.get(mDateManager.getTodayDay(), mDateManager.getTodayMonth())
            .compose(Transformer.applyIoSchedulers())
            .subscribe(events -> {
              mView.hideErrorView();
              mView.hideEmptyView();

              if (isFistTime) {
                mView.dismissEmptyViewProgress();
              } else {
                mView.dismissReloadProgress();
              }

              if (events.isEmpty()) {
                if (mEvents == null) {
                  mView.showEmptyView();
                } else {
                  // TODO: move the string to strings and show from view
                  mView.showErrorToast("The loaded list is empty, retry latter.");
                }
              } else {
                mEvents = events;
                mView.showEvents(events);
              }
            }, error -> {
              mView.hideEmptyView();

              if (isFistTime) {
                mView.dismissEmptyViewProgress();
              } else {
                mView.dismissReloadProgress();
              }

              if (mEvents == null) {
                if (isFistTime || mEvents == null || mEvents.isEmpty()) {
                  ApiException apiException = ApiException.from(error);
                  mView.showErrorView(apiException.getName(), apiException.getMessage());
                  mView.hideEvents();
                } else {
                  mView.showError(error);
                }
              } else {
                ApiException apiException = ApiException.from(error);
                mView.showErrorToast(apiException.getMessage());
              }
            });

    mCompositeSubscription.add(subscription);
  }
}
