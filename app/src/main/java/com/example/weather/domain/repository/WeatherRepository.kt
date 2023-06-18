package com.example.weather.domain.repository

import com.example.weather.domain.models.todisplay.DisplayWeather14d
import com.example.weather.domain.models.todisplay.DisplayWeather24h
import com.example.weather.domain.models.todisplay.DisplayWeatherNow
import com.example.weather.domain.models.todisplay.Summary
import io.reactivex.Single

interface WeatherRepository {

    fun getWeatherNow(): Single<DisplayWeatherNow>

    fun getWeather24h(): Single<DisplayWeather24h>

    fun getWeather14d(pickedDate: String?): Pair<Single<DisplayWeather14d>, Single<Summary>>

}