package com.example.weather.domain.models.weathernow

data class HourlyUnits(
    val cloudcover: String,
    val rain: String,
    val relativehumidity_2m: String,
    val showers: String,
    val snowfall: String,
    val time: String
)