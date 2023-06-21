package com.example.weather.app.presentation.city_presenter

import com.example.weather.app.presentation.main.MainView
import com.example.weather.app.schedulers.SchedulerProvider
import com.example.weather.domain.usecase.city.GetCityUseCase
import io.reactivex.disposables.CompositeDisposable

class CityPresenterImpl(
    private val getCityUseCase: GetCityUseCase,
    private val schedulers : SchedulerProvider
): CityPresenter {

    private val disposables: CompositeDisposable = CompositeDisposable()

    private var view: MainView? = null

    override fun attachView(view: MainView) {
        this.view = view
    }

    override fun detachView() {
        this.view = null
        disposables.dispose()
    }

    override fun getCity(city: String) {
        disposables.add(getCityUseCase.getCity(city)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
            .subscribe(
                {
                    view?.showCitiesResult(it.take(5))
                }, {
                    throw RuntimeException(it.message)
                }
            )

        )
    }
}