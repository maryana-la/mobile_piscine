package com.mobilepiscine42.mediumweatherapp.api

data class CurrentUnits(
    val interval: String,
    val temperature_2m: String,
    val time: String,
    val weather_code: String,
    val wind_direction_10m: String,
    val wind_speed_10m: String
)