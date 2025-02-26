package com.mobilepiscine42.advanced_weather_app.pageviewer

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobilepiscine42.advanced_weather_app.R
import com.mobilepiscine42.advanced_weather_app.api.Hourly
import com.mobilepiscine42.advanced_weather_app.api.HourlyUnits
import androidx.fragment.app.Fragment

class ForecastAdapter(
    private var hourlyForecast: Hourly,
    val hourlyUnits: HourlyUnits
) : RecyclerView.Adapter<ForecastAdapter.HourlyForecastViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastAdapter.HourlyForecastViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_hourly_forecast, parent, false)
        return HourlyForecastViewHolder(view)
    }


    override fun onBindViewHolder(holder: ForecastAdapter.HourlyForecastViewHolder, position: Int) {
        val timeStamp = hourlyForecast.time[position]
        val weatherCode = hourlyForecast.weather_code[position]
        val temperature = hourlyForecast.temperature_2m[position]
        val wind = hourlyForecast.wind_speed_10m[position]
        holder.bind(timeStamp, weatherCode, temperature, wind)
    }


    override fun getItemCount(): Int {
        return 24 //TODO replace with constant
    }



    class HourlyForecastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //TODO add hourly units?
        private val timeStamp: TextView = itemView.findViewById(R.id.timeStamp)
        private val weatherIcon: ImageView = itemView.findViewById(R.id.weatherIcon)
        private val temperatureHour: TextView = itemView.findViewById(R.id.temperatureHour)
        private val windHour: TextView = itemView.findViewById(R.id.windHour)

        fun bind(time: String, weatherCode: Int, temperature: Double, wind: Double) {
            timeStamp.text = Util.formatTimeHHMM(time)
            Log.d("timeStamp", timeStamp.text.toString())
//            weatherIcon.setImageDrawable(Util.setWeatherImage(context, weatherCode)
            temperatureHour.text = temperature.toString()
            windHour.text = wind.toString()
//            cityName.text = city.name
//            cityRegion.text = city.admin1
//            cityCountry.text = city.country
        }
    }
}



