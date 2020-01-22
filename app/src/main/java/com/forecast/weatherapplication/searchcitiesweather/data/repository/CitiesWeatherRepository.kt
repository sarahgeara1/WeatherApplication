package com.forecast.weatherapplication.searchcitiesweather.data.repository

import com.forecast.weatherapplication.common.di.DaggerAppComponent
import com.forecast.weatherapplication.common.models.CityWeather
import com.forecast.weatherapplication.searchcitiesweather.data.remote.CitiesWeatherDataSource
import io.reactivex.Single
import javax.inject.Inject

class CitiesWeatherRepository {
    @Inject
    lateinit var citiesWeatherDataSource: CitiesWeatherDataSource
    init {
        DaggerAppComponent.create().inject(this)
    }
    fun getCitiesWeatherCity(cityName : String): Single<CityWeather>?{
        return citiesWeatherDataSource.getWeatherCity(cityName)
    }
}