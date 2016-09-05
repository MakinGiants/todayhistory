package com.makingiants.today.api.utils.extensions

import com.makingiants.today.api.Answer
import retrofit2.Response
import rx.Observable
import rx.Observable.just

/**
 * Parse responses to check if there was an error, from it and return Request with all the parsed
 * data.
 *
 * All errors inside api will not call onError on Subscribers, instead will call onNext with an
 * Answer object that will contain the error.
 */
fun <T> Observable<Response<T>>.mapResponseToAnswer(): Observable<Answer<T>> = compose({
  it.flatMap { just(Answer.from(it)) }.onErrorResumeNext { just(Answer.from(it)) }
})

