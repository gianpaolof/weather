package com.example.weather.domain.repository

import com.example.weather.domain.models.todisplay.DisplayWeather14d
import com.example.weather.domain.models.todisplay.DisplayWeather24h
import com.example.weather.domain.models.todisplay.DisplayWeatherNow
import com.example.weather.domain.models.todisplay.Summary
import io.reactivex.Single

interface WeatherRepository {

    fun getWeatherNow(): Single<DisplayWeatherNow>

    fun getWeather24h(): Single<Pair<DisplayWeather24h, List<DisplayWeather24h>>>

    fun getWeather14d(pickedDate: String?): Pair<Single<Pair<DisplayWeather14d, List<DisplayWeather14d>>>, Single<Summary>>

}