package com.example.weather.app.presentation.weather_presenter

import com.example.weather.app.presentation.main.MainView
import com.example.weather.app.schedulers.SchedulerProvider
import com.example.weather.domain.usecase.weather.GetWeather14dUseCase
import com.example.weather.domain.usecase.weather.GetWeather24hUseCase
import com.example.weather.domain.usecase.weather.GetWeatherNowUseCase
import io.reactivex.disposables.CompositeDisposable

class WeatherPresenterImpl(
    private val getWeatherNowUseCase: GetWeatherNowUseCase,
    private val getWeather24hUseCase: GetWeather24hUseCase,
    private val getWeather14dUseCase: GetWeather14dUseCase,
    private val schedulers: SchedulerProvider
) : WeatherPresenter {

    private val disposables: CompositeDisposable = CompositeDisposable()

    private var view: MainView? = null

    override fun attachView(view: MainView) {
        this.view = view
    }

    override fun detachView() {
        this.view = null
        disposables.dispose()
    }

    override fun getWeatherNow(latitude: Double, longitude: Double) {
        disposables.add(getWeatherNowUseCase.getWeatherNow(latitude, longitude)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
            .subscribe(
                {
                    view?.showWeatherNow(it)
                }, {
                    throw RuntimeException(it.message)
                }
            )
        )
    }

    override fun getWeather24h(latitude: Double, longitude: Double) {
        disposables.add(getWeather24hUseCase.getWeather24h(latitude, longitude)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
            .subscribe(
                {
                    view?.showWeather24h(
                        displayWeather24h = it.first,
                        listDisplayWeather24h = it.second
                    )
                }, {
                    throw RuntimeException(it.message)
                }
            )
        )
    }

    override fun getWeather14d(pickedDate: String?, latitude: Double, longitude: Double) {
        disposables.add(getWeather14dUseCase.getWeather14d(
            pickedDate = pickedDate,
            latitude = latitude,
            longitude = longitude
        )
            .first
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
            .subscribe(
                {
                    view?.showWeather14d(
                        displayWeather14d = it.first,
                        listDisplayWeather14d = it.second
                    )
                }, {
                    throw RuntimeException(it.message)
                }
            )
        )
        disposables.add(getWeather14dUseCase.getWeather14d(
            pickedDate = pickedDate,
            latitude = latitude,
            longitude = longitude
        )
            .second
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
            .subscribe(
                {
                    view?.showWeatherSummary(it)
                }, {
                    throw RuntimeException(it.message)
                }
            )
        )
    }
}