package com.makingiants.todayhistory.today

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.makingiants.today.api.repository.history.HistoryRepository
import com.makingiants.today.api.repository.history.pojo.Event
import com.makingiants.todayhistory.R
import com.makingiants.todayhistory.base.BaseActivityView
import com.makingiants.todayhistory.utils.DateManager
import com.makingiants.todayhistory.utils.NetworkChecker
import com.makingiants.todayhistory.utils.SpacesItemDecoration
import com.makingiants.todayhistory.utils.refresh_layout.ScrollEnabler
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.today_activity.*
import timber.log.Timber

class TodayActivity : BaseActivityView(), TodayView, SwipeRefreshLayout.OnRefreshListener, ScrollEnabler {
    val mPresenter: TodayPresenter by lazy { TodayPresenter(DateManager()) }
    val mAdapter: TodayAdapter by lazy { TodayAdapter(Picasso.with(applicationContext)) }

    //<editor-fold desc="Activity">

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.today_activity)
        activateToolbar(R.string.app_name)

        Timber.tag("TodayActivity")
        mPresenter.onCreate(this, HistoryRepository(), NetworkChecker(applicationContext))
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.onDestroy()

        swipeRefreshLayout.setScrollEnabler(null)
        swipeRefreshLayout.setOnRefreshListener(null)
    }
    //</editor-fold>

    //<editor-fold desc="TodayView">
    override fun initViews() {
        recyclerView.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(applicationContext)
            itemAnimator = DefaultItemAnimator()
            addItemDecoration(SpacesItemDecoration(16))
        }

        swipeRefreshLayout.apply {
            setOnRefreshListener(this@TodayActivity)
            setScrollEnabler(this@TodayActivity)
            setColorSchemeColors(R.color.colorAccent, R.color.colorPrimary)
        }
    }

    override fun showEvents(events: List<Event>) {
        recyclerView.visibility = View.VISIBLE
        mAdapter.setEvents(events)
    }

    override fun hideEvents() = recyclerView.setVisibility(View.GONE)

    override fun showEmptyViewProgress() = progressView.setVisibility(View.VISIBLE)

    override fun dismissEmptyViewProgress() = progressView.setVisibility(View.GONE)

    override fun showReloadProgress() = swipeRefreshLayout.setRefreshing(true)

    override fun dismissReloadProgress() = swipeRefreshLayout.setRefreshing(false)

    override fun showErrorView(title: String, message: String) {
        errorTitleView.text = title
        errorMessageTextView.text = message
        errorView.visibility = View.VISIBLE
    }

    override fun hideErrorView() = errorView.setVisibility(View.GONE)

    override fun showErrorToast(message: String) = showToast(message)

    override fun showEmptyView() = emptyView.setVisibility (View.VISIBLE)

    override fun hideEmptyView() = emptyView.setVisibility(View.GONE)

    override fun showErrorDialog(throwable: Throwable) = super.showError(throwable)

    //</editor-fold>

    //<editor-fold desc="SwipeRefreshLayout.OnRefreshListener">
    override fun onRefresh() = mPresenter.onRefresh()
    //</editor-fold>

    //<editor-fold desc="ScrollEnabler">
    override fun canScrollUp(): Boolean =
            recyclerView.visibility === View.VISIBLE && recyclerView.canScrollVertically(-1)
    //</editor-fold>
}
