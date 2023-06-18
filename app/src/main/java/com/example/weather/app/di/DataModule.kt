package com.example.weather.app.di

import android.content.Context
import com.example.weather.data.repository.WeatherRepositoryImpl
import com.example.weather.domain.repository.WeatherRepository
import dagger.Module
import dagger.Provides

@Module
class DataModule {

    @Provides
    fun provideWeatherRepository(context: Context): WeatherRepository {
        return WeatherRepositoryImpl(context)
    }

}