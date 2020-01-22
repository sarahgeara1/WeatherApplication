package com.forecast.weatherapplication.common.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class CityWeather : Serializable {
    var city :City? = City()
    @SerializedName("weather")
    var weeklyWeather: ArrayList<Weather>? = null
    @SerializedName("wind")
    var wind: Wind? = Wind()
    @SerializedName("main")
    var temp: Temp? = Temp()
    @SerializedName("dt_txt")
    var date : String?=""

}