package com.forecast.weatherapplication.currentcityforecast.views.adapters

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.forecast.weatherapplication.R
import com.forecast.weatherapplication.common.models.CityWeather
import com.forecast.weatherapplication.common.utils.GlobalFunctions
import kotlinx.android.synthetic.main.item_city_weather.view.tvWeatherDescription
import kotlinx.android.synthetic.main.item_city_weather.view.tvWeatherMaxTemp
import kotlinx.android.synthetic.main.item_city_weather.view.tvWeatherMinTemp
import kotlinx.android.synthetic.main.item_city_weather.view.tvWeatherTemp
import kotlinx.android.synthetic.main.item_day_city_weather.view.*

class TimeWeatherAdapter (activity: Activity) : RecyclerView.Adapter<TimeWeatherAdapter.CitiesWeatherViewHolder>() {
    private var citiesWeather: ArrayList<CityWeather> = ArrayList()
    private var inflater: LayoutInflater? = null
    var DURATION: Long = 200
    var activity : Activity
    private val on_attach = true

    init {
        this.activity = activity
        inflater = activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitiesWeatherViewHolder {
        val view: View = inflater!!.inflate(R.layout.item_day_city_weather, parent, false)
        return CitiesWeatherViewHolder(
            view
        )
    }

    override fun getItemCount(): Int {
        return citiesWeather.size
    }

    override fun onBindViewHolder(holder: CitiesWeatherViewHolder, position: Int) {
        val holder: CitiesWeatherViewHolder = holder
        val cityWeather: CityWeather = citiesWeather.get(position)

        holder.bind(cityWeather,activity)
        setAnimation(holder.itemView, position);

    }

    fun setCities(news: ArrayList<CityWeather>) {
        this.citiesWeather = news
        notifyDataSetChanged()
    }

    fun updateCities(cityWeather: CityWeather){
        this.citiesWeather.add(cityWeather)
        notifyDataSetChanged()
    }

    class CitiesWeatherViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageWeather = view.imageViewCardWeatherIcon
        val time = view.tvTime
        val windSpeed = view.tvWindSpeed
        val weatherDesciption = view.tvWeatherDescription
        val weatherTemp = view.tvWeatherTemp
        val weatherMaxTemp = view.tvWeatherMaxTemp
        val weatherMinTemp = view.tvWeatherMinTemp
        fun bind(cityWeather: CityWeather,activity: Activity) {
            windSpeed.text = ""+cityWeather.wind!!.speed+"km/h"
            time.text = GlobalFunctions.formatDate(cityWeather.date!!,"yyyy-MM-dd HH:mm:ss","hh:mm aaa")+""
            weatherTemp.text =""+ cityWeather.temp!!.temp.toInt()+ "°"
            weatherMaxTemp.text = ""+cityWeather.temp!!.temp_max.toInt() + "°"
            weatherMinTemp.text = ""+cityWeather.temp!!.temp_min.toInt() + "°"
            weatherDesciption.text = ""+cityWeather.weeklyWeather!!.get(0).description

            Glide
                .with(activity)
                .load("http://openweathermap.org/img/wn/"+cityWeather.weeklyWeather!!.get(0).icon+"@2x.png")
                .centerCrop()
                .placeholder(R.drawable.default_pager_dot)
                .into(imageWeather);
        }
    }

    private fun setAnimation(itemView: View, i: Int) {
        var i = i
        if (!on_attach) {
            i = -1
        }
        val isNotFirstItem = i == -1
        i++
        itemView.alpha = 0f
        val animatorSet = AnimatorSet()
        val animator = ObjectAnimator.ofFloat(itemView, "alpha", 0f, 0.5f, 1.0f)
        ObjectAnimator.ofFloat(itemView, "alpha", 0f).start()
        animator.startDelay = if (isNotFirstItem) DURATION / 2 else i * DURATION / 3
        animator.duration = 500
        animatorSet.play(animator)
        animator.start()
    }
}