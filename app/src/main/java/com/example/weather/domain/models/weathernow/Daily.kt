package com.example.weather.domain.models.weathernow

data class Daily(
    val precipitation_probability_max: List<Int>,
    val temperature_2m_max: List<Double>,
    val temperature_2m_min: List<Double>,
    val time: List<String>
)