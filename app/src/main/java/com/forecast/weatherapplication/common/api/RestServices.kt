package com.forecast.weatherapplication.common.api

import com.forecast.weatherapplication.common.models.CityWeather
import com.forecast.weatherapplication.common.models.CurrentCityWeather
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface RestServices {
    @GET("weather")
    fun getWeatherCity(@Query("q") city: String?, @Query("appid") key: String?,@Query("units")unit: String?): Single<CityWeather>?
    @GET("forecast")
    fun getWeatherCurrentCity(@Query("lat") lat: Double?, @Query("lon")lon: Double?, @Query("appid") key: String?, @Query("units")unit: String?): Single<CurrentCityWeather>?

}