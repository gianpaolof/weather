package com.example.weather.app.presentation.weather_presenter

interface WeatherPresenter {

    fun getWeatherNow()

    fun getWeather24h()

    fun getWeather14d(pickedDate: String?)

    fun unsubscribe()

}