package com.mobilepiscine42.mediumweatherapp.pageviewer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.mobilepiscine42.mediumweatherapp.geocoding_api.Result

class SharedViewModel : ViewModel() {

    private val currentLocation = MutableLiveData<String>()
    private var cityOptions : MutableList<Result> = mutableListOf()
    val location: LiveData<String> get() = currentLocation


    fun setCurrentLocation(city: String) {
        currentLocation.value = city.uppercase()
    }

    fun getCurrentLocation() : String? {
        return currentLocation.value
    }

    fun setCityOptions(fromAPI : List<Result>) {
        cityOptions.clear()
        cityOptions.addAll(fromAPI)
    }

    fun getCityOptions() : List<Result> {
        return cityOptions.toList()
    }

}