package com.makingiants.todayhistory.utils.refresh_layout;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Use {@link ScrollEnabler} to know when {@link CustomScrollSwipeRefreshLayout} can scroll up.
 * It's usefull when the mView have multiple inner views that are not inside a {@link ScrollView}
 */
public class CustomScrollSwipeRefreshLayout extends SwipeRefreshLayout {
  @Nullable private ScrollEnabler mScrollEnabler;

  public CustomScrollSwipeRefreshLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public void setScrollEnabler(@Nullable ScrollEnabler scrollEnabler) {
    mScrollEnabler = scrollEnabler;
  }

  @Override
  public boolean canChildScrollUp() {
    if (mScrollEnabler != null) {
      return mScrollEnabler.canScrollUp();
    }
    return super.canChildScrollUp();
  }
}
