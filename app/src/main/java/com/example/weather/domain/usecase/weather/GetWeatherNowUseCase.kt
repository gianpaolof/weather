package com.example.weather.domain.usecase.weather

import com.example.weather.domain.models.todisplay.DisplayWeatherNow
import com.example.weather.domain.repository.WeatherRepository
import io.reactivex.Single

class GetWeatherNowUseCase(private val weatherRepository: WeatherRepository) {

    fun getWeatherNow(latitude: Double, longitude: Double): Single<DisplayWeatherNow> {
        return weatherRepository.getWeatherNow(latitude, longitude)
    }

}