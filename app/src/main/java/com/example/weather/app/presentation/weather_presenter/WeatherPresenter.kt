package com.example.weather.app.presentation.weather_presenter

import com.example.weather.app.presentation.main.MainView

interface WeatherPresenter {

    fun attachView(view: MainView)

    fun detachView()

    fun getWeatherNow(latitude: Double, longitude: Double)

    fun getWeather24h(latitude: Double, longitude: Double)

    fun getWeather14d(pickedDate: String?, latitude: Double, longitude: Double)

    fun setLocation(longitude: Double, latitude: Double)

    fun getLocation()

}