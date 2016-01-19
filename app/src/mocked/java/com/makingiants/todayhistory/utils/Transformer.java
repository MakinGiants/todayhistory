package com.makingiants.todayhistory.utils;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Transforms to avoid setting each Schedulers on each call with inmediate Scheduler. (for tests)
 * Use:
 *
 * observable.compose(applyIoSchedulers())
 */
public class Transformer {
  public static <T> Observable.Transformer<T, T> applyIoSchedulers() {
    return observable -> observable.subscribeOn(Schedulers.immediate())
        .observeOn(Schedulers.immediate());
  }
}
