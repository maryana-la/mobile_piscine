package com.mobilepiscine42.mobileweatherapp

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
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
                _toastMessage.value = "New location $city has been set up"
                response.body()?.location?.let { sharedViewModel.setCurrentLocation(it.name) }
            } else {
                Log.i("Error : ", response.message())
                _toastMessage.value = "Location $city is not found"
            }
        }
    }

}
