package com.mobilepiscine42.advanced_weather_app.pageviewer

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.mobilepiscine42.advanced_weather_app.R
import com.mobilepiscine42.advanced_weather_app.api.Hourly

class Today : Fragment() {

    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_today, container, false)
        val innerLayout = view?.findViewById<LinearLayout>(R.id.innerLayout)
        val mainLayout = view?.findViewById<LinearLayout>(R.id.mainLayout)
        val scrollView = view?.findViewById<ScrollView>(R.id.scrollView)
        val city = view?.findViewById<TextView>(R.id.city)
        val region = view?.findViewById<TextView>(R.id.region)
        val country = view?.findViewById<TextView>(R.id.country)
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
            val hourlyForecast : Hourly = sharedViewModel.getWeatherForecast().hourly
            mainLayout?.removeView(errorMessage)
            Util.removeErrorMessage(errorMessage)
            innerLayout?.removeAllViewsInLayout()
            innerLayout?.visibility = View.VISIBLE


            for (i in 0 until 24) {
                val linePerHour = LinearLayout(requireContext()).apply {
                    orientation = LinearLayout.HORIZONTAL
                }

                val timeStamp = Util.setTextViewForFragments(Util.formatTimeHHMM(hourlyForecast.time[i]), requireContext())
                val temperatureHour = Util.setTextViewForFragments(hourlyForecast.temperature_2m[i].toString() +
                        sharedViewModel.getWeatherForecast().hourly_units.temperature_2m,
                    requireContext())
                val windHour = Util.setTextViewForFragments(hourlyForecast.wind_speed_10m[i].toString() +
                            sharedViewModel.getWeatherForecast().hourly_units.wind_speed_10m + " " +
                            Util.getWindDirection(hourlyForecast.wind_direction_10m[i]), requireContext()
                    ).apply { layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 2f) }
                linePerHour.addView(timeStamp)
                linePerHour.addView(temperatureHour)
                linePerHour.addView(windHour)
                innerLayout?.addView(linePerHour)
            }
        }

        sharedViewModel.errorLiveData.observe(viewLifecycleOwner) {
            if (sharedViewModel.getErrorMsg().isNotEmpty()) {
                Log.e("FRAGMENT Today", "Error message print")
                city?.text = ""
                region?.text = ""
                country?.text = ""
                mainLayout?.removeView(errorMessage)
                innerLayout?.removeAllViewsInLayout()
                innerLayout?.visibility = View.GONE
                if (mainLayout != null) {
                    errorMessage.text = sharedViewModel.getErrorMsg()
                    Util.setupErrorMessage(errorMessage, mainLayout)
                }
            }
        }
        return view
    }




}