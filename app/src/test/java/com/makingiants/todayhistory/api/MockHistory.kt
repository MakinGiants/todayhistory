package com.makingiants.todayhistory.api

import com.makingiants.today.api.repository.history.pojo.Event
import java.util.ArrayList

object MockHistory {
    fun events(number: Int): List<Event> {
        val list = ArrayList<Event>(number)
        for (i in 0..number - 1) {
            val title = String.format("Title %d", i)
            val date = String.format("1/1/%d", i)
            val imageUrl = String.format("http://image.com/%d", i)

            list.add(Event(title, date, imageUrl))
        }
        return list
    }
}
