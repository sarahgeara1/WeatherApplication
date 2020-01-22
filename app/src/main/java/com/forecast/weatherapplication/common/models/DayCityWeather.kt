package com.forecast.weatherapplication.common.models

import java.io.Serializable

class DayCityWeather : Serializable {

    var date :String? = ""
    var daysWeather : ArrayList<CityWeather> = ArrayList<CityWeather>()
}