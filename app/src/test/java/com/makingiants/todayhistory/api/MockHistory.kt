package com.makingiants.todayhistory.api

import com.makingiants.today.api.repository.history.pojo.Event
import com.makingiants.today.api.repository.history.pojo.Thumb
import java.util.*

object MockHistory {
  fun events(number: Int): List<Event> {
    val list = ArrayList<Event>(number)
    for (i in 0..number - 1) {
      val title = String.format("Title %d", i)
      val date = String.format("1/1/%d", i)
      val image = Thumb(i, i, "http://image.com/%d")

      list.add(Event(title, date, image))
    }
    return list
  }
}
