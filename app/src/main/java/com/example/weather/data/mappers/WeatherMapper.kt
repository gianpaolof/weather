package com.example.weather.data.mappers

import android.content.Context
import com.example.weather.R
import com.example.weather.domain.models.todisplay.DisplayWeather14d
import com.example.weather.domain.models.todisplay.DisplayWeather24h
import com.example.weather.domain.models.todisplay.DisplayWeatherNow
import com.example.weather.domain.models.todisplay.Summary
import com.example.weather.domain.models.weather14d.Weather14d
import com.example.weather.domain.models.weather24h.Weather24h
import com.example.weather.domain.models.weathernow.WeatherNow
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

class WeatherMapper {

    fun mapApiToDisplay(weatherNow: WeatherNow) : DisplayWeatherNow {

        val currentWeather = weatherNow.current_weather
        val isDay = currentWeather.is_day == 1
        val currentTime = currentWeather.time
        val currentTemperature = "${currentWeather.temperature}°C"

        val minTemperature = weatherNow.daily.temperature_2m_min[INDEX_TODAY]
        val maxTemperature = weatherNow.daily.temperature_2m_max[INDEX_TODAY]
        val minMaxTemperature = "${minTemperature}°/" + "${maxTemperature}°"

        val todayDate = currentTime.substringBefore("T")
        val localeDate = LocalDate.parse(todayDate)
        val day = localeDate.dayOfMonth
        val month = localeDate.month.getDisplayName(TextStyle.FULL, Locale.getDefault())

        val todayDateText = "$day $month"

        val daily = weatherNow.daily
        val hourly = weatherNow.hourly
        val hourlyCurrentTime = hourly.time.filter { it == currentTime }
        val index = hourlyCurrentTime.indexOf(currentTime)

        val precipitationProbability =
            "${daily.precipitation_probability_max.first()}" +
                    weatherNow.daily_units.precipitation_probability_max

        val windSpeed =
            "${currentWeather.windspeed}" +
                    weatherNow.current_weather.windspeed

        val relativeHumidity =
            "${hourly.relativehumidity_2m[index]}" +
                    weatherNow.hourly_units.relativehumidity_2m

        val isRain = hourly.rain[index] > 0
        val isShowers = hourly.showers[index] > 0
        val isSnow = hourly.snowfall[index] > 0

        val typeOfWeather = when {
            isRain -> DisplayWeatherNow.TYPE_OF_WEATHER_RAIN
            isShowers -> DisplayWeatherNow.TYPE_OF_WEATHER_SHOWERS
            isSnow -> DisplayWeatherNow.TYPE_OF_WEATHER_SNOW
            isDay -> DisplayWeatherNow.TYPE_OF_WEATHER_DAY
            !isDay -> DisplayWeatherNow.TYPE_OF_WEATHER_NIGHT
            else -> throw RuntimeException("Unknown type of weather")
        }

        return DisplayWeatherNow(
            temperature = currentTemperature,
            minMaxTemperature = minMaxTemperature,
            precipitationProbability = precipitationProbability,
            relativeHumidity = relativeHumidity,
            windSpeed = windSpeed,
            isDay = isDay,
            todayDate = todayDateText,
            typeOfWeatherNow = typeOfWeather,
        )
    }

