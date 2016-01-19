package com.makingiants.todayhistory.api;

import com.makingiants.today.api.repository.history.pojo.Event;
import java.util.ArrayList;
import java.util.List;

public class MockHistory {
  public static List<Event> events(int number) {
    List<Event> list = new ArrayList<>(number);
    for (int i = 0; i < number; i++) {
      String title = String.format("Title %d", i);
      String date = String.format("1/1/%d", i);
      String imageUrl = String.format("http://image.com/%d", i);

      list.add(new Event(title, date, imageUrl));
    }
    return list;
  }
}
