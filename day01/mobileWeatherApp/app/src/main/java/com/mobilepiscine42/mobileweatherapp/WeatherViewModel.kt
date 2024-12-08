package com.mobilepiscine42.mobileweatherapp

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mobilepiscine42.mobileweatherapp.api.Constant
import kotlinx.coroutines.launch
import com.mobilepiscine42.mobileweatherapp.api.RetrofitInstance
import com.mobilepiscine42.mobileweatherapp.pageviewer.SharedViewModel

class WeatherViewModel : ViewModel() {

    private val weatherApi = RetrofitInstance.weatherApi
    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> get() = _toastMessage

    fun getData (city : String, sharedViewModel: SharedViewModel) {
        viewModelScope.launch {
            val response = weatherApi.getWeather(Constant.apiKey, city)
            if (response.isSuccessful) {
                Log.i ("Respones : ", response.body().toString())
                val currentCity = response.body()?.location?.name
                _toastMessage.value = "New location $currentCity has been set up"
                sharedViewModel.setCurrentLocation(currentCity.toString())
            } else {
                Log.i("Error : ", response.message())
                _toastMessage.value = "Location $city is not found"
            }
        }
    }
}
