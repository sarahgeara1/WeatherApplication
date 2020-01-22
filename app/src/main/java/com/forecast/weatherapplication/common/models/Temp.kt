package com.forecast.weatherapplication.common.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Temp : Serializable {
    @SerializedName("pressure")
    var pressure = 0f
    @SerializedName("temp")
    var temp  = 0f
    @SerializedName("temp_max")
    var temp_max  = 0f
    @SerializedName("temp_min")
    var temp_min  = 0f
}
