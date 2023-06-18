package com.example.weather.domain.models.weather24h

data class Hourly(
    val cloudcover: List<Int>,
    val is_day: List<Int>,
    val rain: List<Double>,
    val showers: List<Double>,
    val snowfall: List<Double>,
    val temperature_2m: List<Double>,
    val time: List<String>
)