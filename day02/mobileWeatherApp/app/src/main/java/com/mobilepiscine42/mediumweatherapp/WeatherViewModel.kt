package com.mobilepiscine42.mediumweatherapp

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mobilepiscine42.mediumweatherapp.api.Constant
import kotlinx.coroutines.launch
import com.mobilepiscine42.mediumweatherapp.api.RetrofitInstance
import com.mobilepiscine42.mediumweatherapp.pageviewer.SharedViewModel
import com.mobilepiscine42.mediumweatherapp.reverse_geocoding_api.ReverseGeoViewModel
import retrofit2.HttpException
import java.io.IOException

class WeatherViewModel : ViewModel() {

    private val weatherApi = RetrofitInstance.weatherApi
    private val _toastMessage = MutableLiveData<String>()
//    val toastMessage: LiveData<String> get() = _toastMessage

    fun getData (latitude : String, longitude : String, sharedViewModel: SharedViewModel, reverseGeoViewModel: ReverseGeoViewModel) {
        viewModelScope.launch {
            try {
                val response = weatherApi.getWeather(
                    latitude,
                    longitude,
                    Constant.CURRENT,
                    Constant.HOURLY,
                    Constant.DAILY,
                    Constant.FORECAST_DAYS
                )
                if (response.isSuccessful) {
                    Log.i("Respones : ", response.body().toString())
                    val forecast = response.body()
                    if (forecast != null) {
                        reverseGeoViewModel.getData(latitude, longitude, sharedViewModel)
                        sharedViewModel.setWeatherForecast(forecast)
                    }
//                    _toastMessage.value =
//                        "New location latitude: $latitude, longitude: $longitude has been set up"
                } else {
                    Log.i("Error1 : ", response.message())
                    sharedViewModel.setErrorMsg("Location is not found")
                }
            } catch ( e : IOException) {
                Log.e("Network", "No internet connection", e)
                sharedViewModel.setErrorMsg("1.No internet connection. Please check your network and try again.")
            } catch (e : HttpException) {
                Log.e ("API error", "HTTP: ${e.message()}", e)
                sharedViewModel.setErrorMsg("2.Error fetching weather data. Please try again later.")
            } catch (e : Exception) {
                Log.e ("WeatherViewModel", "Unexpected error: ${e.message}", e)
                sharedViewModel.setErrorMsg("3.Unexpected error has happened. Please try again later.")
            }
        }
    }
}

//https://api.open-meteo.com/v1/forecast?latitude=52.52&longitude=13.41&current=temperature_2m&hourly=temperature_2m&daily=temperature_2m_max,temperature_2m_min,wind_speed_10m_max&timezone=GMT&forecast_days=3