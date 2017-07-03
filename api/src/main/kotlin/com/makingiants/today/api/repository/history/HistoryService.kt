package com.makingiants.today.api.repository.history

import com.makingiants.today.api.repository.history.pojo.Event
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

internal interface HistoryService {
  @GET("main.json")
  operator fun get(@Query("d") day: Int, @Query("m") month: Int): Observable<List<Event>>
}
