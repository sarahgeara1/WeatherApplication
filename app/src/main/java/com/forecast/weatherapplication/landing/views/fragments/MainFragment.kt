package com.forecast.weatherapplication.landing.views.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.forecast.weatherapplication.BuildConfig
import com.forecast.weatherapplication.R
import com.forecast.weatherapplication.common.utils.GlobalFunctions
import kotlinx.android.synthetic.main.fragment_main.*


class MainFragment : Fragment() {

    val LOCATION_PERMISSION_ID = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        bCitiesWeather.setOnClickListener(View.OnClickListener {
            view?.let { it1 -> navigateCity(it1) }
        })

        bCurrentWeather.setOnClickListener(View.OnClickListener {
            view?.let { it1 -> navigateCurrentCity(it1) }
        })

    }

    private fun navigateCity(view: View) {
        Navigation.findNavController(view).navigate(
            R.id.action_main_fragment_to_cities_fragment
        )
    }
    private fun navigateCurrentCity(view: View) {
        if(checkPermissions()) {
            Navigation.findNavController(view).navigate(
                R.id.action_main_fragment_to_current_city_fragment
            )
        }else{
            requestPermissions()
        }
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return true
        }
        return false
    }

    private fun requestPermissions() {
        var shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(activity!!, Manifest.permission.ACCESS_FINE_LOCATION);
        if (shouldProvideRationale) {
            GlobalFunctions.showDialog(activity!!,"Permission was denied, but is needed for core functionality.","Ok","cancel", Runnable {
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_ID
                )
            }, Runnable {
                activity!!.onBackPressed()
            })

        } else {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_ID
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==LOCATION_PERMISSION_ID){
            if (grantResults.size > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                bCurrentWeather.performClick()
            }else{
                GlobalFunctions.showDialog(activity!!,getString(R.string.error_message_permission_was_denied),"Ok","cancel", Runnable {
                    val intent = Intent()
                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    val uri: Uri = Uri.fromParts(
                        "package",
                        BuildConfig.APPLICATION_ID, null
                    )
                    intent.data = uri
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }, Runnable {
                  Toast.makeText(activity!!,getString(R.string.error_message_application_needs_location_permission),Toast.LENGTH_LONG)
                })

            }
        }
    }
}
