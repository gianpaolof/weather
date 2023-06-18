package com.example.weather.domain.models.weather24h

data class HourlyUnits(
    val cloudcover: String,
    val is_day: String,
    val rain: String,
    val showers: String,
    val snowfall: String,
    val temperature_2m: String,
    val time: String
)