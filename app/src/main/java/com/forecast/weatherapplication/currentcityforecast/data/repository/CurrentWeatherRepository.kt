package com.forecast.weatherapplication.currentcityforecast.data.repository

import com.forecast.weatherapplication.currentcityforecast.data.remote.CurrentWeatherDataSource
import com.forecast.weatherapplication.common.di.DaggerAppComponent
import com.forecast.weatherapplication.common.models.CurrentCityWeather
import io.reactivex.Single
import javax.inject.Inject

class CurrentWeatherRepository {
    @Inject
    lateinit var currentWeatherDataSource: CurrentWeatherDataSource
    init {
        DaggerAppComponent.create().inject(this)
    }
    fun getWeatherCurrentCity(lat:Double, lon:Double): Single<CurrentCityWeather>?{
        return currentWeatherDataSource.getWeatherCurrentCity(lat,lon)
    }
}