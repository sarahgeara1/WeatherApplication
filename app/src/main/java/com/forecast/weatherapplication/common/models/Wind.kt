package com.forecast.weatherapplication.common.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Wind : Serializable {
    @SerializedName("speed")
    var speed = 0f
    @SerializedName("deg")
    var deg  = 0f
}