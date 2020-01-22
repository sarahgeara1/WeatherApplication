package com.forecast.weatherapplication

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.forecast.weatherapplication.currentcityforecast.domain.GetCurrentWeatherUseCase
import com.forecast.weatherapplication.common.models.*
import com.forecast.weatherapplication.currentcityforecast.viewmodel.CurrentWeatherViewModel
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

class CurrentWeatherViewModelTest {
    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var currentWeatherUseCase : GetCurrentWeatherUseCase

    @InjectMocks
    var currentWeatherViewModel =
        CurrentWeatherViewModel()


    private var testSingle: Single<CurrentCityWeather>? = null

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
    fun getCurrentCityWeatherSuccess() {

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
        cityWeather.city = City()
        cityWeather.city!!.name = "Chicago"
        cityWeather.date = "2020-10-05 15:00:00"
        cityWeather.weeklyWeather = weatherList
        cityWeather.wind= wind
        cityWeather.temp = temp


        val currentCityForecast = CurrentCityWeather()
        currentCityForecast.city = City()
        currentCityForecast.city?.name = "Chicago"
        currentCityForecast.cityDaysWeather= arrayListOf(cityWeather)

        testSingle = Single.just(currentCityForecast)
        Mockito.`when`(currentWeatherUseCase.getWeatherCurrentCity(30.2525,35.26589)).thenReturn(testSingle)
        currentWeatherViewModel.getCurrentCityForecast(30.2525,35.26589)


        Assert.assertEquals(temp.temp_min, currentWeatherViewModel.cityDaysWeather.value?.get(0)?.temp?.temp_min)
        Assert.assertEquals(temp.temp_max, currentWeatherViewModel.cityDaysWeather.value?.get(0)?.temp?.temp_max)
        Assert.assertEquals(wind.speed, currentWeatherViewModel.cityDaysWeather.value?.get(0)?.wind?.speed)
        Assert.assertEquals(1, currentWeatherViewModel.cityDaysWeather.value?.size)
        Assert.assertEquals(false, currentWeatherViewModel.isError.value)
    }


    @Test
    fun getCurrentCityWeatherFailure() {
        testSingle = Single.error(Throwable())
        Mockito.`when`(currentWeatherUseCase.getWeatherCurrentCity(20.255,25.265)).thenReturn(testSingle)

        currentWeatherUseCase.getWeatherCurrentCity(20.255,25.265)

        Assert.assertEquals(0, currentWeatherViewModel.weatherByDayArrayList?.size)
    }


}