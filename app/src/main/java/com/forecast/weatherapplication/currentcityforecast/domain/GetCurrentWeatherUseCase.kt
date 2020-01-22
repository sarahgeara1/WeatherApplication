package com.forecast.weatherapplication.currentcityforecast.domain

import com.forecast.weatherapplication.currentcityforecast.data.repository.CurrentWeatherRepository
import com.forecast.weatherapplication.common.di.DaggerAppComponent
import com.forecast.weatherapplication.common.models.CurrentCityWeather
import io.reactivex.Single
import javax.inject.Inject

class GetCurrentWeatherUseCase {

    @Inject
    lateinit var currentWeatherRepository: CurrentWeatherRepository

    init {
        DaggerAppComponent.create().inject(this)
    }

    fun getWeatherCurrentCity(lat : Double, lon:Double): Single<CurrentCityWeather>?{
        return currentWeatherRepository.getWeatherCurrentCity(lat,lon)
    }
}