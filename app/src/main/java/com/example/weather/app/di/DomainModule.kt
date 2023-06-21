package com.example.weather.app.di

import com.example.weather.domain.repository.CityRepository
import com.example.weather.domain.repository.WeatherRepository
import com.example.weather.domain.usecase.city.GetCityUseCase
import com.example.weather.domain.usecase.weather.GetWeather14dUseCase
import com.example.weather.domain.usecase.weather.GetWeather24hUseCase
import com.example.weather.domain.usecase.weather.GetWeatherNowUseCase
import dagger.Module
import dagger.Provides

@Module
class DomainModule {

    @Provides
    fun provideGetWeather14dUseCase(weatherRepository: WeatherRepository): GetWeather14dUseCase {
        return GetWeather14dUseCase(weatherRepository = weatherRepository)
    }

    @Provides
    fun provideGetWeather24hUseCase(weatherRepository: WeatherRepository): GetWeather24hUseCase {
        return GetWeather24hUseCase(weatherRepository = weatherRepository)
    }

    @Provides
    fun provideGetWeatherNowUseCase(weatherRepository: WeatherRepository): GetWeatherNowUseCase {
        return GetWeatherNowUseCase(weatherRepository = weatherRepository)
    }

    @Provides
    fun provideGetCityUseCase(cityRepository: CityRepository): GetCityUseCase {
        return GetCityUseCase(cityRepository = cityRepository)
    }

}