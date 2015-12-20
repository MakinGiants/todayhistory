package com.makingiants.todayhistory.today;

import com.makingiants.today.api.repository.history.pojo.Event;
import com.makingiants.todayhistory.base.BaseActivityView;
import java.util.List;

public abstract class TodayView extends BaseActivityView {
  public abstract void showEvents(List<Event> events);

  public abstract void hideEvents();

  public abstract void showEmptyViewProgress();

  public abstract void dismissEmptyViewProgress();

  public abstract void dismissReloadProgress();

  public abstract void showEmptyView();

  public abstract void hideEmptyView();

  public abstract void showReloadProgress();

  public abstract void showErrorView(String title, String message);

  public abstract void hideErrorView();

  public abstract void showErrorToast(String message);
}