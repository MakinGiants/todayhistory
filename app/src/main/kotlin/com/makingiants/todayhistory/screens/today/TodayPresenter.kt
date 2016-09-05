package com.makingiants.todayhistory.screens.today

import android.support.annotation.VisibleForTesting
import com.makingiants.today.api.Answer
import com.makingiants.today.api.error_handling.ApiError
import com.makingiants.today.api.error_handling.NoInternetApiError
import com.makingiants.today.api.repository.history.HistoryRepository
import com.makingiants.today.api.repository.history.pojo.Event
import com.makingiants.todayhistory.utils.DateManager
import com.makingiants.todayhistory.utils.NetworkChecker
import com.makingiants.todayhistory.utils.extensions.composeForIoTasks
import rx.subscriptions.CompositeSubscription

open class TodayPresenter(var dateManager: DateManager,
                          val historyRepository: HistoryRepository,
                          val networkChecker: NetworkChecker) {
  @VisibleForTesting var compositeSubscription: CompositeSubscription? = null
  @VisibleForTesting var view: TodayView? = null
  private var currentEvents: List<Event>? = null

  fun attach(view: TodayView) {
    this.view = view
    compositeSubscription = CompositeSubscription()

    view.initViews()
    if (currentEvents == null) {
      loadEvents(true)
    } else {
      view.showEvents(currentEvents!!)
    }
  }

  fun unAttach() {
    view = null

    if (compositeSubscription?.hasSubscriptions() ?: false) {
      compositeSubscription?.unsubscribe()
    }
  }

  fun onRefresh() = loadEvents()

  @VisibleForTesting
  fun loadEvents(isFistTime: Boolean = false) {
    if (!networkChecker.isNetworkConnectionAvailable()) {
      view?.showErrorToast(NoInternetApiError())
      return
    }

    if (isFistTime) {
      view?.showEmptyViewProgress()
    } else {
      view?.showReloadProgress()
    }

    val subscription = historyRepository.get(dateManager.getTodayDay(), dateManager.getTodayMonth())
        .composeForIoTasks()
        .subscribe({ answer: Answer<List<Event>> ->

          view?.apply {
            hideErrorView()
            hideEmptyView()
            dismissEmptyViewProgress()
            dismissReloadProgress()
          }

          val events = answer.body
          if (answer.error == null && events != null) {
            if (events.isEmpty()) {
              if (currentEvents == null) {
                view?.showEmptyView()
              } else {
                view?.showErrorEmptyItemsToast()
              }
            } else {
              currentEvents = events
              view?.showEvents(events)
            }
          } else {
            manageError(answer.error)
          }
        }, { error: Throwable ->
          view?.apply {
            hideEmptyView()
            dismissEmptyViewProgress()
            dismissReloadProgress()
          }

          if (currentEvents == null) {
            if (isFistTime || currentEvents == null || currentEvents!!.isEmpty()) {
              view?.showErrorView(ApiError(error))
              view?.hideEvents()
            } else {
              view?.showErrorDialog(ApiError(error))
            }
          } else {
            view?.showErrorToast(ApiError(error))
          }
        })

    compositeSubscription?.add(subscription)
  }

  fun manageError(apiError: ApiError?) = apiError?.let {
    view?.showErrorDialog(apiError)
  }
}
