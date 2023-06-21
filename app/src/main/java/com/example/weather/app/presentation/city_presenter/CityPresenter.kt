package com.example.weather.app.presentation.city_presenter

import com.example.weather.app.presentation.main.MainView

interface CityPresenter {
    fun attachView(view: MainView)
    fun detachView()
    fun getCity(city: String)
}