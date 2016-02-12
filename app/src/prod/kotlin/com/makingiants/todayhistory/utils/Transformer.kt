package com.makingiants.todayhistory.utils

import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Transforms to avoid setting each Schedulers on each call.
 * Use:

 * observable.compose(applyIoSchedulers())
 */
object Transformer {
    fun <T> applyIoSchedulers(): Observable.Transformer<T, T> {
        return object : Observable.Transformer<T, T> {
            override fun call(observable: Observable<T>): Observable<T> {
                return observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        }
    }
}