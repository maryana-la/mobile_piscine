package com.mobilepiscine42.mediumweatherapp.geocoding_api

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobilepiscine42.mediumweatherapp.api.Constant
import com.mobilepiscine42.mediumweatherapp.pageviewer.SharedViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class GeocodingViewModel : ViewModel() {

    private val geocodingApi = RetrofitInstance.geocodingApi


    fun getData(city : String, sharedViewModel: SharedViewModel) {
        viewModelScope.launch {
            try {
                val response = geocodingApi.getLocationList(city, Constant.NUMBER_OF_SEARCH_RESULT)
                if (response.body()?.results != null) {
                    Log.i("Success", response.body().toString())
                    sharedViewModel.setCityOptions(response.body()?.results ?: emptyList())
                } else {
                    Log.i("Error : ", response.message())
                    sharedViewModel.setCityOptions(emptyList())
                    sharedViewModel.setErrorMsg("10. Could not find any result for the provided address or coordinates")
                }
            } catch (e : IOException) {
                Log.e("Network", "No internet connection", e)
                sharedViewModel.setErrorMsg("4.No internet connection. Please check your network and try again.")
            } catch (e : HttpException) {
                Log.e ("API error", "HTTP: ${e.message()}", e)
                sharedViewModel.setErrorMsg("5.Error fetching weather data. Please try again later.")
            } catch (e : Exception) {
                Log.e ("WeatherViewModel", "Unexpected error: ${e.message}", e)
                sharedViewModel.setErrorMsg("6.Unexpected error has happened. Please try again later.")
            }
        }
    }
}