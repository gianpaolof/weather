package com.example.weather.app.di

import android.app.Application
import android.content.Context
import com.example.weather.app.presentation.city_presenter.CityPresenterImpl
import com.example.weather.app.presentation.weather_presenter.WeatherPresenterImpl
import com.example.weather.app.schedulers.AppSchedulerProvider
import com.example.weather.app.schedulers.SchedulerProvider
import com.example.weather.domain.usecase.city.GetCityUseCase
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
    ): WeatherPresenterImpl {
        return WeatherPresenterImpl(
            getWeatherNowUseCase = getWeatherNowUseCase,
            getWeather24hUseCase = getWeather24hUseCase,
            getWeather14dUseCase = getWeather14dUseCase,
            schedulers = schedulerProvider,
        )
    }

    @Provides
    fun provideCityPresenter(
        getCityUseCase: GetCityUseCase,
        schedulerProvider: SchedulerProvider,
    ): CityPresenterImpl {
        return CityPresenterImpl(
            getCityUseCase = getCityUseCase,
            schedulers = schedulerProvider
        )
    }

}