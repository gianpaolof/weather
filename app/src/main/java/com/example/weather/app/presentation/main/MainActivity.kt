package com.example.weather.app.presentation.main

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.weather.R
import com.example.weather.app.App
import com.example.weather.app.presentation.weather_presenter.WeatherPresenter
import com.example.weather.databinding.ActivityMainBinding
import com.example.weather.domain.models.todisplay.DisplayWeather14d
import com.example.weather.domain.models.todisplay.DisplayWeather24h
import com.example.weather.domain.models.todisplay.DisplayWeatherNow
import com.example.weather.domain.models.todisplay.Summary
import javax.inject.Inject

class MainActivity : AppCompatActivity(), MainView {

    private lateinit var binding: ActivityMainBinding

    @Inject lateinit var weatherPresenter: WeatherPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        (applicationContext as App).appComponent.inject(this)

        weatherPresenter.getWeatherNow()
        weatherPresenter.getWeather24h()
        weatherPresenter.getWeather14d(null)
    }

    override fun showProgress() {
        TODO("Not yet implemented")
    }

    override fun hideProgress() {
        TODO("Not yet implemented")
    }

    override fun showWeatherNow(displayWeatherNow: DisplayWeatherNow) {
        binding.maxMin.text = displayWeatherNow.minMaxTemperature
    }

    override fun showWeather24h(displayWeather24h: DisplayWeather24h) {
        Log.e("showWeather24h", "showWeather24h")
    }

    override fun showWeather14d(displayWeather14d: DisplayWeather14d) {
        Log.e("showWeather14d", "showWeather14d")
    }

    override fun showWeatherSummary(summary: Summary) {
        Log.e("showWeatherSummary", "showWeatherSummary")
    }

    override fun onDestroy() {
        super.onDestroy()
        weatherPresenter.unsubscribe()
    }
}