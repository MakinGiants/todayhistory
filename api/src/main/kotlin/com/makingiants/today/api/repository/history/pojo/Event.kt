package com.makingiants.today.api.repository.history.pojo

import com.google.gson.annotations.SerializedName

class Event(val title: String, val date: String, @SerializedName("url") val imageUrl: String)