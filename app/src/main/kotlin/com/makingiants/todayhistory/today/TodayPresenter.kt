package com.makingiants.todayhistory.today

import android.os.Parcel
import android.os.Parcelable
import android.support.annotation.VisibleForTesting
import com.makingiants.today.api.error_handling.ApiException
import com.makingiants.today.api.repository.history.HistoryRepository
import com.makingiants.today.api.repository.history.pojo.Event
import com.makingiants.todayhistory.utils.DateManager
import com.makingiants.todayhistory.utils.NetworkChecker
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import java.util.*

open class TodayPresenter : Parcelable {
    @VisibleForTesting internal var compositeSubscription: CompositeSubscription? = null
    @VisibleForTesting internal var view: TodayView? = null

    private var mHistoryRepository: HistoryRepository? = null
    private var mNetworkChecker: NetworkChecker? = null
    private var mDateManager: DateManager? = null
    private var mEvents: List<Event>? = null

    constructor(dateManager: DateManager) : super() {
        mDateManager = dateManager
    }

    fun onCreate(view: TodayView, historyRepository: HistoryRepository,
                 networkChecker: NetworkChecker) {
        this.view = view
        mHistoryRepository = historyRepository
        mNetworkChecker = networkChecker
        compositeSubscription = CompositeSubscription()

        if (mEvents == null) {
            loadEvents(true)
        } else {
            view.showEvents(mEvents!!)
        }
    }

    fun onDestroy() {
        view = null

        if (compositeSubscription?.hasSubscriptions() ?: false) {
            compositeSubscription?.unsubscribe()
        }
    }

    fun updateItems() {
        loadEvents(false)
    }

    @VisibleForTesting
    internal fun loadEvents(isFistTime: Boolean) {
        if (!(mNetworkChecker?.isNetworkConnectionAvailable() ?: true)) {
            view?.showErrorToast("There is no internet.")
            return
        }

        if (isFistTime) {
            view?.showEmptyViewProgress()
        } else {
            view?.showReloadProgress()
        }

        val subscription = mHistoryRepository!!.get(mDateManager!!.getTodayDay(), mDateManager!!.getTodayMonth())
                .subscribeOn(Schedulers.immediate())
                .observeOn(Schedulers.immediate())
                .subscribe({ events: List<Event> ->
                    view?.hideErrorView()
                    view?.hideEmptyView()

                    if (isFistTime) {
                        view?.dismissEmptyViewProgress()
                    } else {
                        view?.dismissReloadProgress()
                    }

                    if (events.isEmpty()) {
                        if (mEvents == null) {
                            view?.showEmptyView()
                        } else {
                            // TODO: move the string to strings and show from view
                            view?.showErrorToast("The loaded list is empty, retry latter.")
                        }
                    } else {
                        mEvents = events
                        view?.showEvents(events)
                    }
                }, { error: Throwable ->
                    view?.hideEmptyView()

                    if (isFistTime) {
                        view?.dismissEmptyViewProgress()
                    } else {
                        view?.dismissReloadProgress()
                    }

                    if (mEvents == null) {
                        if (isFistTime || mEvents == null || mEvents!!.isEmpty()) {
                            val apiException = ApiException.from(error)
                            view?.showErrorView(apiException.name, apiException.message)
                            view?.hideEvents()
                        } else {
                            view?.showError(error)
                        }
                    } else {
                        val apiException = ApiException.from(error)
                        view?.showErrorToast(apiException.message)
                    }
                })

        compositeSubscription?.add(subscription)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        //        dest.writeParcelable(this.mDateManager, flags)
        dest.writeList(this.mEvents)
    }

    protected constructor(parcel: Parcel) {
        //        this.mDateManager = parcel.readParcelable<DateManager>(DateManager::class.java!!.getClassLoader())
        this.mEvents = ArrayList<Event>()
        parcel.readList(this.mEvents, List::class.java.getClassLoader())
    }

    companion object {
        val CREATOR: Parcelable.Creator<TodayPresenter> = object : Parcelable.Creator<TodayPresenter> {
            override fun createFromParcel(source: Parcel): TodayPresenter {
                return TodayPresenter(source)
            }

            override fun newArray(size: Int): Array<out TodayPresenter?> =
                    arrayOfNulls<TodayPresenter>(size)
        }
    }
}
