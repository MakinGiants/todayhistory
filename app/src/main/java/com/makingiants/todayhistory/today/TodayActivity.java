package com.makingiants.todayhistory.today;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import butterknife.Bind;
import com.makingiants.today.api.repository.history.HistoryRepository;
import com.makingiants.today.api.repository.history.pojo.Event;
import com.makingiants.todayhistory.R;
import com.makingiants.todayhistory.utils.DateManager;
import com.makingiants.todayhistory.utils.refresh_layout.CustomScrollSwipeRefreshLayout;
import com.makingiants.todayhistory.utils.refresh_layout.ScrollEnabler;
import java.util.List;

public class TodayActivity extends TodayView
    implements SwipeRefreshLayout.OnRefreshListener, ScrollEnabler {
  @Bind(R.id.today_recycler) RecyclerView mRecyclerView;
  @Bind(R.id.today_layout_refresh) CustomScrollSwipeRefreshLayout mSwipeRefreshLayout;
  @Bind(R.id.today_view_progress) View mEmptyViewProgress;
  @Bind(R.id.today_view_empty) View mEmptyView;
  @Bind(R.id.today_view_error) View mErrorView;
  @Bind(R.id.today_text_error_title) TextView mErrorTitleTextView;
  @Bind(R.id.today_text_error_message) TextView mErrorMessageTextView;

  private TodayPresenter mPresenter;
  private TodayAdapter mAdapter;

  //<editor-fold desc="Activity">

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.today_activity);
    activateToolbar(R.string.title_activity_today);

    mAdapter = new TodayAdapter();
    mRecyclerView.setAdapter(mAdapter);
    mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    mSwipeRefreshLayout.setOnRefreshListener(this);
    mSwipeRefreshLayout.setScrollEnabler(this);
    mSwipeRefreshLayout.setColorSchemeColors(R.color.colorAccent, R.color.colorPrimary);

    if (mPresenter == null) {
      mPresenter = new TodayPresenter(new HistoryRepository(), new DateManager());
    }

    mPresenter.onCreate(this);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    mPresenter.onDestroy();

    if (mSwipeRefreshLayout != null) {
      mSwipeRefreshLayout.setScrollEnabler(null);
      mSwipeRefreshLayout.setOnRefreshListener(null);
    }
  }
  //</editor-fold>

  //<editor-fold desc="TodayView">
  @Override
  public void showEvents(List<Event> events) {
    mRecyclerView.setVisibility(View.VISIBLE);
    mAdapter.setEvents(events);
  }

  @Override
  public void hideEvents() {
    mRecyclerView.setVisibility(View.GONE);
  }

  @Override
  public void showEmptyViewProgress() {
    mEmptyViewProgress.setVisibility(View.VISIBLE);
  }

  @Override
  public void dismissEmptyViewProgress() {
    mEmptyViewProgress.setVisibility(View.GONE);
  }

  @Override
  public void showReloadProgress() {
    mSwipeRefreshLayout.setRefreshing(true);
  }

  @Override
  public void showErrorView(String title, String message) {
    mErrorTitleTextView.setText(title);
    mErrorMessageTextView.setText(message);
    mErrorView.setVisibility(View.VISIBLE);
  }

  @Override
  public void hideErrorView() {
    mErrorView.setVisibility(View.GONE);
  }

  @Override
  public void showErrorToast(String message) {
    showToast(message);
  }

  @Override
  public void dismissReloadProgress() {
    mSwipeRefreshLayout.setRefreshing(false);
  }

  @Override
  public void showEmptyView() {
    mEmptyView.setVisibility(View.VISIBLE);
  }

  @Override
  public void hideEmptyView() {
    mEmptyView.setVisibility(View.GONE);
  }
  //</editor-fold>

  //<editor-fold desc="SwipeRefreshLayout.OnRefreshListener">
  @Override
  public void onRefresh() {
    mPresenter.updateItems();
  }
  //</editor-fold>

  //<editor-fold desc="ScrollEnabler">
  @Override
  public boolean canScrollUp() {
    return mRecyclerView.getVisibility() == View.VISIBLE && mRecyclerView.canScrollVertically(-1);
  }
  //</editor-fold>
}