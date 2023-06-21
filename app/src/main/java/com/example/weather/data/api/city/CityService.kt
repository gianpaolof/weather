package com.example.weather.data.api.city

import com.example.weather.domain.models.city.CityModel
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface CityService {

    @GET("search?&format=json")
    fun getCity(@Query("city") city: String): Observable<List<CityModel>>

}