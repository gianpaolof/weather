package com.example.weather.domain.usecase.weather

import com.example.weather.domain.models.todisplay.DisplayWeather14d
import com.example.weather.domain.models.todisplay.Summary
import com.example.weather.domain.repository.WeatherRepository
import io.reactivex.Single

class GetWeather14dUseCase (private val weatherRepository: WeatherRepository) {

    fun getWeather14d(pickedDate: String?): Pair<Single<Pair<DisplayWeather14d, List<DisplayWeather14d>>>, Single<Summary>> {
        return weatherRepository.getWeather14d(pickedDate = pickedDate)
    }

}