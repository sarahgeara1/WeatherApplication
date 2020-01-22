package com.forecast.weatherapplication.common.di

import com.forecast.weatherapplication.common.api.API
import com.forecast.weatherapplication.common.api.RestServices
import com.forecast.weatherapplication.currentcityforecast.data.remote.CurrentWeatherDataSource
import com.forecast.weatherapplication.currentcityforecast.data.repository.CurrentWeatherRepository
import com.forecast.weatherapplication.currentcityforecast.domain.GetCurrentWeatherUseCase
import com.forecast.weatherapplication.searchcitiesweather.data.remote.CitiesWeatherDataSource
import com.forecast.weatherapplication.searchcitiesweather.data.repository.CitiesWeatherRepository
import com.forecast.weatherapplication.searchcitiesweather.domain.GetCitiesWeatherUseCase
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
class WeatherModule {

    @Provides
    fun provideWeatherService(): RestServices {
        return Retrofit.Builder()
            .baseUrl(API.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(RestServices::class.java)
    }

    @Provides
    fun provideCitiesWeatherDataSource():CitiesWeatherDataSource{
        return CitiesWeatherDataSource()
    }

    @Provides
    fun provideCitiesWeatherRepository():CitiesWeatherRepository{
        return CitiesWeatherRepository()
    }

    @Provides
    fun provideCurrentWeatherDataSource():CurrentWeatherDataSource{
        return CurrentWeatherDataSource()
    }

    @Provides
    fun provideCurrentWeatherRepository():CurrentWeatherRepository{
        return CurrentWeatherRepository()
    }
    @Provides
    fun provideGetWeatherCityUseCase():GetCitiesWeatherUseCase{
        return GetCitiesWeatherUseCase()
    }
    @Provides
    fun provideGetCurrentWeatherUseCase(): GetCurrentWeatherUseCase {
        return GetCurrentWeatherUseCase()
    }
}