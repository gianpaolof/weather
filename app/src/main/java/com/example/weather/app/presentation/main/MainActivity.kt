package com.example.weather.app.presentation.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.app.App
import com.example.weather.app.presentation.adapters.Weather14dAdapter
import com.example.weather.app.presentation.adapters.Weather24hAdapter
import com.example.weather.app.presentation.weather_presenter.WeatherPresenter
import com.example.weather.data.WeatherType.*
import com.example.weather.databinding.ActivityMainBinding
import com.example.weather.domain.models.todisplay.DisplayWeather14d
import com.example.weather.domain.models.todisplay.DisplayWeather24h
import com.example.weather.domain.models.todisplay.DisplayWeatherNow
import com.example.weather.domain.models.todisplay.Summary
import javax.inject.Inject

class MainActivity : AppCompatActivity(), MainView {

    private lateinit var binding: ActivityMainBinding

    @Inject lateinit var weatherPresenter: WeatherPresenter

    private lateinit var recyclerWeather24h: RecyclerView
    private lateinit var adapterWeather24h: Weather24hAdapter

    private lateinit var recyclerWeather14d: RecyclerView
    private lateinit var adapterWeather14d: Weather14dAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        (application as App).appComponent.inject(this)

        initWeather24hRecycler()
        initWeather14dRecycler()

        weatherPresenter.attachView(this)

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
        binding.mainTemp.text = displayWeatherNow.temperature
        binding.maxMin.text = displayWeatherNow.minMaxTemperature
        binding.precipitationProbability.text = displayWeatherNow.precipitationProbability
        binding.relativeHumidity.text = displayWeatherNow.relativeHumidity
        binding.windSpeed.text = displayWeatherNow.windSpeed

        binding.todayDate.text = displayWeatherNow.todayDate

        val img = when (displayWeatherNow.typeOfWeatherNow) {
            RAIN.type -> R.drawable.ic_rainshower
            SHOWERS.type -> R.drawable.ic_rainythunder
            SNOW.type -> R.drawable.ic_heavysnow
            DAY.type -> R.drawable.ic_day
            NIGHT.type -> R.drawable.ic_night
            else -> throw RuntimeException("Unknown type of weather - ${displayWeatherNow.typeOfWeatherNow}")
        }

        binding.mainImg.setImageResource(img)

        if (displayWeatherNow.isDay) {
            binding.linearLayout.background = ContextCompat.getDrawable(this, R.drawable.ic_background_day)
            binding.mainConstraint.background = ContextCompat.getDrawable(this, R.color.sky)
        } else {
            binding.linearLayout.background = ContextCompat.getDrawable(this, R.drawable.ic_background_night)
            binding.mainConstraint.background = ContextCompat.getDrawable(this, R.color.blue)
        }
    }

    override fun showWeather24h(displayWeather24h: DisplayWeather24h, listDisplayWeather24h: List<DisplayWeather24h>) {
        adapterWeather24h.submitList(listDisplayWeather24h)
    }

    override fun showWeather14d(displayWeather14d: DisplayWeather14d, listDisplayWeather14d: List<DisplayWeather14d>) {
        adapterWeather14d.submitList(listDisplayWeather14d)
        adapterWeather14d.onWeatherClickListener = {
            weatherPresenter.getWeather14d(it.date)
        }
    }

    override fun showWeatherSummary(summary: Summary) {
        with(binding) {
            summaryDate.text = summary.date
            summarySunriseValue.text = summary.sunrise
            summarySunsetValue.text = summary.sunset
            summaryTemperatureValue.text = summary.min_max_temperature
            summaryPrecipitationProbabilityValue.text = summary.drop
            summaryWindValue.text = summary.wind
            summaryApparentTempValue.text = summary.apparent_min_max_temperature
        }
    }

    private fun initWeather24hRecycler() {
        recyclerWeather24h = binding.todayRecycler
        recyclerWeather24h.layoutManager =
            LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
        adapterWeather24h = Weather24hAdapter()
        recyclerWeather24h.adapter = adapterWeather24h
    }

    private fun initWeather14dRecycler() {
        recyclerWeather14d = binding.recyclerFor14Days
        recyclerWeather14d.layoutManager =
            LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
        adapterWeather14d = Weather14dAdapter()
        recyclerWeather14d.adapter = adapterWeather14d

        recyclerWeather14d.recycledViewPool
            .setMaxRecycledViews(
                Weather14dAdapter.UNCHECKED_VIEW_TYPE,
                Weather14dAdapter.MAX_POOL_SIZE_UNCHECKED
            )

        recyclerWeather14d.recycledViewPool
            .setMaxRecycledViews(
                Weather14dAdapter.CHECKED_VIEW_TYPE,
                Weather14dAdapter.MAX_POOL_SIZE_CHECKED
            )
    }

    override fun onDestroy() {
        super.onDestroy()
        weatherPresenter.detachView()
    }
}