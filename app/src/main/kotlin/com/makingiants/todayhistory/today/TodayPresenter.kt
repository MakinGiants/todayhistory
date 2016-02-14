package com.makingiants.todayhistory.today

import android.os.Parcel
import android.os.Parcelable
import android.support.annotation.VisibleForTesting
import com.makingiants.today.api.error_handling.ApiException
import com.makingiants.today.api.repository.history.HistoryRepository
import com.makingiants.today.api.repository.history.pojo.Event
import com.makingiants.todayhistory.utils.DateManager
import com.makingiants.todayhistory.utils.NetworkChecker
import com.makingiants.todayhistory.utils.Transformer
import rx.subscriptions.CompositeSubscription
import java.util.*

open class TodayPresenter(var dateManager: DateManager) : Parcelable {
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

        if (mEvents == null) {
            loadEvents(true)
        } else {
            mView?.showEvents(mEvents!!)
        }
    }

    fun onDestroy() {
        mView = null

        if (mCompositeSubscription?.hasSubscriptions() ?: false) {
            mCompositeSubscription?.unsubscribe()
        }
    }

    fun updateItems() {
        loadEvents()
    }

    @VisibleForTesting
    fun loadEvents(isFistTime: Boolean = false) {
        if (!(mNetworkChecker?.isNetworkConnectionAvailable() ?: true)) {
            mView?.showErrorToast("There is no internet.")
            return
        }

        if (isFistTime) {
            mView?.showEmptyViewProgress()
        } else {
            mView?.showReloadProgress()
        }

        val subscription = mHistoryRepository!!.get(dateManager.getTodayDay(), dateManager.getTodayMonth())
                .compose(Transformer.applyIoSchedulers<List<Event>>())
                .subscribe({ events: List<Event> ->
                    mView?.hideErrorView()
                    mView?.hideEmptyView()
                    mView?.dismissEmptyViewProgress()
                    mView?.dismissReloadProgress()

                    if (events.isEmpty()) {
                        if (mEvents == null) {
                            mView?.showEmptyView()
                        } else {
                            // TODO: move the string to strings and show from view
                            mView?.showErrorToast("The loaded list is empty, retry latter.")
                        }
                    } else {
                        mEvents = events
                        mView?.showEvents(events)
                    }
                }, { error: Throwable ->
                    mView?.hideEmptyView()
                    mView?.dismissEmptyViewProgress()
                    mView?.dismissReloadProgress()

                    if (mEvents == null) {
                        if (isFistTime || mEvents == null || mEvents!!.isEmpty()) {
                            val apiException = ApiException.from(error)
                            mView?.showErrorView(apiException.name, apiException.text)
                            mView?.hideEvents()
                        } else {
                            mView?.showErrorDialog(error)
                        }
                    } else {
                        val apiException = ApiException.from(error)
                        mView?.showErrorToast(apiException.text)
                    }
                })

        mCompositeSubscription?.add(subscription)
    }

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelable(this.dateManager, flags)

        if (mEvents != null) {
            dest.writeList(this.mEvents)
        }
    }

    constructor(parcel: Parcel) :
    this(parcel.readParcelable<DateManager>(DateManager::class.java.classLoader)) {
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
