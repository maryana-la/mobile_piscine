package com.mobilepiscine42.advanced_weather_app.pageviewer

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.LineChart
import com.mobilepiscine42.advanced_weather_app.R
import com.mobilepiscine42.advanced_weather_app.api.Hourly


class Today : Fragment() {

    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_today, container, false)
        val mainLayout = view?.findViewById<LinearLayout>(R.id.mainLayout)
        val lineChart = view?.findViewById<LineChart>(R.id.lineChart)
        val city = view?.findViewById<TextView>(R.id.city)
        val region = view?.findViewById<TextView>(R.id.region)
        val errorMessage = TextView(context)
        val forecastRecyclerView = view?.findViewById<RecyclerView>(R.id.forecastRecyclerView)!!

        forecastRecyclerView.setOnTouchListener { v, event ->
            v.parent.requestDisallowInterceptTouchEvent(true) // Stop parent from intercepting touch
            false
        }

        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        sharedViewModel.cityLiveData.observe(viewLifecycleOwner) {
            mainLayout?.removeView(errorMessage)
            Util.removeErrorMessage(errorMessage)
            city?.text = sharedViewModel.getCurrentCity().City
            region?.text = listOfNotNull(sharedViewModel.getCurrentCity().Region,  sharedViewModel.getCurrentCity().CntryName)
                .joinToString(separator = ", ")
        }

        sharedViewModel.forecastLiveData.observe(viewLifecycleOwner) {
            val hourlyForecast : Hourly = sharedViewModel.getWeatherForecast().hourly
            mainLayout?.removeView(errorMessage)
            Util.removeErrorMessage(errorMessage)

            forecastRecyclerView.visibility = View.VISIBLE
            forecastRecyclerView.layoutManager = LinearLayoutManager(parentFragment?.context, LinearLayoutManager.HORIZONTAL, false)
            val adapter = context?.let { it1 -> ForecastAdapter(it1, hourlyForecast, sharedViewModel.getWeatherForecast().hourly_units) }!!
            forecastRecyclerView.adapter = adapter

            if (lineChart != null) {
                Util.createChart(sharedViewModel, lineChart)
            }
        }

        sharedViewModel.errorLiveData.observe(viewLifecycleOwner) {
            if (sharedViewModel.getErrorMsg().isNotEmpty()) {
                Log.e("FRAGMENT Today", "Error message print")
                city?.text = ""
                region?.text = ""
                forecastRecyclerView.visibility = View.GONE
                lineChart?.visibility = View.GONE
                if (mainLayout != null) {
                    errorMessage.text = sharedViewModel.getErrorMsg()
                    Util.setupErrorMessage(errorMessage, mainLayout)
                }
            }
        }
        return view
    }
}