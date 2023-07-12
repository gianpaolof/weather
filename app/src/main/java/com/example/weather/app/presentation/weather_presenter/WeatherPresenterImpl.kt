package com.example.weather.app.presentation.weather_presenter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weather.app.presentation.main.MainView
import com.example.weather.app.schedulers.SchedulerProvider
import com.example.weather.domain.usecase.weather.GetWeather14dUseCase
import com.example.weather.domain.usecase.weather.GetWeather24hUseCase
import com.example.weather.domain.usecase.weather.GetWeatherNowUseCase
import io.reactivex.disposables.CompositeDisposable
import moxy.InjectViewState
import moxy.MvpPresenter
import javax.inject.Inject

@InjectViewState
class WeatherPresenterImpl @Inject constructor(
    private val getWeatherNowUseCase: GetWeatherNowUseCase,
    private val getWeather24hUseCase: GetWeather24hUseCase,
    private val getWeather14dUseCase: GetWeather14dUseCase,
    private val schedulers: SchedulerProvider
) : MvpPresenter<MainView>() {

    private val disposables: CompositeDisposable = CompositeDisposable()

    private val _location = MutableLiveData<Pair<Double, Double>>()
    private val location: LiveData<Pair<Double, Double>> = _location

    override fun detachView(view: MainView?) {
        disposables.dispose()
    }

    fun getWeatherNow(latitude: Double, longitude: Double) {
        disposables.add(getWeatherNowUseCase.getWeatherNow(latitude, longitude)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
            .doOnSubscribe { viewState.showProgress() }
            .doFinally { viewState.hideProgress() }
            .subscribe(
                {
                    viewState.showWeatherNow(it)
                }, {
                    throw RuntimeException(it.message)
                }
            )
        )
    }

    fun getWeather24h(latitude: Double, longitude: Double) {
        disposables.add(getWeather24hUseCase.getWeather24h(latitude, longitude)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
            .doOnSubscribe { viewState.showProgress() }
            .doFinally { viewState.hideProgress() }
            .subscribe(
                {
                    viewState.showWeather24h(
                        displayWeather24h = it.first,
                        listDisplayWeather24h = it.second
                    )
                }, {
                    throw RuntimeException(it.message)
                }
            )
        )
    }

    fun getWeather14d(pickedDate: String?, latitude: Double, longitude: Double) {
        disposables.add(getWeather14dUseCase.getWeather14d(
            pickedDate = pickedDate,
            latitude = latitude,
            longitude = longitude
        )
            .first
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
            .doOnSubscribe { viewState.showProgress() }
            .doFinally { viewState.hideProgress() }
            .subscribe(
                {
                    viewState.showWeather14d(
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
            .doOnSubscribe { viewState.showProgress() }
            .doFinally { viewState.hideProgress() }
            .subscribe(
                {
                    viewState.showWeatherSummary(it)
                }, {
                    throw RuntimeException(it.message)
                }
            )
        )
    }

    fun setLocation(longitude: Double, latitude: Double) {
        _location.postValue(Pair(first = longitude, second = latitude))
    }

    fun getLocation() {
        viewState.getLocation(location)
    }
}