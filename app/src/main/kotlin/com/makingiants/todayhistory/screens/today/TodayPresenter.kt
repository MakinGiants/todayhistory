package com.makingiants.todayhistory.screens.today

import android.support.annotation.VisibleForTesting
import com.makingiants.today.api.error_handling.ApiException
import com.makingiants.today.api.repository.history.HistoryRepository
import com.makingiants.today.api.repository.history.pojo.Event
import com.makingiants.todayhistory.utils.DateManager
import com.makingiants.todayhistory.utils.NetworkChecker
import com.makingiants.todayhistory.utils.Transformer
import rx.subscriptions.CompositeSubscription

open class TodayPresenter(var dateManager: DateManager,
                          val historyRepository: HistoryRepository,
                          val networkChecker: NetworkChecker) {
  @VisibleForTesting var compositeSubscription: CompositeSubscription? = null
  @VisibleForTesting var view: TodayView? = null
  private var events: List<Event>? = null

  fun attach(view: TodayView) {
    this.view = view
    compositeSubscription = CompositeSubscription()

    view.initViews()
    if (events == null) {
      loadEvents(true)
    } else {
      view.showEvents(events!!)
    }
  }

  fun unAttach() {
    view = null

    if (compositeSubscription?.hasSubscriptions() ?: false) {
      compositeSubscription?.clear()
    }
  }

  fun onRefresh() = loadEvents()

  @VisibleForTesting
  fun loadEvents(isFistTime: Boolean = false) {
    if (!(networkChecker.isNetworkConnectionAvailable())) {
      view?.showErrorToast("There is no internet.")
      return
    }

    if (isFistTime) {
      view?.showEmptyViewProgress()
    } else {
      view?.showReloadProgress()
    }

    val subscription = historyRepository.get(dateManager.getTodayDay(), dateManager.getTodayMonth())
        .compose(Transformer.applyIoSchedulers<List<Event>>())
        .subscribe({ events: List<Event> ->
          view?.hideErrorView()
          view?.hideEmptyView()
          view?.dismissEmptyViewProgress()
          view?.dismissReloadProgress()

          if (events.isEmpty()) {
            if (this.events == null) {
              view?.showEmptyView()
            } else {
              // TODO: move the string to strings and show from view
              view?.showErrorToast("The loaded list is empty, retry latter.")
            }
          } else {
            this.events = events
            view?.showEvents(events)
          }
        }, { error: Throwable ->
          view?.hideEmptyView()
          view?.dismissEmptyViewProgress()
          view?.dismissReloadProgress()

          if (events == null) {
            if (isFistTime || events == null || events!!.isEmpty()) {
              val apiException = ApiException.from(error)
              view?.showErrorView(apiException.name, apiException.text)
              view?.hideEvents()
            } else {
              view?.showErrorDialog(error)
            }
          } else {
            val apiException = ApiException.from(error)
            view?.showErrorToast(apiException.text)
          }
        })

    compositeSubscription?.add(subscription)
  }
}
