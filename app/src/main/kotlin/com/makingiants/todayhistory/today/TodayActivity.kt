package com.makingiants.todayhistory.today

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.makingiants.today.api.repository.history.HistoryRepositoryImpl
import com.makingiants.today.api.repository.history.pojo.Event
import com.makingiants.todayhistory.R
import com.makingiants.todayhistory.utils.AndroidDateManager
import com.makingiants.todayhistory.utils.NetworkCheckerImpl
import com.makingiants.todayhistory.utils.SpacesItemDecoration
import com.makingiants.todayhistory.utils.refresh_layout.ScrollEnabler
import com.squareup.picasso.Picasso
import icepick.State
import kotlinx.android.synthetic.main.today_activity.*

class TodayActivity : TodayView(), SwipeRefreshLayout.OnRefreshListener, ScrollEnabler {
    @State internal var mPresenter: TodayPresenter? = null
    private var mAdapter: TodayAdapter? = null

    //<editor-fold desc="Activity">

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.today_activity)
        activateToolbar(R.string.title_activity_today)

        mAdapter = TodayAdapter(Picasso.with(getApplicationContext()))


        recycler.setAdapter(mAdapter)
        recycler.setLayoutManager(LinearLayoutManager(getApplicationContext()))
        recycler.addItemDecoration(SpacesItemDecoration(16))

        swipeRefreshLayout.setOnRefreshListener(this)
        swipeRefreshLayout.setScrollEnabler(this)
        swipeRefreshLayout.setColorSchemeColors(R.color.colorAccent, R.color.colorPrimary)

        if (mPresenter == null) {
            mPresenter = TodayPresenter(AndroidDateManager())
        }

        mPresenter?.onCreate(this, HistoryRepositoryImpl(), NetworkCheckerImpl(getApplicationContext()))
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter?.onDestroy()

        swipeRefreshLayout?.setScrollEnabler(null)
        swipeRefreshLayout?.setOnRefreshListener(null)
    }
    //</editor-fold>

    //<editor-fold desc="TodayView">
    override fun showEvents(events: List<Event>) {
        recycler?.setVisibility(View.VISIBLE)
        mAdapter?.setEvents(events)
    }

    override fun hideEvents() {
        recycler?.setVisibility(View.GONE)
    }

    override fun showEmptyViewProgress() {
        progressView?.visibility = View.VISIBLE
    }

    override fun dismissEmptyViewProgress() {
        emptyView?.visibility = View.GONE
    }

    override fun showReloadProgress() {
        swipeRefreshLayout?.setRefreshing(true)
    }

    override fun showErrorView(title: String, message: String) {
        errorTitleView?.text = title
        errorMessageTextView?.text = message
        errorView?.visibility = View.VISIBLE
    }

    override fun hideErrorView() {
        errorView?.visibility = View.GONE
    }

    override fun showErrorToast(message: String) {
        showToast(message)
    }

    override fun dismissReloadProgress() {
        swipeRefreshLayout?.setRefreshing(false)
    }

    override fun showEmptyView() {
        emptyView?.visibility = View.VISIBLE
    }

    override fun hideEmptyView() {
        emptyView?.visibility = View.GONE
    }
    //</editor-fold>

    //<editor-fold desc="SwipeRefreshLayout.OnRefreshListener">
    override fun onRefresh() {
        mPresenter?.updateItems()
    }
    //</editor-fold>

    //<editor-fold desc="ScrollEnabler">
    override fun canScrollUp(): Boolean {
        return recycler?.getVisibility() === View.VISIBLE && recycler?.canScrollVertically(-1) ?: true
    }
    //</editor-fold>
}
