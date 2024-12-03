package com.mobilepiscine42.mobileweatherapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.mobilepiscine42.mobileweatherapp.api.RetrofitInstance

class WeatherViewModel : ViewModel() {

    private val weatherApi = RetrofitInstance.weatherApi

    fun getData (city : String) {
        viewModelScope.launch {
        val response = weatherApi.getWeather(Constant.apiKey, city)
    }
}