package com.makingiants.todayhistory.today

import com.makingiants.today.api.repository.history.pojo.Event
import com.makingiants.todayhistory.base.BaseActivityView

abstract class TodayView : BaseActivityView() {
    abstract fun showEvents(events: List<Event>)

    abstract fun hideEvents()

    abstract fun showEmptyViewProgress()

    abstract fun dismissEmptyViewProgress()

    abstract fun dismissReloadProgress()

    abstract fun showEmptyView()

    abstract fun hideEmptyView()

    abstract fun showReloadProgress()

    abstract fun showErrorView(title: String, message: String)

    abstract fun hideErrorView()

    abstract fun showErrorToast(message: String)
}