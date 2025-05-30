package com.mobilepiscine42.mediumweatherapp.reverse_geocoding_api

import com.mobilepiscine42.mediumweatherapp.api.Constant
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private fun getInstance() : Retrofit {
        return  Retrofit.Builder()
            .baseUrl(Constant.REVERSE_GEOCODING_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val reverseGeoApi : ReverseGeoApi = getInstance().create(ReverseGeoApi::class.java)
}