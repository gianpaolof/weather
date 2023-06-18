package com.example.weather.domain.models.todisplay

data class DisplayWeather14d(
    val dayOfWeek: String = "",
    val date: String = "",
    val minTemperature: Double = 0.0,
    val maxTemperature: Double = 0.0,
    val isPicked: Boolean? = null,
    val typeOfWeather: Int? = null
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
