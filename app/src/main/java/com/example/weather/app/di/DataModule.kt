package com.example.weather.app.di

import android.content.Context
import com.example.weather.app.schedulers.SchedulerProvider
import com.example.weather.data.mappers.WeatherMapper
import com.example.weather.data.repository.CityRepositoryImpl
import com.example.weather.data.repository.WeatherRepositoryImpl
import com.example.weather.domain.repository.CityRepository
import com.example.weather.domain.repository.WeatherRepository
import dagger.Module
import dagger.Provides

@Module
class DataModule {

    @Provides
    fun provideWeatherMapper(context: Context): WeatherMapper = WeatherMapper(context)

    @Provides
    fun provideWeatherRepository(
        weatherMapper: WeatherMapper,
        schedulerProvider: SchedulerProvider
    ): WeatherRepository {
        return WeatherRepositoryImpl(mapper = weatherMapper, schedulers = schedulerProvider)
    }

    @Provides
    fun provideCityRepository(): CityRepository = CityRepositoryImpl()

}