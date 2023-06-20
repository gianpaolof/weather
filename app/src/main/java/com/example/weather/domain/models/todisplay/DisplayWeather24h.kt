package com.example.weather.domain.models.todisplay

data class DisplayWeather24h(
    val temperature: Double = 0.0,
    val time: String = "",
    val typeOfWeather: Int? = null
)
