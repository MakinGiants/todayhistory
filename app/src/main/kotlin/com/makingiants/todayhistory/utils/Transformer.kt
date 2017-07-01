package com.makingiants.todayhistory.utils

import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


/**
 * Transforms to avoid setting each Schedulers on each call.
 * Use:

 * observable.compose(applyIoSchedulers())
 */
object Transformer {

  fun <T> applyIoSchedulers() = ObservableTransformer<T, T> {
    it.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
  }

}