package com.mobilepiscine42.mediumweatherapp

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mobilepiscine42.mediumweatherapp.api.Constant
import kotlinx.coroutines.launch
import com.mobilepiscine42.mediumweatherapp.api.RetrofitInstance
import com.mobilepiscine42.mediumweatherapp.pageviewer.SharedViewModel

class WeatherViewModel : ViewModel() {

    private val weatherApi = RetrofitInstance.weatherApi
    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> get() = _toastMessage

    fun getData (latitude : String, longitude : String, sharedViewModel: SharedViewModel) {
        viewModelScope.launch {
            val response = weatherApi.getWeather("48.85341", "2.3488", Constant.CURRENT, Constant.HOURLY, Constant.DAILY, Constant.FORECAST_DAYS)
            if (response.isSuccessful) {
                Log.i ("Respones : ", response.body().toString())
//                val currentCity = response.body()?.location?.name
//                _toastMessage.value = "New location $currentCity has been set up"
//                sharedViewModel.setCurrentLocation(currentCity.toString())
            } else {
                Log.i("Error : ", response.message())
                _toastMessage.value = "Location is not found"
            }
        }
    }
}

//https://api.open-meteo.com/v1/forecast?latitude=52.52&longitude=13.41&current=temperature_2m&hourly=temperature_2m&daily=temperature_2m_max,temperature_2m_min,wind_speed_10m_max&timezone=GMT&forecast_days=3