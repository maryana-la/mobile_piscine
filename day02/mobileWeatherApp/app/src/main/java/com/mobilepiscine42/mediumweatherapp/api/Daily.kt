package com.mobilepiscine42.mediumweatherapp.api

data class Daily(
    val temperature_2m_max: List<Double>,
    val temperature_2m_min: List<Double>,
    val time: List<String>,
    val wind_speed_10m_max: List<Double>
)