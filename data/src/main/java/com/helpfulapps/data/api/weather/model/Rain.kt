package com.helpfulapps.data.api.weather.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Rain(
    @SerializedName("3h")
    @Expose
    var _3h: Double = 0.toDouble()
)
