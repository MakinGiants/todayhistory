package com.makingiants.todayhistory.utils

import rx.Observable
import rx.schedulers.Schedulers.immediate

/**
 * Transforms to avoid setting each Schedulers on each call with immediate Scheduler. (for tests)
 * Use:

 * observable.compose(applyIoSchedulers())
 */
object Transformer {
    fun <T> applyIoSchedulers(): Observable.Transformer<T, T> {
        return Observable.Transformer<T, T> { it.subscribeOn(immediate()).observeOn(immediate()) }
    }
}