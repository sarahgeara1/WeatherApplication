package com.forecast.weatherapplication

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.forecast.weatherapplication.searchcitiesweather.domain.GetCitiesWeatherUseCase
import com.forecast.weatherapplication.common.models.*
import com.forecast.weatherapplication.searchcitiesweather.viewmodel.CitiesWeatherViewModel
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.android.plugins.RxAndroidPlugins
import org.junit.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

class CitiesWeatherViewModelTest {
    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var citiesWeatherUseCase : GetCitiesWeatherUseCase

    @InjectMocks
    var citiesWeatherViewModel = CitiesWeatherViewModel()


    private var testSingle: Single<CityWeather>? = null

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Before
    fun setupJavaRx() {
        val immediate = object : Scheduler() {
            override fun scheduleDirect(run: Runnable?, delay: Long, unit: TimeUnit?): Disposable {
                return super.scheduleDirect(run, 0, unit)
            }

            override fun createWorker(): Worker {
                return ExecutorScheduler.ExecutorWorker(Executor { it.run() })
            }
        }

        RxJavaPlugins.setInitIoSchedulerHandler { scheduler -> immediate }
        RxJavaPlugins.setInitComputationSchedulerHandler { scheduler -> immediate }
        RxJavaPlugins.setInitNewThreadSchedulerHandler { scheduler -> immediate }
        RxJavaPlugins.setSingleSchedulerHandler { scheduler -> immediate }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler -> immediate }

    }

    @Test
    fun getCitiesWeatherSuccess() {

        val temp = Temp()
        temp.pressure=2.5f
        temp.temp=9.10f
        temp.temp_max = 9.15f
        temp.temp_min = 9.05f
        val wind = Wind()
        wind.speed = 35.0f
        wind.speed = 35.0f
        val weather = Weather()
        weather.description="light rain"
        weather.icon="icon"
        weather.id=1
        weather.main="main"

        val weatherList = arrayListOf(weather)

        val cityWeather = CityWeather()
        cityWeather.city = City("Chicago")
        cityWeather.date = "2020-10-05 15:00:00"
        cityWeather.weeklyWeather = weatherList
        cityWeather.wind= wind
        cityWeather.temp = temp

        testSingle = Single.just(cityWeather)
        Mockito.`when`(citiesWeatherUseCase.getWeatherCity("Chicago")).thenReturn(testSingle)

        citiesWeatherViewModel.getCityWeather("Chicago")

        Assert.assertEquals(1, citiesWeatherViewModel.cities.value?.size)
        Assert.assertEquals(false, citiesWeatherViewModel.loading.value)
        Assert.assertEquals(false, citiesWeatherViewModel.isError.value)
    }


    @Test
    fun getCitiesWeatherFailure() {
        testSingle = Single.error(Throwable())
        Mockito.`when`(citiesWeatherUseCase.getWeatherCity("Paris")).thenReturn(testSingle)

        citiesWeatherViewModel.getCityWeather("Paris")

        Assert.assertEquals(null, citiesWeatherViewModel.cities.value?.size)
        Assert.assertEquals(false, citiesWeatherViewModel.loading.value)
        Assert.assertEquals(true, citiesWeatherViewModel.isError.value)
    }


}