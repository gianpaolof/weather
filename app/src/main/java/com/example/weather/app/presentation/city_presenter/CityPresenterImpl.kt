package com.example.weather.app.presentation.city_presenter

import com.example.weather.app.presentation.main.MainView
import com.example.weather.app.schedulers.SchedulerProvider
import com.example.weather.domain.usecase.city.GetCityUseCase
import io.reactivex.disposables.CompositeDisposable
import moxy.InjectViewState
import moxy.MvpPresenter
import javax.inject.Inject

@InjectViewState
class CityPresenterImpl @Inject constructor(
    private val getCityUseCase: GetCityUseCase,
    private val schedulers : SchedulerProvider
): MvpPresenter<MainView>() {

    private val disposables: CompositeDisposable = CompositeDisposable()

    override fun detachView(view: MainView?) {
        disposables.dispose()
    }

    fun getCity(city: String) {
        disposables.add(getCityUseCase.getCity(city)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
            .subscribe(
                {
                    viewState.showCitiesResult(it.take(5))
                }, {
                    throw RuntimeException(it.message)
                }
            )

        )
    }
}