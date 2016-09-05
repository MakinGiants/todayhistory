package com.makingiants.todayhistory.utils.extensions

import com.makingiants.todayhistory.utils.RetryWithDelayIfTimeout
import rx.Observable
import rx.android.schedulers.AndroidSchedulers.mainThread
import rx.schedulers.Schedulers.io

/**
 * Shorthand to set [subscribeOn] and [observeOn] thread for observables,
 * show/hide progress dialogs when needed and retry requests on timeouts.
 */
fun <T> Observable<T>.composeForIoTasks(): Observable<T> = compose<T>(Observable.Transformer {
  it.subscribeOn(io()).observeOn(mainThread()).retryWhen(RetryWithDelayIfTimeout(3, 500))
})