package com.forecast.weatherapplication.searchcitiesweather.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.forecast.weatherapplication.common.di.DaggerAppComponent
import com.forecast.weatherapplication.searchcitiesweather.domain.GetCitiesWeatherUseCase
import com.forecast.weatherapplication.common.models.CityWeather
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class CitiesWeatherViewModel : ViewModel() {
    @Inject
    lateinit var getCitiesWeatherUseCase : GetCitiesWeatherUseCase
    val loading :MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isError : MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val cities : MutableLiveData<List<CityWeather>>  = MutableLiveData<List<CityWeather>>()
    var listCities : ArrayList<CityWeather>  =  ArrayList<CityWeather>()
    private val disposableCityWeather = CompositeDisposable()


    init {
        DaggerAppComponent.create().inject(this)
    }

    fun getAllCitiesWeather():MutableLiveData<List<CityWeather>>{
        return cities
    }
    fun removeAllCities(){
        listCities =  ArrayList<CityWeather>()
        cities.value = listCities
    }
    fun getCityWeather(cityName :String){
        loading.value=true

        val call = getCitiesWeatherUseCase.getWeatherCity(cityName)
        disposableCityWeather.add(
            call!!.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<CityWeather>() {
                    override fun onSuccess(cityWeather: CityWeather?) {
                        loading.value=false
                        isError.value=false
                        cityWeather!!.city!!.name = cityName
                        listCities.add(cityWeather)
                        cities.value=listCities
                    }

                    override fun onError(e: Throwable?) {
                        loading.value = false
                        isError.value=true
                    }
                }))

    }


}
