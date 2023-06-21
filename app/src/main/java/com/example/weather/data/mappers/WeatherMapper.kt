package com.example.weather.data.mappers

import android.content.Context
import com.example.weather.R
import com.example.weather.data.WeatherType.*
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

class WeatherMapper(private val context: Context) {

    fun mapApiToDisplay(weatherNow: WeatherNow) : DisplayWeatherNow {
        val currentWeather = weatherNow.current_weather
        val isDay = currentWeather.is_day == 1
        val currentTime = currentWeather.time
        val currentTemperature = "${currentWeather.temperature}°C"

        val minTemperature = weatherNow.daily.temperature_2m_min[INDEX_TODAY]
        val maxTemperature = weatherNow.daily.temperature_2m_max[INDEX_TODAY]
        val maxMinTemperature = "${maxTemperature}°/" + "${minTemperature}°"

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

        val windSpeed = "${currentWeather.windspeed} " + context.resources.getString(R.string.m_s)

        val relativeHumidity =
            "${hourly.relativehumidity_2m[index]}" +
                    weatherNow.hourly_units.relativehumidity_2m

        val isRain = hourly.rain[index] > 0
        val isShowers = hourly.showers[index] > 0
        val isSnow = hourly.snowfall[index] > 0

        val typeOfWeather = when {
            isRain -> RAIN
            isShowers -> SHOWERS
            isSnow -> SNOW
            isDay -> DAY
            !isDay -> NIGHT
            else -> throw RuntimeException("Unknown type of weather")
        }

        return DisplayWeatherNow(
            temperature = currentTemperature,
            maxMinTemperature = maxMinTemperature,
            precipitationProbability = precipitationProbability,
            relativeHumidity = relativeHumidity,
            windSpeed = windSpeed,
            isDay = isDay,
            todayDate = todayDateText,
            typeOfWeatherNow = typeOfWeather.type,
        )
    }

    fun mapApiToDisplay(weather24h: Weather24h) : Pair<DisplayWeather24h, List<DisplayWeather24h>>  {
        val list = mutableListOf<DisplayWeather24h>()
        var displayWeather24h = DisplayWeather24h()
        val hourly = weather24h.hourly
        val isDay = hourly.is_day
        val currentTime = weather24h.current_weather.time
        val hourlyTime = hourly.time
        hourlyTime.forEach {
            if (it == currentTime) {
                val index = hourlyTime.indexOf(currentTime)
                val range = index..index + FULL_DAY_HOURS

                for (i in range) {
                    val displayIsDay = isDay[i] == 1
                    val displayTime = hourly.time[i].substringAfter("T")

                    val isRain = hourly.rain[i] > 0
                    val isShowers = hourly.showers[i] > 0
                    val isSnow = hourly.snowfall[i] > 0

                    val typeOfWeather = when {
                        isRain -> RAIN
                        isShowers -> SHOWERS
                        isSnow -> SNOW
                        displayIsDay -> DAY
                        !displayIsDay -> NIGHT
                        else -> throw RuntimeException("Unknown type of weather")
                    }

                    displayWeather24h = DisplayWeather24h(
                        temperature = hourly.temperature_2m[i],
                        time = displayTime,
                        typeOfWeather = typeOfWeather.type,
                    )
                    list.add(displayWeather24h)
                }
            }
        }


        return Pair(first = displayWeather24h, second = list)
    }

    fun mapApiToDisplay(
        weather14d: Weather14d,
        pickedDate: String?,
    ) : Pair<Pair<DisplayWeather14d, List<DisplayWeather14d>>, Summary> {

        val list = mutableListOf<DisplayWeather14d>()
        var displayWeather14d = DisplayWeather14d()
        var summary = Summary()
        val daily = weather14d.daily
        val isDay = weather14d.current_weather.is_day == 1
        val dailyTime = daily.time

        val formatter = DateTimeFormatter.ofPattern("dd/MM")

        dailyTime.forEachIndexed { index, s ->

            val date = LocalDate.parse(s)
            val dayOfWeek = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())

            val formattedDate = date.format(formatter)

            val isRain = daily.rain_sum[index] > 0
            val isShowers = daily.showers_sum[index] > 0
            val isSnow = daily.snowfall_sum[index] > 0

            val minTemperature = daily.temperature_2m_min[index]
            val maxTemperature = daily.temperature_2m_max[index]

            val typeOfWeather = when {
                isRain -> RAIN
                isShowers -> SHOWERS
                isSnow -> SNOW
                isDay -> DAY
                !isDay -> NIGHT
                else -> throw RuntimeException("Unknown type of weather")
            }

            val isPicked = if (pickedDate == null && index == 0) true else pickedDate == formattedDate

            displayWeather14d = DisplayWeather14d(
                dayOfWeek = dayOfWeek,
                date = formattedDate,
                minTemperature = minTemperature,
                maxTemperature = maxTemperature,
                isPicked = isPicked,
                typeOfWeather = typeOfWeather.type,
            )

            list.add(displayWeather14d)

            if (isPicked) {

                val dateWeatherParse = LocalDate.parse(s)
                val day = dateWeatherParse.dayOfMonth
                val month = dateWeatherParse.month.getDisplayName(
                    TextStyle.FULL,
                    Locale.getDefault()
                )

                val summaryDate = "$day $month"

                val maxMinTemperature = "${maxTemperature}°/" + "${minTemperature}°"

                val drop = if (daily.precipitation_probability_max[index] == null) {
                    context.resources.getString(R.string.unknown)
                } else {
                    "${daily.precipitation_probability_max[index]}" +
                            weather14d.daily_units.precipitation_probability_max
                }

                val wind = "${daily.windspeed_10m_max[index]} " +
                        context.resources.getString(R.string.m_s)

                val sunrise = daily.sunrise[index].substringAfter("T")
                val sunset = daily.sunset[index].substringAfter("T")

                val apparentMaxMinTemperature = "${daily.apparent_temperature_max[index]}°/" +
                        "${daily.apparent_temperature_min[index]}°"

                summary = Summary(
                    date = summaryDate,
                    max_min_temperature = maxMinTemperature,
                    drop = drop,
                    wind = wind,
                    sunrise = sunrise,
                    sunset = sunset,
                    apparent_max_min_temperature = apparentMaxMinTemperature,
                )
            }

        }

        return Pair(first = Pair(first = displayWeather14d, second = list), second = summary)
    }

    companion object {
        private const val INDEX_TODAY = 0
        private const val FULL_DAY_HOURS = 23
    }

}