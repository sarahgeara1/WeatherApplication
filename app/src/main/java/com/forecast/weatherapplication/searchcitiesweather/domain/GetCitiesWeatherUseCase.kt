package com.forecast.weatherapplication.searchcitiesweather.domain

import com.forecast.weatherapplication.common.di.DaggerAppComponent
import com.forecast.weatherapplication.common.models.CityWeather
import com.forecast.weatherapplication.searchcitiesweather.data.repository.CitiesWeatherRepository
import io.reactivex.Single
import javax.inject.Inject

class GetCitiesWeatherUseCase {
    @Inject
    lateinit var citiesWeatherRepository: CitiesWeatherRepository

    init {
        DaggerAppComponent.create().inject(this)
    }

    fun getWeatherCity(cityName : String): Single<CityWeather>?{
        return citiesWeatherRepository.getCitiesWeatherCity(cityName)
    }
}