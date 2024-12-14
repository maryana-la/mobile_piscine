package com.mobilepiscine42.mediumweatherapp.api

data class Hourly(
    val temperature_2m: List<Double>,
    val time: List<String>
)