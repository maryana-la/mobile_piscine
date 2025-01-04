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

class Weekly : Fragment() {

    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_weekly, container, false)
        val mainLinearLayout = view.findViewById<LinearLayout>(R.id.mainLinearLayout)
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
            val dailyForecast = sharedViewModel.getWeatherForecast().daily

            for (i in 0 until 7) {
                val linePerDay = LinearLayout(requireContext()).apply {
                    orientation = LinearLayout.HORIZONTAL
                }

                val timeTmp = Util.setTextViewForFragments(dailyForecast.time[i], requireContext())
                val tempDayMin = Util.setTextViewForFragments(dailyForecast.temperature_2m_min[i].toString() +
                        sharedViewModel.getWeatherForecast().daily_units.temperature_2m_min,
                    requireContext())
                val tempDayMax = Util.setTextViewForFragments(dailyForecast.temperature_2m_max[i].toString() +
                        sharedViewModel.getWeatherForecast().daily_units.temperature_2m_max,
                    requireContext())
                val weatherDescription = Util.setTextViewForFragments(Util.weatherCode(dailyForecast.weather_code[i]),
                    requireContext())

                linePerDay.addView(timeTmp)
                linePerDay.addView(tempDayMin)
                linePerDay.addView(tempDayMax)
                linePerDay.addView(weatherDescription)
                mainLinearLayout.addView(linePerDay)
            }
        }


        return view
    }




}