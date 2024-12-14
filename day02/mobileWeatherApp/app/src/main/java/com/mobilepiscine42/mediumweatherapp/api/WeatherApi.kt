package com.mobilepiscine42.mediumweatherapp.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

//https://api.open-meteo.com/v1/forecast?latitude=52.52&longitude=13.41&current=temperature_2m&hourly=temperature_2m&daily=temperature_2m_max,temperature_2m_min,wind_speed_10m_max

interface WeatherApi {

    @GET(Constant.WEATHER_API_ENDPOINT)
    suspend fun getWeather (
        @Query("latitude") latitude : String,
        @Query("longitude") longitude : String,
        @Query("current") current : String,
        @Query("hourly") hourly : String,
        @Query("daily") daily : String,
        @Query("forecast_days") forecastDays : Int
    ) : Response<WeatherModel>
}