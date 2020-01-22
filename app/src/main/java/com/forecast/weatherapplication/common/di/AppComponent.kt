package com.forecast.weatherapplication.common.di

import com.forecast.weatherapplication.currentcityforecast.data.remote.CurrentWeatherDataSource
import com.forecast.weatherapplication.currentcityforecast.data.repository.CurrentWeatherRepository
import com.forecast.weatherapplication.currentcityforecast.domain.GetCurrentWeatherUseCase
import com.forecast.weatherapplication.searchcitiesweather.domain.GetCitiesWeatherUseCase
import com.forecast.weatherapplication.searchcitiesweather.viewmodel.CitiesWeatherViewModel
import com.forecast.weatherapplication.currentcityforecast.viewmodel.CurrentWeatherViewModel
import com.forecast.weatherapplication.searchcitiesweather.data.remote.CitiesWeatherDataSource
import com.forecast.weatherapplication.searchcitiesweather.data.repository.CitiesWeatherRepository
import dagger.Component

@Component(modules = [WeatherModule::class])
interface AppComponent {

    fun inject(citiesWeatherDataSource: CitiesWeatherDataSource){}
    fun inject(citiesWeatherRepository: CitiesWeatherRepository){}
    fun inject(currentWeatherDataSource: CurrentWeatherDataSource){}
    fun inject(currentWeatherRepository: CurrentWeatherRepository){}
    fun inject(getCitiesWeatherUseCase: GetCitiesWeatherUseCase){}
    fun inject(getCurrentWeatherUseCase: GetCurrentWeatherUseCase){}
    fun inject(viewModel: CitiesWeatherViewModel){}
    fun inject(viewModel: CurrentWeatherViewModel){}
}