package com.example.weather.data.repository

import android.content.Context
import com.example.weather.data.api.weather.WeatherInstance
import com.example.weather.data.mappers.WeatherMapper
import com.example.weather.domain.models.todisplay.DisplayWeather14d
import com.example.weather.domain.models.todisplay.DisplayWeather24h
import com.example.weather.domain.models.todisplay.DisplayWeatherNow
import com.example.weather.domain.models.todisplay.Summary
import com.example.weather.domain.repository.WeatherRepository
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(private val context: Context) : WeatherRepository {

    private val mapper = WeatherMapper()

    override fun getWeatherNow(): Single<DisplayWeatherNow> {
        return WeatherInstance.api.getWeatherNow()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.single())
            .map { weatherNow ->
                mapper.mapApiToDisplay(weatherNow)
            }
            .onErrorReturn {
                throw RuntimeException(it.message)
            }
    }

    override fun getWeather24h(): Single<DisplayWeather24h> {
        return WeatherInstance.api.getWeather24h()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.single())
            .map { weather24h ->
                mapper.mapApiToDisplay(weather24h)
            }
            .onErrorReturn {
                throw RuntimeException(it.message)
            }
    }

    override fun getWeather14d(pickedDate: String?): Pair<Single<DisplayWeather14d>, Single<Summary>> {

        return Pair(
            first = WeatherInstance.api.getWeather14d()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.single())
                .map { weather14d ->
                    mapper.mapApiToDisplay(weather14d = weather14d, pickedDate = pickedDate, context = context).first
                }
                .onErrorReturn {
                    throw RuntimeException(it.message)
                },
            second = WeatherInstance.api.getWeather14d()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.single())
                .map { weather14d ->
                    mapper.mapApiToDisplay(weather14d = weather14d, pickedDate = pickedDate, context = context).second
                }
                .onErrorReturn {
                    throw RuntimeException(it.message)
                }
        )
    }
}