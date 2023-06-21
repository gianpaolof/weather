package com.example.weather.data.api.weather

import com.example.weather.domain.models.weather14d.Weather14d
import com.example.weather.domain.models.weather24h.Weather24h
import com.example.weather.domain.models.weathernow.WeatherNow
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET(
        "v1/forecast?latitude=&longitude=&" +
                "hourly=relativehumidity_2m,rain,showers,snowfall,cloudcover&" +
                "daily=temperature_2m_max,temperature_2m_min,precipitation_probability_max&" +
                "current_weather=true&windspeed_unit=ms&forecast_days=1&timezone=auto"
    )
    fun getWeatherNow(
        @Query("latitude") lat: Double,
        @Query("longitude") long: Double
    ): Single<WeatherNow>

    @GET(
        "v1/forecast?latitude=&longitude=&" +
                "hourly=temperature_2m,rain,showers,snowfall,cloudcover,is_day&" +
                "current_weather=true&windspeed_unit=ms&forecast_days=2&timezone=auto"
    )
    fun getWeather24h(
        @Query("latitude") lat: Double,
        @Query("longitude") long: Double
    ): Single<Weather24h>

    @GET(
        "v1/forecast?latitude=&longitude=&" +
                "hourly=cloudcover&" +
                "daily=temperature_2m_max,temperature_2m_min,apparent_temperature_max,apparent_temperature_min,sunrise,sunset,rain_sum,showers_sum,snowfall_sum,precipitation_probability_max,windspeed_10m_max&" +
                "current_weather=true&windspeed_unit=ms&forecast_days=14&timezone=auto"
    )
    fun getWeather14d(
        @Query("latitude") lat: Double,
        @Query("longitude") long: Double
    ): Single<Weather14d>

}