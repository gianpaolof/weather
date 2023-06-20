package com.example.weather.domain.models.todisplay

data class DisplayWeatherNow(
    val temperature: String = "",
    val minMaxTemperature: String = "",
    val precipitationProbability: String = "",
    val relativeHumidity: String = "",
    val windSpeed: String = "",
    val isDay: Boolean = false,
    val todayDate: String = "",
    val typeOfWeatherNow: Int? = null
)