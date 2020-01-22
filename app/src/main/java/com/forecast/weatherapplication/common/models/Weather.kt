package com.forecast.weatherapplication.common.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Weather : Serializable {
    @SerializedName("id")
    var id = 0
    @SerializedName("main")
    var main = ""
    @SerializedName("description")
    var description =""
    @SerializedName("icon")
    var icon =""
}
