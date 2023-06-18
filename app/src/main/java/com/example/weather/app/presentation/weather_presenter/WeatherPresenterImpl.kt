package com.example.weather.app.presentation.weather_presenter

import com.example.weather.app.presentation.main.MainView
import com.example.weather.domain.usecase.weather.GetWeather14dUseCase
import com.example.weather.domain.usecase.weather.GetWeather24hUseCase
import com.example.weather.domain.usecase.weather.GetWeatherNowUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class WeatherPresenterImpl(
    private val view: MainView,
    private val getWeatherNowUseCase: GetWeatherNowUseCase,
    private val getWeather24hUseCase: GetWeather24hUseCase,
    private val getWeather14dUseCase: GetWeather14dUseCase,
) : WeatherPresenter {

    private val disposables: CompositeDisposable = CompositeDisposable()

    override fun getWeatherNow() {
        disposables.add(getWeatherNowUseCase.getWeatherNow()
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    view.showWeatherNow(it)
                }, {
                    throw RuntimeException(it.message)
                }
            )
        )
    }

    override fun getWeather24h() {
        disposables.add(getWeather24hUseCase.getWeather24h()
            .subscribeOn(Schedulers.single())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    view.showWeather24h(it)
                }, {
                    throw RuntimeException(it.message)
                }
            )
        )
    }

    override fun getWeather14d(pickedDate: String?) {
        disposables.add(getWeather14dUseCase.getWeather14d(pickedDate = pickedDate)
            .first
            .subscribeOn(Schedulers.single())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    view.showWeather14d(it)
                }, {
                    throw RuntimeException(it.message)
                }
            )
        )
        disposables.add(getWeather14dUseCase.getWeather14d(pickedDate = pickedDate)
            .second
            .subscribeOn(Schedulers.single())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    view.showWeatherSummary(it)
                }, {
                    throw RuntimeException(it.message)
                }
            )
        )
    }

    override fun unsubscribe() {
        disposables.dispose()
    }
}