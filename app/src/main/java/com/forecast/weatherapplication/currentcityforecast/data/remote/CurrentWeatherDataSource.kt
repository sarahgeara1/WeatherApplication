package com.forecast.weatherapplication.currentcityforecast.data.remote

import com.forecast.weatherapplication.common.api.API.Companion.KEY
import com.forecast.weatherapplication.common.api.API.Companion.UNIT_MESURE
import com.forecast.weatherapplication.common.api.RestServices
import com.forecast.weatherapplication.common.di.DaggerAppComponent
import com.forecast.weatherapplication.common.models.CurrentCityWeather
import io.reactivex.Single
import javax.inject.Inject

class CurrentWeatherDataSource {

    @Inject
    lateinit var services: RestServices
    init {
      DaggerAppComponent.create().inject(this)
    }

    fun getWeatherCurrentCity(lat:Double, lon:Double) : Single<CurrentCityWeather>?{
        return services.getWeatherCurrentCity(lat,lon, KEY, UNIT_MESURE)
    }
}