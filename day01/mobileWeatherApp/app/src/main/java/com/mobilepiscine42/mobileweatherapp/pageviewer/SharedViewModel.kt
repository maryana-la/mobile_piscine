package com.mobilepiscine42.mobileweatherapp.pageviewer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {

    private val currentLocation = MutableLiveData<String>()
    val location: LiveData<String> get() = currentLocation

    fun setCurrentLocation(city: String) {
        currentLocation.value = city.uppercase()
    }

    fun getCurrentLocation() : String? {
        return currentLocation.value
    }

}