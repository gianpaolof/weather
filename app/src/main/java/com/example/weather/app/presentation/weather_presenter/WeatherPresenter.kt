package com.example.weather.app.presentation.weather_presenter

import com.example.weather.app.presentation.main.MainView

interface WeatherPresenter {

    fun attachView(view: MainView)

    fun detachView()

    fun getWeatherNow()

    fun getWeather24h()

    fun getWeather14d(pickedDate: String?)

}