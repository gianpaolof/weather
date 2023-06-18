package com.example.weather.app.di

import android.app.Application
import android.content.Context
import com.example.weather.app.presentation.main.MainActivity
import com.example.weather.app.presentation.main.MainView
import com.example.weather.app.presentation.weather_presenter.WeatherPresenter
import com.example.weather.app.presentation.weather_presenter.WeatherPresenterImpl
import com.example.weather.app.schedulers.AppSchedulerProvider
import com.example.weather.app.schedulers.SchedulerProvider
import com.example.weather.domain.usecase.weather.GetWeather14dUseCase
import com.example.weather.domain.usecase.weather.GetWeather24hUseCase
import com.example.weather.domain.usecase.weather.GetWeatherNowUseCase
import dagger.Module
import dagger.Provides

@Module
class AppModule(private val application: Application) {

    @Provides
    fun provideContext(): Context = application.applicationContext

    @Provides
    fun provideSchedulerProvider(): SchedulerProvider = AppSchedulerProvider()

    @Provides
    fun provideWeatherPresenter(
        getWeatherNowUseCase: GetWeatherNowUseCase,
        getWeather24hUseCase: GetWeather24hUseCase,
        getWeather14dUseCase: GetWeather14dUseCase,
        schedulerProvider: SchedulerProvider,
    ): WeatherPresenter {
        return WeatherPresenterImpl(
            getWeatherNowUseCase = getWeatherNowUseCase,
            getWeather24hUseCase = getWeather24hUseCase,
            getWeather14dUseCase = getWeather14dUseCase,
            schedulers = schedulerProvider,
        )
    }

}