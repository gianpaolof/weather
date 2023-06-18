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
) {
    companion object {
        const val TYPE_OF_WEATHER_RAIN = 0
        const val TYPE_OF_WEATHER_SHOWERS = 1
        const val TYPE_OF_WEATHER_SNOW = 2
        const val TYPE_OF_WEATHER_SNOW_AND_RAIN = 3
        const val TYPE_OF_WEATHER_DAY = 4
        const val TYPE_OF_WEATHER_NIGHT = 5
    }
}