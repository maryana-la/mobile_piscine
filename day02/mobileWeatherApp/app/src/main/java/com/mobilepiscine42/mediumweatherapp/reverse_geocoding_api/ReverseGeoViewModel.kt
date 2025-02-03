package com.mobilepiscine42.mediumweatherapp.reverse_geocoding_api

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobilepiscine42.mediumweatherapp.api.Constant
import com.mobilepiscine42.mediumweatherapp.pageviewer.SharedViewModel
import kotlinx.coroutines.launch

class ReverseGeoViewModel : ViewModel() {

    private val reverseGeoApi = RetrofitInstance.reverseGeoApi

    fun getData (latitude : String, longitude : String, shareViewModel : SharedViewModel) {
        viewModelScope.launch {
            val response = reverseGeoApi.getLocationName("$longitude,$latitude", Constant.LANG, Constant.FORSTORAGE, Constant.FORMAT)
            if (response.isSuccessful) {
                Log.i ("Reverse geo response : ", response.body().toString())
                val reverseGeoDetails = response.body()
                if (reverseGeoDetails != null) {
                    shareViewModel.setCurrentCity(reverseGeoDetails.address) //TODO change the recepient for shareviewmodel
                }
            }
        }
    }

}



