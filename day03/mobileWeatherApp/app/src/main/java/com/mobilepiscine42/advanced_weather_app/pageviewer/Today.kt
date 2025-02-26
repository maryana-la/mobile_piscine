package com.mobilepiscine42.advanced_weather_app.pageviewer

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.marginLeft
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
        val forecastRecyclerView = view?.findViewById<RecyclerView>(R.id.forecastRecyclerView)
//        val scrollView = view?.findViewById<ScrollView>(R.id.scrollView)
        val city = view?.findViewById<TextView>(R.id.city)
        val region = view?.findViewById<TextView>(R.id.region)
        val errorMessage = TextView(context)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        sharedViewModel.cityLiveData.observe(viewLifecycleOwner) {
            mainLayout?.removeView(errorMessage)
            Util.removeErrorMessage(errorMessage)
            city?.text = sharedViewModel.getCurrentCity().City
            region?.text = listOfNotNull(sharedViewModel.getCurrentCity().Region,  sharedViewModel.getCurrentCity().CntryName)
                .joinToString(separator = ", ")
        }

//        sharedViewModel.forecastLiveData.observe(viewLifecycleOwner) {
//            val hourlyForecast : Hourly = sharedViewModel.getWeatherForecast().hourly
//            mainLayout?.removeView(errorMessage)
//            Util.removeErrorMessage(errorMessage)
//            innerLayout?.removeAllViewsInLayout()
//            innerLayout?.visibility = View.VISIBLE
//
//
//            for (i in 0 until 24) {
//                val columnPerHour = LinearLayout(requireContext()).apply {
//                    orientation = LinearLayout.VERTICAL
//                    gravity = Gravity.CENTER
//                    layoutParams = LinearLayout.LayoutParams(
//                        LinearLayout.LayoutParams.WRAP_CONTENT,
//                        LinearLayout.LayoutParams.WRAP_CONTENT,
//                    )
//                }
//                columnPerHour.setPadding(10, 0, 10, 0)
//
//                var layoutParamsCommon = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
//
//                // 1
//                val timeStamp = Util.setTextViewForFragments(Util.formatTimeHHMM(hourlyForecast.time[i]), requireContext(), 17f)
//                timeStamp.layoutParams = layoutParamsCommon
//                Log.d("timeStamp", timeStamp.text.toString())
//
//                // 2
//                val weatherIcon = ImageView(context)
//                val layoutParamsWIcon = LinearLayout.LayoutParams(100, 100, 2f)
//                weatherIcon.setLayoutParams(layoutParamsWIcon)
////                weatherIcon.layoutParams.height = 40;
////                weatherIcon.layoutParams.width = 40;
//                weatherIcon.setImageDrawable(context?.let {
//                        it1 -> Util.setWeatherImage(it1, sharedViewModel.getWeatherForecast().current.weather_code) })
//
//                // 3
//                val temperatureHour = Util.setTextViewForFragments(hourlyForecast.temperature_2m[i].toString() +
//                        sharedViewModel.getWeatherForecast().hourly_units.temperature_2m,
//                    requireContext(), 20f)
//                temperatureHour.layoutParams = layoutParamsCommon
//                Log.d("temperatureHour", temperatureHour.text.toString())
//
//                // 4
//
//                val windHour = Util.setTextViewForFragments(hourlyForecast.wind_speed_10m[i].toString() +
//                        sharedViewModel.getWeatherForecast().hourly_units.wind_speed_10m, requireContext(), 13f
//                ).apply {
//                    gravity = Gravity.CENTER_VERTICAL
//                    layoutParams = layoutParamsCommon
////                    layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
//                }
//
//                val windIcon = context?.let { it1 -> Util.resizeVectorDrawable(it1, R.drawable.ic_weather_wind, 40, 40) }
//                windHour.setCompoundDrawablesWithIntrinsicBounds(windIcon, null, null, null)
////                windHour.layoutParams = layoutParamsCommon
//
//                // ----
//                columnPerHour.addView(timeStamp)
//                columnPerHour.addView(weatherIcon)
//                columnPerHour.addView(temperatureHour)
//                columnPerHour.addView(windHour)
//                Log.d("columnPerHour.childCount", columnPerHour.childCount.toString())
//                innerLayout?.addView(columnPerHour)
//                Log.d("innerLayout.childCount", innerLayout?.childCount.toString())
//            }
//        }



        sharedViewModel.forecastLiveData.observe(viewLifecycleOwner) {
            val hourlyForecast : Hourly = sharedViewModel.getWeatherForecast().hourly
            mainLayout?.removeView(errorMessage)
            Util.removeErrorMessage(errorMessage)


//            innerLayout?.removeAllViewsInLayout()
//            innerLayout?.visibility = View.VISIBLE


            for (i in 0 until 24) {
                val columnPerHour = LinearLayout(requireContext()).apply {
                    orientation = LinearLayout.VERTICAL
                    gravity = Gravity.CENTER
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                    )
                }
                columnPerHour.setPadding(10, 0, 10, 0)

                var layoutParamsCommon = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)

                // 1
                val timeStamp = Util.setTextViewForFragments(Util.formatTimeHHMM(hourlyForecast.time[i]), requireContext(), 17f)
                timeStamp.layoutParams = layoutParamsCommon
                Log.d("timeStamp", timeStamp.text.toString())

                // 2
                val weatherIcon = ImageView(context)
                val layoutParamsWIcon = LinearLayout.LayoutParams(100, 100, 2f)
                weatherIcon.setLayoutParams(layoutParamsWIcon)
//                weatherIcon.layoutParams.height = 40;
//                weatherIcon.layoutParams.width = 40;
                weatherIcon.setImageDrawable(context?.let {
                        it1 -> Util.setWeatherImage(it1, hourlyForecast.weather_code[i]) })

                // 3
                val temperatureHour = Util.setTextViewForFragments(hourlyForecast.temperature_2m[i].toString() +
                        sharedViewModel.getWeatherForecast().hourly_units.temperature_2m,
                    requireContext(), 20f)
                temperatureHour.layoutParams = layoutParamsCommon
                Log.d("temperatureHour", temperatureHour.text.toString())

                // 4

                val windHour = Util.setTextViewForFragments(hourlyForecast.wind_speed_10m[i].toString() +
                        sharedViewModel.getWeatherForecast().hourly_units.wind_speed_10m, requireContext(), 13f
                ).apply {
                    gravity = Gravity.CENTER_VERTICAL
                    layoutParams = layoutParamsCommon
//                    layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
                }

                val windIcon = context?.let { it1 -> Util.resizeVectorDrawable(it1, R.drawable.ic_weather_wind, 40, 40) }
                windHour.setCompoundDrawablesWithIntrinsicBounds(windIcon, null, null, null)
//                windHour.layoutParams = layoutParamsCommon

                // ----
                columnPerHour.addView(timeStamp)
                columnPerHour.addView(weatherIcon)
                columnPerHour.addView(temperatureHour)
                columnPerHour.addView(windHour)
                Log.d("columnPerHour.childCount", columnPerHour.childCount.toString())
                innerLayout?.addView(columnPerHour)
                Log.d("innerLayout.childCount", innerLayout?.childCount.toString())
            }
        }


        sharedViewModel.errorLiveData.observe(viewLifecycleOwner) {
            if (sharedViewModel.getErrorMsg().isNotEmpty()) {
                Log.e("FRAGMENT Today", "Error message print")
                city?.text = ""
                region?.text = ""
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