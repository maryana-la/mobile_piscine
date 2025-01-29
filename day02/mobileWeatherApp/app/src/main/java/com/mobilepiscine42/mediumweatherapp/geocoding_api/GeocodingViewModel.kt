package com.mobilepiscine42.mediumweatherapp.geocoding_api

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobilepiscine42.mediumweatherapp.api.Constant
import com.mobilepiscine42.mediumweatherapp.pageviewer.SharedViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GeocodingViewModel : ViewModel() {

    private val geocodingApi = RetrofitInstance.geocodingApi

    //TODO check if internet connection is available
    fun getData(city : String, sharedViewModel: SharedViewModel) {
        viewModelScope.launch {
            val response = geocodingApi.getLocationList(city, Constant.NUMBER_OF_SEARCH_RESULT)
            if (response.isSuccessful) {
                Log.i("Success", "List of cities received")
                sharedViewModel.setCityOptions(response.body()?.results ?: emptyList())
            } else {
                Log.i("Error : ", response.message())
                sharedViewModel.setCityOptions(emptyList())
            }
        }
    }
}