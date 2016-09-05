package com.makingiants.todayhistory.screens.today

import com.makingiants.today.api.error_handling.ApiError
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

  fun showErrorView(apiError: ApiError)

  fun hideErrorView()

  fun showErrorToast(apiError: ApiError)

  fun showErrorDialog(apiError: ApiError)

  fun showErrorEmptyItemsToast()
}