package com.forecast.weatherapplication.landing.views.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.forecast.weatherapplication.R


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //send result to current fragment for gps enabled permission
        val navHostFragment: NavHostFragment? = supportFragmentManager!!.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
        navHostFragment!!.childFragmentManager.fragments[0].onActivityResult(requestCode, resultCode, data)
    }
}
