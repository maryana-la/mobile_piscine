package com.mobilepiscine42.mediumweatherapp.pageviewer

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModelProvider
import com.mobilepiscine42.mediumweatherapp.R
import com.mobilepiscine42.mediumweatherapp.api.Hourly
import com.mobilepiscine42.mediumweatherapp.reverse_geocoding_api.Address

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
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        sharedViewModel.cityLiveData.observe(viewLifecycleOwner) {
            city?.text = sharedViewModel.getCurrentCity().City
            region?.text = sharedViewModel.getCurrentCity().Region
            country?.text = sharedViewModel.getCurrentCity().CntryName
        }


        sharedViewModel.forecastLiveData.observe(viewLifecycleOwner) {
            val hourlyForecast : Hourly = sharedViewModel.getWeatherForecast().hourly
            innerLayout?.removeAllViewsInLayout()

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
                            Util.getWindDirection(hourlyForecast.wind_direction_10m[i]),
                    requireContext())
                linePerHour.addView(timeStamp)
                linePerHour.addView(temperatureHour)
                linePerHour.addView(windHour)
                innerLayout?.addView(linePerHour)

            }
        }


        sharedViewModel.errorLiveData.observe(viewLifecycleOwner) {
            val errorMessage = TextView(context).apply {
                text = sharedViewModel.getErrorMsg()
                textSize = 40f
                setTextColor(156)
                textAlignment = View.TEXT_ALIGNMENT_CENTER
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    3f)
            }
            mainLayout?.addView(errorMessage)
        }
        return view
    }




}