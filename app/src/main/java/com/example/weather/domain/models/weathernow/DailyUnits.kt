package com.example.weather.domain.models.weathernow

data class DailyUnits(
    val precipitation_probability_max: String,
    val temperature_2m_max: String,
    val temperature_2m_min: String,
    val time: String
)