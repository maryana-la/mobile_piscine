package com.mobilepiscine42.mediumweatherapp.geocoding_api

data class GeoOption(
    val generationtime_ms: Double,
    val results: List<Result>
)