    fun mapApiToDisplay(weather24h: Weather24h) : DisplayWeather24h {
        var displayWeather24h = DisplayWeather24h()
        val hourly = weather24h.hourly
        val isDay = hourly.is_day
        val currentTime = weather24h.current_weather.time
        val hourlyTime = hourly.time.filter { it == currentTime }
        hourlyTime.forEach {
            val index = hourlyTime.indexOf(currentTime)

            val displayIsDay = isDay[index] == 1
            val displayTime = it
            val displayTemp = hourly.temperature_2m[index]

            val isRain = hourly.rain[index] > 0
            val isShowers = hourly.showers[index] > 0
            val isSnow = hourly.snowfall[index] > 0

            val typeOfWeather = when {
                isRain -> DisplayWeather24h.TYPE_OF_WEATHER_RAIN
                isShowers -> DisplayWeather24h.TYPE_OF_WEATHER_SHOWERS
                isSnow -> DisplayWeather24h.TYPE_OF_WEATHER_SNOW
                displayIsDay -> DisplayWeather24h.TYPE_OF_WEATHER_DAY
                !displayIsDay -> DisplayWeather24h.TYPE_OF_WEATHER_NIGHT
                else -> throw RuntimeException("Unknown type of weather")
            }

            displayWeather24h = DisplayWeather24h(
                temperature = displayTemp,
                time = displayTime,
                typeOfWeather = typeOfWeather,
            )
        }
        return displayWeather24h
    }

    fun mapApiToDisplay(weather14d: Weather14d, pickedDate: String?, context: Context) : Pair<DisplayWeather14d, Summary>  {

        var displayWeather14d = DisplayWeather14d()
        var summary = Summary()
        val daily = weather14d.daily
        val isDay = weather14d.current_weather.is_day == 1
        val dailyTime = daily.time

        dailyTime.forEachIndexed { index, s ->

            val date = LocalDate.parse(s)
            val dayOfWeek = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())

            val formatter = DateTimeFormatter.ofPattern("dd/MM")
            val formattedDate = date.format(formatter)

            val isRain = daily.rain_sum[index] > 0
            val isShowers = daily.showers_sum[index] > 0
            val isSnow = daily.snowfall_sum[index] > 0

            val minTemperature = daily.temperature_2m_min[index]
            val maxTemperature = daily.temperature_2m_max[index]

            val typeOfWeather = when {
                isRain -> DisplayWeather14d.TYPE_OF_WEATHER_RAIN
                isShowers -> DisplayWeather14d.TYPE_OF_WEATHER_SHOWERS
                isSnow -> DisplayWeather14d.TYPE_OF_WEATHER_SNOW
                isDay -> DisplayWeather14d.TYPE_OF_WEATHER_DAY
                !isDay -> DisplayWeather14d.TYPE_OF_WEATHER_NIGHT
                else -> throw RuntimeException("Unknown type of weather")
            }

            val isPicked = if (pickedDate == null) true else pickedDate == formattedDate

            displayWeather14d = DisplayWeather14d(
                dayOfWeek = dayOfWeek,
                date = formattedDate,
                minTemperature = minTemperature,
                maxTemperature = maxTemperature,
                isPicked = isPicked,
                typeOfWeather = typeOfWeather,
            )

            val dateWeatherParse = LocalDate.parse(s)

            val day = dateWeatherParse.dayOfMonth
            val month = dateWeatherParse.month.getDisplayName(
                TextStyle.FULL,
                Locale.getDefault()
            )

            val summaryDate = "$day $month"

            val minMaxTemperature = "${minTemperature}°/" + "${maxTemperature}°"

            val drop = if (daily.precipitation_probability_max[index] == null) {
                context.resources.getString(R.string.unknown)
            } else {
                "${daily.precipitation_probability_max[index]}" +
                        weather14d.daily_units.precipitation_probability_max
            }

            val wind = "${daily.windspeed_10m_max[index]}" +
                    weather14d.daily_units.windspeed_10m_max

            val sunrise = daily.sunrise[index].substringAfter("T")
            val sunset = daily.sunset[index].substringAfter("T")

            val apparentMaxMinTemperature = "${daily.apparent_temperature_min}°/" +
                    "${daily.apparent_temperature_max}°"

            summary = Summary(
                date = summaryDate,
                min_max_temperature = minMaxTemperature,
                drop = drop,
                wind = wind,
                sunrise = sunrise,
                sunset = sunset,
                apparent_min_max_temperature = apparentMaxMinTemperature,
            )
        }

        return Pair(first = displayWeather14d, second = summary)
    }

    companion object {
        private const val INDEX_TODAY = 0
    }

}