package com.makingiants.today.api.repository.history.pojo

import com.google.gson.annotations.SerializedName

class Event(var title: String,
            var date: String,
            var thumb: Thumb)

class Thumb(@SerializedName("w") var weight: Int,
            @SerializedName("h") var height: Int,
            val src: String)