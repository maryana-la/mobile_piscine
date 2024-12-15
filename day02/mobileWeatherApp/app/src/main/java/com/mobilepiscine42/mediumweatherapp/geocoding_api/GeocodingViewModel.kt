package com.mobilepiscine42.mediumweatherapp.geocoding_api

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobilepiscine42.mediumweatherapp.api.Constant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GeocodingViewModel : ViewModel() {

    private val geocodingApi = RetrofitInstance.geocodingApi

//     fun getData (city : String) : List <Result> {
//         var suggestions : List<Result> = emptyList()
//         viewModelScope.launch {
//            suggestions= apiRequest(city)
//        }
//         return suggestions
//    }

     suspend fun getData(city : String) {
        return withContext(Dispatchers.IO) {
            viewModelScope.launch {
                val response = geocodingApi.getLocationList(city, Constant.NUMBER_OF_SEARCH_RESULT)
                if (response.isSuccessful) {
                    Log.i("Success", "List of cities received")
                    response.body()?.results ?: emptyList()
                } else {
                    Log.i("Error : ", response.message())
                    emptyList()
                }
            }
        }
    }
}