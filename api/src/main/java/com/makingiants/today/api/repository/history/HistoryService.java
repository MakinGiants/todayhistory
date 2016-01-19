package com.makingiants.today.api.repository.history;

import com.makingiants.today.api.repository.history.pojo.Event;
import java.util.List;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

interface HistoryService {
  @GET("/main.json")
  Observable<List<Event>> get(@Query("day") int day, @Query("month") String month);
}
