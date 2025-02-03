package com.mobilepiscine42.mediumweatherapp.reverse_geocoding_api

data class Location(
    val spatialReference: SpatialReference,
    val x: Double,
    val y: Double
)