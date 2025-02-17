package com.mobilepiscine42.mediumweatherapp.pageviewer

import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.mobilepiscine42.mediumweatherapp.R
import com.mobilepiscine42.mediumweatherapp.api.Hourly

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
            city?.text = sharedViewModel.getCurrentCity().City
            region?.text = sharedViewModel.getCurrentCity().Region
            country?.text = sharedViewModel.getCurrentCity().CntryName
        }

        sharedViewModel.forecastLiveData.observe(viewLifecycleOwner) {
            val hourlyForecast : Hourly = sharedViewModel.getWeatherForecast().hourly
            mainLayout?.removeView(errorMessage)
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
                Log.e("Error message", "inside")
                city?.text = ""
                region?.text = ""
                country?.text = ""
                mainLayout?.removeView(errorMessage)

                innerLayout?.removeAllViewsInLayout()
                innerLayout?.visibility = View.GONE

                Log.d("mainLayout Debug", "Visibility: ${mainLayout?.visibility}")
                errorMessage.apply {
                    text = sharedViewModel.getErrorMsg()
                    setTextColor(Color.RED)

                    textAlignment = View.TEXT_ALIGNMENT_CENTER
                    textSize = 20f
                    maxLines = 3
//                    ellipsize = TextUtils.TruncateAt.END
                    setLineSpacing(4f, 1.2f)
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                }

                mainLayout?.post {
                    if (errorMessage.parent == null) {
                        Log.i("mainLayout?.post", errorMessage.text.toString())
                        mainLayout.addView(errorMessage)
                    } else {
                        Log.i("Error msg parent", errorMessage.parent.toString())
                    }
                }
                Log.d("mainLayout Debug", "Visibility: ${mainLayout?.visibility}")
//                mainLayout?.visibility = View.VISIBLE
                Log.d("error message", "Visibility: ${errorMessage.visibility}")
//                errorMessage.visibility = View.VISIBLE
                sharedViewModel.setErrorMsg("")

//
//                errorMessage.bringToFront()
//                errorMessage.requestLayout()
//                errorMessage.invalidate()
//
//
//                mainLayout?.requestLayout()
            }
        }
        return view
    }




}