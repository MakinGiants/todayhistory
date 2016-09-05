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

open class TodayPresenter(var dateManager: DateManager) {
  @VisibleForTesting var mCompositeSubscription: CompositeSubscription? = null
  @VisibleForTesting var mView: TodayView? = null
  private var mHistoryRepository: HistoryRepository? = null
  private var mNetworkChecker: NetworkChecker? = null
  private var mEvents: List<Event>? = null

  fun onCreate(view: TodayView, historyRepository: HistoryRepository,
               networkChecker: NetworkChecker) {
    mView = view
    mHistoryRepository = historyRepository
    mNetworkChecker = networkChecker
    mCompositeSubscription = CompositeSubscription()

    view.initViews()

    if (mEvents == null) {
      loadEvents(true)
    } else {
      mView?.showEvents(mEvents!!)
    }
  }

  fun onDestroy() {
    mView = null
    mHistoryRepository = null
    mNetworkChecker = null

    if (mCompositeSubscription?.hasSubscriptions() ?: false) {
      mCompositeSubscription?.unsubscribe()
    }
  }

  fun onRefresh() = loadEvents()

  @VisibleForTesting
  fun loadEvents(isFistTime: Boolean = false) {
    if (!(mNetworkChecker?.isNetworkConnectionAvailable() ?: true)) {
      mView?.showErrorToast(NoInternetApiError())
      return
    }

    if (isFistTime) {
      mView?.showEmptyViewProgress()
    } else {
      mView?.showReloadProgress()
    }

    val subscription = mHistoryRepository?.get(dateManager.getTodayDay(), dateManager.getTodayMonth())
        ?.composeForIoTasks()
        ?.subscribe({ answer: Answer<List<Event>> ->

          mView?.apply {
            hideErrorView()
            hideEmptyView()
            dismissEmptyViewProgress()
            dismissReloadProgress()
          }

          val events = answer.body
          if (answer.error == null && events != null) {
            if (events.isEmpty()) {
              if (mEvents == null) {
                mView?.showEmptyView()
              } else {
                mView?.showErrorEmptyItemsToast()
              }
            } else {
              mEvents = events
              mView?.showEvents(events)
            }
          } else {
            manageError(answer.error)
          }
        }, { error: Throwable ->
          mView?.apply {
            hideEmptyView()
            dismissEmptyViewProgress()
            dismissReloadProgress()
          }

          if (mEvents == null) {
            if (isFistTime || mEvents == null || mEvents!!.isEmpty()) {
              mView?.showErrorView(ApiError(error))
              mView?.hideEvents()
            } else {
              mView?.showErrorDialog(ApiError(error))
            }
          } else {
            mView?.showErrorToast(ApiError(error))
          }
        })

    mCompositeSubscription?.add(subscription)
  }

  fun manageError(apiError: ApiError?) = apiError?.let {
    mView?.showErrorDialog(apiError)
  }

}
