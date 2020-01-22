package com.forecast.weatherapplication.currentcityforecast.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.forecast.weatherapplication.common.di.DaggerAppComponent
import com.forecast.weatherapplication.currentcityforecast.domain.GetCurrentWeatherUseCase
import com.forecast.weatherapplication.common.models.CityWeather
import com.forecast.weatherapplication.common.models.CurrentCityWeather
import com.forecast.weatherapplication.common.models.DayCityWeather
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CurrentWeatherViewModel : ViewModel() {
    @Inject
    lateinit var getCurrentWeatherUseCase: GetCurrentWeatherUseCase
    var currentCityName : String? =""
    var loading :MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    var isError : MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    var cityDaysWeather : MutableLiveData<List<CityWeather>> = MutableLiveData<List<CityWeather>>()
    var cityWeathersArrayList : ArrayList<CityWeather>?= ArrayList<CityWeather>()
    var weatherByDayArrayList : ArrayList<DayCityWeather>?= ArrayList<DayCityWeather>()
    var weatherByDayArrayListMutable : MutableLiveData<ArrayList<DayCityWeather>> = MutableLiveData<ArrayList<DayCityWeather>>()
    private val disposableCurrentCityWeather = CompositeDisposable()

    init {
        DaggerAppComponent.create().inject(this)
    }

    fun getAllCityDaysWeather():MutableLiveData<ArrayList<DayCityWeather>>{
        return weatherByDayArrayListMutable
    }

    fun getCurrentCityForecast(lat :Double,lon:Double){
        loading.value=true
        val call = getCurrentWeatherUseCase.getWeatherCurrentCity(lat,lon)

        disposableCurrentCityWeather.add(
            call!!.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<CurrentCityWeather>() {
                    override fun onSuccess(currentCityWeather: CurrentCityWeather?) {
                        loading.value=false
                        isError.value=false
                        currentCityName = currentCityWeather!!.city!!.name
                        cityWeathersArrayList = currentCityWeather!!.cityDaysWeather
                        cityDaysWeather.value=cityWeathersArrayList
                        groupByDate()
                    }

                    override fun onError(e: Throwable?) {
                        loading.value = false
                        isError.value=true
                    }
                }))

    }

    fun groupByDate(){
        var days = ArrayList<String>()
        for (cityWeather in cityWeathersArrayList!!) {
            var day=  cityWeather.date!!.split(" ")
            if(!days.contains(day[0]))
            days.add(day[0])

        }
        for(day in days){
            var item = DayCityWeather()
            item.date = day

            for(cityWeather in cityWeathersArrayList!!){

                if(cityWeather.date!!.contains(day)){
                    cityWeather.city!!.name = currentCityName
                    item.daysWeather.add(cityWeather)
                }
            }
            weatherByDayArrayList!!.add(item)
        }
        weatherByDayArrayListMutable.value = weatherByDayArrayList
    }

}
