package com.mobilepiscine42.advanced_weather_app.pageviewer

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.mobilepiscine42.advanced_weather_app.R

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
        val mainLayout = view.findViewById<LinearLayout>(R.id.mainLinearLayout)
        val innerLayout = view.findViewById<LinearLayout>(R.id.innerLayout)
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
            mainLayout?.removeView(errorMessage)
            Util.removeErrorMessage(errorMessage)
            val dailyForecast = sharedViewModel.getWeatherForecast().daily
            innerLayout.removeAllViewsInLayout()
            innerLayout?.visibility = View.VISIBLE

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
                val weatherDescription = Util.setTextViewForFragments(Util.setWeatherDescription(dailyForecast.weather_code[i]),
                    requireContext())

                linePerDay.addView(timeTmp)
                linePerDay.addView(tempDayMin)
                linePerDay.addView(tempDayMax)
                linePerDay.addView(weatherDescription)
                innerLayout.addView(linePerDay)
            }
        }

        sharedViewModel.errorLiveData.observe(viewLifecycleOwner) {
            if (sharedViewModel.getErrorMsg().isNotEmpty()) {
                Log.e("FRAGMENT Weekly", "Error message print")
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