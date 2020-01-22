package com.forecast.weatherapplication.searchcitiesweather.views.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.forecast.weatherapplication.R
import com.forecast.weatherapplication.common.models.CityWeather
import com.forecast.weatherapplication.searchcitiesweather.views.adapters.CitiesAdapter
import com.forecast.weatherapplication.searchcitiesweather.viewmodel.CitiesWeatherViewModel
import com.forecast.weatherapplication.common.utils.GlobalFunctions
import kotlinx.android.synthetic.main.cities_weather_fragment.*


class CitiesWeatherFragment : Fragment() {

    companion object {
        fun newInstance() =
            CitiesWeatherFragment()
    }

    private lateinit var viewModel: CitiesWeatherViewModel
    private var cityToAdd = ""
    private lateinit var adapter: CitiesAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.cities_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CitiesWeatherViewModel::class.java)

        setupView()

        setupRecyclerView()

        observeCitiesViewModel()
    }

    fun setupView() {
        etCityName.setImeOptions(EditorInfo.IME_ACTION_DONE);
        etCityName.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) { // Your action on done
                cityToAdd = etCityName.text.toString()
                checkCities(cityToAdd)
                true
            } else false
        })
        bSearch.setOnClickListener(View.OnClickListener {
            cityToAdd = etCityName.text.toString()
            checkCities(cityToAdd)
        })
    }

    fun checkCities(citiesNames: String?) {
        citiesNames?.let {
            if (citiesNames.contains(",")) {
                var cities = citiesNames.trim().toLowerCase().split(",")
                var citiesTrimed = ArrayList<String>()
                cities.forEach{
                    citiesTrimed.add(it.trim())
                }
                var citiesWithoutDuplication = citiesTrimed.distinct()
                if (citiesWithoutDuplication.size > 2 && citiesWithoutDuplication.size < 8) {
                    viewModel.removeAllCities()
                    citiesWithoutDuplication.forEach { cityName -> viewModel.getCityWeather(cityName.trim()) }
                } else {
                    if (citiesWithoutDuplication.size < 3) {
                        GlobalFunctions.showToast(
                            activity,
                            activity!!.getString(R.string.error_minimum_3_cities)
                        )
                    } else {
                        GlobalFunctions.showToast(
                            activity,
                            activity!!.getString(R.string.error_maximum_8_cities)
                        )
                    }
                }
            } else {
                GlobalFunctions.showToast(
                    activity,
                    activity!!.getString(R.string.error_minimum_3_cities)
                )
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = CitiesAdapter(activity!!)
        adapter.setCities(ArrayList<CityWeather>())
        rvCities.layoutManager = LinearLayoutManager(activity)
        rvCities.adapter = adapter
    }

    fun observeCitiesViewModel() {
        viewModel.getAllCitiesWeather().observe(this,
            Observer<List<CityWeather>> { list ->
                list?.let {
                    adapter.setCities(it as ArrayList<CityWeather>)
                }
            })
        viewModel.isError.observe(this, Observer<Boolean> { isError ->
            isError?.let {
                if (isError) {
                    GlobalFunctions.showToast(
                        activity,
                        activity!!.getString(R.string.error_city_not_found)
                    )
                }
            }
        })
        viewModel.loading.observe(this, Observer<Boolean> { isLoading ->
            isLoading?.let { pbLoader.visibility = if (it) View.VISIBLE else View.GONE }
        })
    }

}
