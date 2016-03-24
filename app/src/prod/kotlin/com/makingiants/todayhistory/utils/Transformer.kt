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
    fun <T> applyIoSchedulers(): Observable.Transformer<T, T> = Observable.Transformer<T, T> {
        it.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }
}