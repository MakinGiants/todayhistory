package com.makingiants.today.api.repository.history

import com.makingiants.today.api.Api
import com.makingiants.today.api.repository.history.pojo.Event
import io.reactivex.Observable

open class HistoryRepository() {
  private val mHistoryService = Api.create(HistoryService::class.java)

  open fun get(day: Int, month: Int): Observable<List<Event>> =
      mHistoryService?.get(day, month) ?: Observable.empty()

}
