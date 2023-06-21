package com.example.weather.data.repository

import com.example.weather.data.api.city.CityInstance
import com.example.weather.domain.models.city.CityModel
import com.example.weather.domain.repository.CityRepository
import io.reactivex.Observable

class CityRepositoryImpl: CityRepository {
    override fun getCity(city: String): Observable<List<CityModel>> = CityInstance.api.getCity(city)
}