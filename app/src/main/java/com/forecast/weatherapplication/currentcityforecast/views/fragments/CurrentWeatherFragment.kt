package com.forecast.weatherapplication.currentcityforecast.views.fragments

import android.app.Activity
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.forecast.weatherapplication.R
import com.forecast.weatherapplication.common.models.DayCityWeather
import com.forecast.weatherapplication.currentcityforecast.views.adapters.ViewPagerAdapter
import com.forecast.weatherapplication.currentcityforecast.viewmodel.CurrentWeatherViewModel
import com.forecast.weatherapplication.common.utils.GlobalFunctions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.cities_weather_fragment.pbLoader
import kotlinx.android.synthetic.main.current_city_weather_fragment.*
import java.text.DateFormat
import java.util.*


class CurrentWeatherFragment : Fragment() {

    companion object {
        fun newInstance() =
            CurrentWeatherFragment()
    }

    private lateinit var viewModel: CurrentWeatherViewModel
    private lateinit var adapter: ViewPagerAdapter

    private val REQUEST_CHECK_SETTINGS = 0x1
    private val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 10000
    private val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2
    private val KEY_REQUESTING_LOCATION_UPDATES = "requesting-location-updates"
    private val KEY_LOCATION = "location"
    private val KEY_LAST_UPDATED_TIME_STRING = "last-updated-time-string"
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var mSettingsClient: SettingsClient? = null
    private var mLocationRequest: LocationRequest? = null
    private var mLocationSettingsRequest: LocationSettingsRequest? = null
    private var mLocationCallback : LocationCallback? = null
    private var mCurrentLocation: Location? = null
    private var mRequestingLocationUpdates: Boolean? = null
    private var mLastUpdateTime: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.current_city_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CurrentWeatherViewModel::class.java)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)
        setupView()
        setupLocationVariables()
        observeCurrentCityViewModel()
        startGettingLocation()
    }


    private fun setupView() {
        adapter =
            ViewPagerAdapter(
                activity!!
            )
        viewPager2.adapter = adapter
    }

    fun observeCurrentCityViewModel() {
        viewModel.getAllCityDaysWeather().observe(this,
            Observer<ArrayList<DayCityWeather>> { list ->
                list?.let {
                    adapter.setData(it)
                    TabLayoutMediator(tlIndicator, viewPager2)
                    { tab, position ->}.attach()
                }
            })
        viewModel.isError.observe(this, Observer<Boolean> { isError ->
            isError?.let {
                if (isError) {
                    GlobalFunctions.showToast(activity,activity!!.getString(R.string.error_city_not_found))
                }
            }
        })
        viewModel.loading.observe(this, Observer<Boolean> { isLoading ->
            isLoading?.let { pbLoader.visibility = if (it) View.VISIBLE else View.GONE }
        })
    }


    fun setupLocationVariables(){
        mRequestingLocationUpdates = false;
        mLastUpdateTime = "";
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!);
        mSettingsClient = LocationServices.getSettingsClient(activity!!);

        createLocationCallback();
        createLocationRequest();
        buildLocationSettingsRequest();
    }

    fun startGettingLocation(){
        if (!mRequestingLocationUpdates!!) {
            mRequestingLocationUpdates = true
            startLocationUpdates()
        }
    }

    private fun createLocationRequest() {
        mLocationRequest = LocationRequest()
        mLocationRequest!!.interval = UPDATE_INTERVAL_IN_MILLISECONDS
        mLocationRequest!!.fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
        mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    private fun createLocationCallback() {
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                mCurrentLocation = locationResult.lastLocation
                mLastUpdateTime = DateFormat.getTimeInstance().format(Date())
                mRequestingLocationUpdates = false
                stopLocationUpdates()
                viewModel.getCurrentCityForecast(mCurrentLocation!!.latitude,mCurrentLocation!!.longitude)
            }
        }
    }

    private fun buildLocationSettingsRequest() {
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest!!)
        mLocationSettingsRequest = builder.build()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CHECK_SETTINGS -> when (resultCode) {
                Activity.RESULT_OK -> {
                    startLocationUpdates()
                }
                Activity.RESULT_CANCELED -> {
                    mRequestingLocationUpdates = false
                    Toast.makeText(activity, getString(R.string.error_message_must_turn_on_location), Toast.LENGTH_LONG)
                        .show()
                    activity!!.onBackPressed()
                }
            }
        }
    }

    private fun startLocationUpdates() {
        pbLoader.visibility = View.VISIBLE
        mSettingsClient!!.checkLocationSettings(mLocationSettingsRequest)
            .addOnSuccessListener(activity!!, OnSuccessListener<LocationSettingsResponse?> {
                mFusedLocationClient!!.requestLocationUpdates(
                    mLocationRequest,
                    mLocationCallback, Looper.myLooper()
                )
            })
            .addOnFailureListener(activity!!, OnFailureListener { e ->
                val statusCode = (e as ApiException).statusCode
                when (statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        try {
                            val rae = e as ResolvableApiException
                            rae.startResolutionForResult(
                                activity,
                                REQUEST_CHECK_SETTINGS
                            )
                        } catch (sie: SendIntentException) {
                            sie.printStackTrace()
                        }
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        val errorMessage =
                            getString(R.string.error_message_settings_change_unavailable)
                        Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG)
                            .show()
                        mRequestingLocationUpdates = false
                    }
                }
            })
    }

    private fun stopLocationUpdates() {
        mFusedLocationClient!!.removeLocationUpdates(mLocationCallback)
            .addOnCompleteListener(activity!!, OnCompleteListener<Void?> {
                mRequestingLocationUpdates = false
            })
    }

     override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.putBoolean(
            KEY_REQUESTING_LOCATION_UPDATES,
            mRequestingLocationUpdates!!
        )
        savedInstanceState.putParcelable(KEY_LOCATION, mCurrentLocation)
        savedInstanceState.putString(KEY_LAST_UPDATED_TIME_STRING, mLastUpdateTime)
        super.onSaveInstanceState(savedInstanceState)
    }



}
