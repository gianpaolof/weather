package com.example.weather.domain.models.todisplay

data class DisplayWeather14d(
    val dayOfWeek: String = "",
    val date: String = "",
    val minTemperature: Double = 0.0,
    val maxTemperature: Double = 0.0,
    val isPicked: Boolean? = null,
    val typeOfWeather: Int? = null
)
