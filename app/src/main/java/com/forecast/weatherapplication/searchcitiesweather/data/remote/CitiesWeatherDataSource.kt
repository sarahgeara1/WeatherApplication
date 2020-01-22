package com.forecast.weatherapplication.searchcitiesweather.data.remote

import com.forecast.weatherapplication.common.api.API.Companion.KEY
import com.forecast.weatherapplication.common.api.API.Companion.UNIT_MESURE
import com.forecast.weatherapplication.common.api.RestServices
import com.forecast.weatherapplication.common.di.DaggerAppComponent
import com.forecast.weatherapplication.common.models.CityWeather
import io.reactivex.Single
import javax.inject.Inject

class CitiesWeatherDataSource {

    @Inject
    lateinit var services: RestServices
    init {
      DaggerAppComponent.create().inject(this)
    }

    fun getWeatherCity(cityName : String): Single<CityWeather>?{
       return services.getWeatherCity(cityName,KEY,UNIT_MESURE)
    }

}