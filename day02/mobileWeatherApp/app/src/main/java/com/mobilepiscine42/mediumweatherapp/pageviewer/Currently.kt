package com.mobilepiscine42.mediumweatherapp.pageviewer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mobilepiscine42.mediumweatherapp.R
import java.math.BigDecimal

class Currently : Fragment() {
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_currently, container, false)
        val city = view?.findViewById<TextView>(R.id.city)
        val region = view?.findViewById<TextView>(R.id.region)
        val country = view?.findViewById<TextView>(R.id.country)
        val temperature = view?.findViewById<TextView>(R.id.temperature)
        val wind = view?.findViewById<TextView>(R.id.wind)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        sharedViewModel.cityLiveData.observe(viewLifecycleOwner) {
            city?.text = sharedViewModel.getCurrentCity().name
            region?.text = sharedViewModel.getCurrentCity().admin1
            country?.text = sharedViewModel.getCurrentCity().country
        }

        sharedViewModel.forecastLiveData.observe(viewLifecycleOwner) {
            temperature?.text = sharedViewModel.getWeatherForecast().current.temperature_2m.toString() +
                sharedViewModel.getWeatherForecast().current_units.temperature_2m
            wind?.text = sharedViewModel.getWeatherForecast().current.wind_speed_10m.toString() +
                    sharedViewModel.getWeatherForecast().current_units.wind_speed_10m + " " +
                    Util.getWindDirection(sharedViewModel.getWeatherForecast().current.wind_direction_10m)

        }
        return view
    }
}