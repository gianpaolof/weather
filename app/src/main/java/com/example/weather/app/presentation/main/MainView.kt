package com.example.weather.app.presentation.main

import com.example.weather.domain.models.todisplay.DisplayWeather14d
import com.example.weather.domain.models.todisplay.DisplayWeather24h
import com.example.weather.domain.models.todisplay.DisplayWeatherNow
import com.example.weather.domain.models.todisplay.Summary


interface MainView {

    fun showProgress()
    fun hideProgress()
    fun showWeatherNow(displayWeatherNow: DisplayWeatherNow)
    fun showWeather24h(displayWeather24h: DisplayWeather24h)
    fun showWeather14d(displayWeather14d: DisplayWeather14d)
    fun showWeatherSummary(summary: Summary)

}