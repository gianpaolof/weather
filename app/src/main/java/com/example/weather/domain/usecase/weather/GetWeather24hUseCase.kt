package com.example.weather.domain.usecase.weather

import com.example.weather.domain.models.todisplay.DisplayWeather24h
import com.example.weather.domain.repository.WeatherRepository
import io.reactivex.Single

class GetWeather24hUseCase(private val weatherRepository: WeatherRepository) {

    fun getWeather24h(): Single<Pair<DisplayWeather24h, List<DisplayWeather24h>>> {
        return weatherRepository.getWeather24h()
    }

}