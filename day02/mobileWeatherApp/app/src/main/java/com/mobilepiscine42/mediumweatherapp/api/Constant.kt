package com.mobilepiscine42.mediumweatherapp.api

object Constant {
    val REQUEST_CODE_LOCATION_PERMISSION = 1001
    const val WEATHER_API_BASE_URL = "https://api.open-meteo.com/v1/"
    const val GEOCODING_URL = "https://geocoding-api.open-meteo.com/v1/"
    const val WEATHER_API_ENDPOINT = "forecast?"
    const val WEATHER_API_SEARCH = "search"
    const val NUMBER_OF_SEARCH_RESULT = 10
    const val HOURLY = "temperature_2m,weather_code,wind_speed_10m,wind_direction_10m"
    const val CURRENT = "temperature_2m,weather_code,wind_speed_10m,wind_direction_10m"
    const val DAILY = "weather_code,temperature_2m_max,temperature_2m_min"
    const val FORECAST_DAYS = 7

}

//https://api.open-meteo.com/v1/forecast?latitude=52.52&longitude=13.41&
// current=temperature_2m,weather_code,wind_speed_10m,wind_direction_10m&
// hourly=temperature_2m,weather_code,wind_speed_10m,wind_direction_10m&
// daily=weather_code,temperature_2m_max,temperature_2m_min

//https://api.open-meteo.com/v1/forecast?latitude=52.52&longitude=13.41&
// current=temperature_2m&
// hourly=temperature_2m&
// daily=temperature_2m_max,temperature_2m_min,wind_speed_10m_max