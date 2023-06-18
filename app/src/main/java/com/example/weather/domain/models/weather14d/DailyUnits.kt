package com.example.weather.domain.models.weather14d

data class DailyUnits(
    val apparent_temperature_max: String,
    val apparent_temperature_min: String,
    val precipitation_probability_max: String,
    val rain_sum: String,
    val showers_sum: String,
    val snowfall_sum: String,
    val sunrise: String,
    val sunset: String,
    val temperature_2m_max: String,
    val temperature_2m_min: String,
    val time: String,
    val windspeed_10m_max: String
)