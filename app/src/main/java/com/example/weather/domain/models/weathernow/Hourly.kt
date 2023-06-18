package com.example.weather.domain.models.weathernow

data class Hourly(
    val cloudcover: List<Int>,
    val rain: List<Double>,
    val relativehumidity_2m: List<Int>,
    val showers: List<Double>,
    val snowfall: List<Double>,
    val time: List<String>
)