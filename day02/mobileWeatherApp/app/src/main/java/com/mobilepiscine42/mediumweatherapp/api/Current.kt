package com.mobilepiscine42.mediumweatherapp.api

data class Current(
    val interval: Int,
    val temperature_2m: Double,
    val time: String
)