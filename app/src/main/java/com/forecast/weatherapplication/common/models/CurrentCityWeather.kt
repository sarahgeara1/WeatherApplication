package com.forecast.weatherapplication.common.models

import com.google.gson.annotations.SerializedName

class CurrentCityWeather {
    @SerializedName("list")
    var cityDaysWeather: ArrayList<CityWeather>? = null
    @SerializedName("city")
    var city: City? = null
}