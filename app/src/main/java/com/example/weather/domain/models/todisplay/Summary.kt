package com.example.weather.domain.models.todisplay

data class Summary(
    val date: String = "",
    val max_min_temperature: String = "",
    val drop: String = "",
    val wind: String = "",
    val sunrise: String = "",
    val sunset: String = "",
    val apparent_max_min_temperature: String = ""
)
