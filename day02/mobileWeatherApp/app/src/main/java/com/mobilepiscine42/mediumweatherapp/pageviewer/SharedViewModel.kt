package com.mobilepiscine42.mediumweatherapp.pageviewer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobilepiscine42.mediumweatherapp.api.WeatherModel
import com.mobilepiscine42.mediumweatherapp.geocoding_api.Result



class SharedViewModel : ViewModel() {

    private var cityOptions : MutableList<Result> = mutableListOf()
    private var weatherForecast = MutableLiveData<WeatherModel>()

    val forecastLiveData: LiveData<WeatherModel> get() = weatherForecast
    private var currentCity = MutableLiveData<Result>()
    val cityLiveData: LiveData<Result> get() = currentCity


    val errorLiveData: LiveData<String> get() = errorMsg
    private var errorMsg = MutableLiveData<String>()


    fun setCityOptions(fromAPI : List<Result>) {
        cityOptions.clear()
        cityOptions.addAll(fromAPI)
    }

    fun getCityOptions() : List<Result> {
        return cityOptions.toList()
    }

    fun setWeatherForecast(forecast : WeatherModel) {
        weatherForecast.value = forecast
    }

    fun getWeatherForecast() : WeatherModel {
        return weatherForecast.value!!
    }

    fun setCurrentCity (city : Result) {
        currentCity.value = city
    }

    fun getCurrentCity() : Result {
        return currentCity.value!!
    }

    fun setErrorMsg(message : String) {
        errorMsg.value = message
    }

    fun getErrorMsg() : String {
        return errorMsg.value!!
    }

}