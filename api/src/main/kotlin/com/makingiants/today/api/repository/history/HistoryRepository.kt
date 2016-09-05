package com.makingiants.today.api.repository.history

import com.makingiants.today.api.Answer
import com.makingiants.today.api.Api
import com.makingiants.today.api.repository.history.pojo.Event
import com.makingiants.today.api.utils.extensions.mapResponseToAnswer
import rx.Observable

open class HistoryRepository() {
  private val mHistoryService = Api.create(HistoryService::class.java)

  open fun get(day: Int, month: Int): Observable<Answer<List<Event>>> =
      mHistoryService?.get(day, month)?.mapResponseToAnswer() ?: Observable.empty()

}
