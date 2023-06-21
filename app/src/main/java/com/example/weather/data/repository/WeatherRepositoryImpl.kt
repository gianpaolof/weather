package com.example.weather.data.repository

import com.example.weather.app.schedulers.SchedulerProvider
import com.example.weather.data.api.weather.WeatherInstance
import com.example.weather.data.mappers.WeatherMapper
import com.example.weather.domain.models.todisplay.DisplayWeather14d
import com.example.weather.domain.models.todisplay.DisplayWeather24h
import com.example.weather.domain.models.todisplay.DisplayWeatherNow
import com.example.weather.domain.models.todisplay.Summary
import com.example.weather.domain.repository.WeatherRepository
import io.reactivex.Single
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val mapper: WeatherMapper,
    private val schedulers: SchedulerProvider
) : WeatherRepository {

    override fun getWeatherNow(latitude: Double, longitude: Double): Single<DisplayWeatherNow> {
        return WeatherInstance.api.getWeatherNow(latitude, longitude)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.io())
            .map { weatherNow ->
                mapper.mapApiToDisplay(weatherNow)
            }
            .onErrorReturn {
                throw RuntimeException(it.message)
            }
    }

    override fun getWeather24h(latitude: Double, longitude: Double): Single<Pair<DisplayWeather24h, List<DisplayWeather24h>>> {
        return WeatherInstance.api.getWeather24h(latitude, longitude)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.io())
            .map { weather24h ->
                mapper.mapApiToDisplay(weather24h)
            }
            .onErrorReturn {
                throw RuntimeException(it.message)
            }
    }

    override fun getWeather14d(pickedDate: String?, latitude: Double, longitude: Double):
            Pair<Single<Pair<DisplayWeather14d, List<DisplayWeather14d>>>, Single<Summary>> {

        return Pair(
            first = WeatherInstance.api.getWeather14d(latitude, longitude)
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.io())
                .map { weather14d ->
                    mapper.mapApiToDisplay(
                        weather14d = weather14d,
                        pickedDate = pickedDate
                    ).first
                }
                .onErrorReturn {
                    throw RuntimeException(it.message)
                },
            second = WeatherInstance.api.getWeather14d(latitude, longitude)
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.io())
                .map { weather14d ->
                    mapper.mapApiToDisplay(
                        weather14d = weather14d,
                        pickedDate = pickedDate
                    ).second
                }
                .onErrorReturn {
                    throw RuntimeException(it.message)
                }
        )
    }
}