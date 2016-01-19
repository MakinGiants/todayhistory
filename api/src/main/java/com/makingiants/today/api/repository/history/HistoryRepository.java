package com.makingiants.today.api.repository.history;

import com.makingiants.today.api.Api;
import com.makingiants.today.api.repository.history.pojo.Event;
import java.util.List;
import rx.Observable;

public class HistoryRepository {
  private HistoryService mHistoryService;

  public HistoryRepository() {
    mHistoryService = Api.create(HistoryService.class);
  }

  public Observable<List<Event>> get(int day, int month) {
    String formattedMoth = String.format("%02d", ++month);
    return mHistoryService.get(day, formattedMoth);
  }
}
