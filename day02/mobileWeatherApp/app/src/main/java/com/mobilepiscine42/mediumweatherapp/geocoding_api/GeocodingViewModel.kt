package com.mobilepiscine42.mediumweatherapp.geocoding_api

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobilepiscine42.mediumweatherapp.api.Constant
import kotlinx.coroutines.launch

class GeocodingViewModel : ViewModel() {

    private val geocodingApi = RetrofitInstance.geocodingApi

    fun getData (city : String) : List<Result> {
        var suggestions : List<Result>
        suggestions = emptyList()
        viewModelScope.launch {
            val response = geocodingApi.getLocationList(city, Constant.NUMBER_OF_SEARCH_RESULT)
            if (response.isSuccessful) {
                Log.i("Success","List of cities received")
                suggestions = response.body()!!.results
            } else {
                Log.i("Error : ", response.message())
            }
        }
        return suggestions
    }


}