package com.makingiants.today.api.repository.history

import com.makingiants.today.api.Api
import com.makingiants.today.api.repository.history.pojo.Event
import rx.Observable

 interface HistoryRepository {
    operator fun get(day: Int, month: Int): Observable<List<Event>>
}

class HistoryRepositoryImpl : HistoryRepository {
    private val mHistoryService: HistoryService

    init {
        mHistoryService = Api.create(HistoryService::class.java)!!
    }

    override operator fun get(day: Int, month: Int): Observable<List<Event>> {
        var month = month
        val formattedMoth = String.format("%02d", ++month)
        return mHistoryService.get(day, formattedMoth)
    }
}
