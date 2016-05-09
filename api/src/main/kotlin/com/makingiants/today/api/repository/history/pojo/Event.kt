package com.makingiants.today.api.repository.history.pojo

import com.google.gson.annotations.SerializedName

class Event(val title: String,
            val date: String,
            val thumb: Thumb)

class Thumb(@SerializedName("w") val weight: Int,
            @SerializedName("h") val height: Int,
            val src: String)