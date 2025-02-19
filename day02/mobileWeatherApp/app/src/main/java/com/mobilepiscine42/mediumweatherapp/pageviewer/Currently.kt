package com.mobilepiscine42.mediumweatherapp.pageviewer

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mobilepiscine42.mediumweatherapp.R

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
        val mainLayout = view?.findViewById<LinearLayout>(R.id.mainLayout)
        val city = view?.findViewById<TextView>(R.id.city)
        val region = view?.findViewById<TextView>(R.id.region)
        val country = view?.findViewById<TextView>(R.id.country)
        val temperature = view?.findViewById<TextView>(R.id.temperature)
        val wind = view?.findViewById<TextView>(R.id.wind)
        val errorMessage = TextView(context)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        sharedViewModel.cityLiveData.observe(viewLifecycleOwner) {
            mainLayout?.removeView(errorMessage)
            Util.removeErrorMessage(errorMessage)
            city?.text = sharedViewModel.getCurrentCity().City
            region?.text = sharedViewModel.getCurrentCity().Region
            country?.text = sharedViewModel.getCurrentCity().CntryName
        }

        sharedViewModel.forecastLiveData.observe(viewLifecycleOwner) {
            mainLayout?.removeView(errorMessage)
            Util.removeErrorMessage(errorMessage)
            temperature?.text = sharedViewModel.getWeatherForecast().current.temperature_2m.toString() +
                sharedViewModel.getWeatherForecast().current_units.temperature_2m
            wind?.text = sharedViewModel.getWeatherForecast().current.wind_speed_10m.toString() +
                    sharedViewModel.getWeatherForecast().current_units.wind_speed_10m + " " +
                    Util.getWindDirection(sharedViewModel.getWeatherForecast().current.wind_direction_10m)

        }

        sharedViewModel.errorLiveData.observe(viewLifecycleOwner) {
            if (sharedViewModel.getErrorMsg().isNotEmpty()) {
                Log.e("FRAGMENT Currently", "Error message print")
                city?.text = ""
                region?.text = ""
                country?.text = ""
                temperature?.text = ""
                wind?.text = ""
                mainLayout?.removeView(errorMessage)
                if (mainLayout != null) {
                    errorMessage.text = sharedViewModel.getErrorMsg()
                    Util.setupErrorMessage(errorMessage, mainLayout)
                }
            }
        }
        return view
    }
}