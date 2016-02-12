package com.makingiants.todayhistory.utils

import rx.Observable
import rx.schedulers.Schedulers
import rx.android.schedulers.AndroidSchedulers

/**
 * Transforms to avoid setting each Schedulers on each call with immediate Scheduler. (for tests)
 * Use:

 * observable.compose(applyIoSchedulers())
 */
object Transformer {
    fun <T> applyIoSchedulers(): Observable.Transformer<T, T> {
        return object : Observable.Transformer<T, T> {
            override fun call(observable: Observable<T>): Observable<T> {
                return observable.subscribeOn(Schedulers.immediate())
                        .observeOn(Schedulers.immediate());
            }
        }
    }
}