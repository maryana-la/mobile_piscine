package com.mobilepiscine42.advanced_weather_app.api

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobilepiscine42.advanced_weather_app.pageviewer.SharedViewModel
import com.mobilepiscine42.advanced_weather_app.reverse_geocoding_api.ReverseGeoViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class WeatherViewModel : ViewModel() {

    private val weatherApi = RetrofitInstance.weatherApi

    fun getData (latitude : String, longitude : String, sharedViewModel: SharedViewModel, reverseGeoViewModel: ReverseGeoViewModel) {
        viewModelScope.launch {
            try {

                val weatherDeferred = async { weatherApi.getWeather(
                    latitude, longitude, Constant.CURRENT, Constant.HOURLY, Constant.DAILY, Constant.FORECAST_DAYS, Constant.TIMEZONE
                ) }
                val reverseGeoDeferred = async { reverseGeoViewModel.getData(latitude, longitude, sharedViewModel) }

                val response = weatherDeferred.await()

                if (response.isSuccessful) {
                    Log.i("Response:", response.body().toString())
                    sharedViewModel.setWeatherForecast(response.body()!!)
                    reverseGeoDeferred.await() // Ensure reverse geocoding completes
                } else {
                    Log.i("Error:", response.message())
                    sharedViewModel.setErrorMsg("Location is not found")
                }
            } catch ( e : IOException) {
                Log.e("Network", "No internet connection", e)
                sharedViewModel.setErrorMsg("No internet connection.\nPlease check your network and try again.")
            } catch (e : HttpException) {
                Log.e ("API error", "HTTP: ${e.message()}", e)
                sharedViewModel.setErrorMsg("Error fetching weather data.\nPlease try again later.")
            } catch (e : Exception) {
                Log.e ("WeatherViewModel", "Unexpected error: ${e.message}", e)
                sharedViewModel.setErrorMsg("Unexpected error has happened.\nPlease try again later.")
            }
        }
    }
}
