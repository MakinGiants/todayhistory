package com.makingiants.today.todayhistory.utils;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Transforms to avoid setting each Schedulers on each call.
 * Use:
 *
 * observable.compose(applyIoSchedulers())
 */
public class Transformer {
  public static <T> Observable.Transformer<T, T> applyIoSchedulers() {
    return observable -> observable.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
  }
}
