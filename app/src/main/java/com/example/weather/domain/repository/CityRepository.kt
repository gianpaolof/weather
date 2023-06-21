package com.example.weather.domain.repository

import com.example.weather.domain.models.city.CityModel
import io.reactivex.Observable

interface CityRepository {

    fun getCity(city: String): Observable<List<CityModel>>

}