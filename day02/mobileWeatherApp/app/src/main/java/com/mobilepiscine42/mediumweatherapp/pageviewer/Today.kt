package com.mobilepiscine42.mediumweatherapp.pageviewer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.mobilepiscine42.mediumweatherapp.R
import com.mobilepiscine42.mediumweatherapp.api.Hourly

class Today : Fragment() {

    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    //TODO add scrolling
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_today, container, false)
        val mainLayout = view?.findViewById<LinearLayout>(R.id.mainLayout)
        val city = view?.findViewById<TextView>(R.id.city)
        val region = view?.findViewById<TextView>(R.id.region)
        val country = view?.findViewById<TextView>(R.id.country)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        sharedViewModel.cityLiveData.observe(viewLifecycleOwner) {
            city?.text = sharedViewModel.getCurrentCity().name
            region?.text = sharedViewModel.getCurrentCity().admin1
            country?.text = sharedViewModel.getCurrentCity().country
        }


        sharedViewModel.forecastLiveData.observe(viewLifecycleOwner) {
            val hourlyForecast : Hourly = sharedViewModel.getWeatherForecast().hourly

            for (i in 0 until 23) {
                val linePerHour = LinearLayout(requireContext()).apply {
                    orientation = LinearLayout.HORIZONTAL
                }

                val timeStamp = Util.setTextViewForFragments(Util.formatTimeHHMM(hourlyForecast.time[i]), requireContext())
                val temperatureHour = Util.setTextViewForFragments(hourlyForecast.temperature_2m[i].toString() +
                        sharedViewModel.getWeatherForecast().hourly_units.temperature_2m,
                    requireContext())
                val windHour = Util.setTextViewForFragments(hourlyForecast.wind_speed_10m[i].toString() +
                            sharedViewModel.getWeatherForecast().hourly_units.wind_speed_10m + " " +
                            Util.getWindDirection(hourlyForecast.wind_direction_10m[i]),
                    requireContext())
                linePerHour.addView(timeStamp)
                linePerHour.addView(temperatureHour)
                linePerHour.addView(windHour)
                mainLayout?.addView(linePerHour)
            }
        }

        return view
    }




}