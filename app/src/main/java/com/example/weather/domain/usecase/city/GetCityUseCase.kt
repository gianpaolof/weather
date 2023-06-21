package com.example.weather.domain.usecase.city

import com.example.weather.domain.models.city.CityModel
import com.example.weather.domain.repository.CityRepository
import io.reactivex.Observable

class GetCityUseCase(private val cityRepository: CityRepository) {
    fun getCity(city: String): Observable<List<CityModel>> = cityRepository.getCity(city)
}