package com.mobilepiscine42.mediumweatherapp.reverse_geocoding_api

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobilepiscine42.mediumweatherapp.api.Constant
import com.mobilepiscine42.mediumweatherapp.pageviewer.SharedViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class ReverseGeoViewModel : ViewModel() {

    private val reverseGeoApi = RetrofitInstance.reverseGeoApi

    fun getData (latitude : String, longitude : String, sharedViewModel : SharedViewModel) {
        viewModelScope.launch {
            try {
                val response = reverseGeoApi.getLocationName(
                    "$longitude,$latitude",
                    Constant.LANG,
                    Constant.FORSTORAGE,
                    Constant.FORMAT
                )
                if (response.isSuccessful) {
                    Log.i("Reverse geo response : ", response.body().toString())
                    val reverseGeoDetails = response.body()
                    if (reverseGeoDetails != null) {
                        sharedViewModel.setCurrentCity(reverseGeoDetails.address)
                    }
                }
            } catch (e : IOException) {
                Log.e("Network", "No internet connection", e)
                sharedViewModel.setErrorMsg("7.No internet connection. Please check your network and try again.")
            } catch (e : HttpException) {
                Log.e ("API error", "HTTP: ${e.message()}", e)
                sharedViewModel.setErrorMsg("8.Error fetching weather data. Please try again later.")
            } catch (e : Exception) {
                Log.e ("WeatherViewModel", "Unexpected error: ${e.message}", e)
                sharedViewModel.setErrorMsg("9.Unexpected error has happened. Please try again later.")
            }
        }
    }

}



