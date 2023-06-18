package com.example.weather.app.di

import android.content.Context
import com.example.weather.app.presentation.main.MainActivity
import com.example.weather.app.presentation.main.MainView
import com.example.weather.app.presentation.weather_presenter.WeatherPresenter
import com.example.weather.app.presentation.weather_presenter.WeatherPresenterImpl
import com.example.weather.domain.usecase.weather.GetWeather14dUseCase
import com.example.weather.domain.usecase.weather.GetWeather24hUseCase
import com.example.weather.domain.usecase.weather.GetWeatherNowUseCase
import dagger.Module
import dagger.Provides

@Module
class AppModule(private val context: Context) {

    @Provides
    fun provideContext(): Context {
        return context
    }

    @Provides
    fun provideMainView(): MainView {
        return MainActivity()
    }

    @Provides
    fun provideWeatherPresenter(
        view: MainView,
        getWeatherNowUseCase: GetWeatherNowUseCase,
        getWeather24hUseCase: GetWeather24hUseCase,
        getWeather14dUseCase: GetWeather14dUseCase,
    ): WeatherPresenter {
        return WeatherPresenterImpl(
            view = view,
            getWeatherNowUseCase = getWeatherNowUseCase,
            getWeather24hUseCase = getWeather24hUseCase,
            getWeather14dUseCase = getWeather14dUseCase,
        )
    }

}