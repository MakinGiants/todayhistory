package com.makingiants.today.api.repository.history

import com.makingiants.today.api.repository.history.pojo.Event
import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable

internal interface HistoryService {
  @GET("main.json")
  operator fun get(@Query("d") day: String, @Query("m") month: String): Observable<List<Event>>
}
