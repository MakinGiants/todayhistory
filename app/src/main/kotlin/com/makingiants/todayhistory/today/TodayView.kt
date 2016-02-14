package com.makingiants.todayhistory.today

import com.makingiants.today.api.repository.history.pojo.Event

interface TodayView {
    fun initViews()

    fun showEvents(events: List<Event>)

    fun hideEvents()

    fun showEmptyViewProgress()

    fun dismissEmptyViewProgress()

    fun dismissReloadProgress()

    fun showEmptyView()

    fun hideEmptyView()

    fun showReloadProgress()

    fun showErrorView(title: String, message: String)

    fun hideErrorView()

    fun showErrorToast(message: String)

    fun showErrorDialog(throwable: Throwable)
}