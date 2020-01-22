package com.forecast.weatherapplication.currentcityforecast.views.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.forecast.weatherapplication.R
import com.forecast.weatherapplication.common.models.DayCityWeather
import com.forecast.weatherapplication.common.utils.GlobalFunctions
import kotlinx.android.synthetic.main.item_city_weather.view.tvCityName
import kotlinx.android.synthetic.main.item_day_city.view.*
import kotlinx.android.synthetic.main.item_view_pager.view.*


class ViewPagerAdapter internal constructor(activity: Activity?) : RecyclerView.Adapter<ViewPagerAdapter.WeatherViewHolder>() {
    private var mData: List<DayCityWeather> = ArrayList()
    private val mInflater: LayoutInflater
    val activity: Activity
    init {
        mInflater = LayoutInflater.from(activity)
        this.activity = activity!!
    }

    fun setData(data:ArrayList<DayCityWeather>){
        mData = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val view: View = mInflater.inflate(R.layout.item_view_pager, parent, false)
        return WeatherViewHolder(
            view,
            activity
        )
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val holder: WeatherViewHolder = holder
        val cityWeather: DayCityWeather = mData.get(position)

        holder.bind(cityWeather)
    }

    override fun getItemCount(): Int {
        return mData.size
    }


    class WeatherViewHolder(view: View,activity: Activity?) : RecyclerView.ViewHolder(view) {
        val cityName = view.tvCityName
        val cityDate = view.tvDate
        val rvWeathers = view.rvWeathers
        var timeWeatherAdapter : TimeWeatherAdapter =
            TimeWeatherAdapter(
                activity!!
            )

        fun bind(cityWeather: DayCityWeather) {
            cityName.text = cityWeather.daysWeather!!.get(0).city!!.name
            cityDate.text =""+ (GlobalFunctions.formatDate(cityWeather.date,"yyyy-MM-dd","EEE, dd MMM yyyy"))
            timeWeatherAdapter.setCities(cityWeather.daysWeather)
            rvWeathers.adapter = timeWeatherAdapter
        }
    }

}
