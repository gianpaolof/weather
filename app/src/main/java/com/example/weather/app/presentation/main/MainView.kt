package com.example.weather.app.presentation.main

import androidx.lifecycle.LiveData
import com.example.weather.domain.models.city.CityModel
import com.example.weather.domain.models.todisplay.DisplayWeather14d
import com.example.weather.domain.models.todisplay.DisplayWeather24h
import com.example.weather.domain.models.todisplay.DisplayWeatherNow
import com.example.weather.domain.models.todisplay.Summary
import moxy.MvpView
import moxy.viewstate.strategy.SingleStateStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(SingleStateStrategy::class)
interface MainView : MvpView {

    fun showProgress()

    fun hideProgress()

    fun showWeatherNow(displayWeatherNow: DisplayWeatherNow)

    fun showWeather24h(displayWeather24h: DisplayWeather24h, listDisplayWeather24h: List<DisplayWeather24h>)

    fun showWeather14d(displayWeather14d: DisplayWeather14d, listDisplayWeather14d: List<DisplayWeather14d>)

    fun showWeatherSummary(summary: Summary)

    fun showCitiesResult(city: List<CityModel>)

    fun getLocation(location: LiveData<Pair<Double, Double>>)

}