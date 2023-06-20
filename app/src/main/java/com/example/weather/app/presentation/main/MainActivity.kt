package com.example.weather.app.presentation.main

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.app.App
import com.example.weather.app.presentation.adapters.Weather14dAdapter
import com.example.weather.app.presentation.adapters.Weather24hAdapter
import com.example.weather.app.presentation.permission.LocationPermissionHelper
import com.example.weather.app.presentation.weather_presenter.WeatherPresenter
import com.example.weather.data.WeatherType.*
import com.example.weather.databinding.ActivityMainBinding
import com.example.weather.domain.models.todisplay.DisplayWeather14d
import com.example.weather.domain.models.todisplay.DisplayWeather24h
import com.example.weather.domain.models.todisplay.DisplayWeatherNow
import com.example.weather.domain.models.todisplay.Summary
import javax.inject.Inject

class MainActivity : AppCompatActivity(), MainView, LocationListener {

    private lateinit var binding: ActivityMainBinding

    @Inject lateinit var weatherPresenter: WeatherPresenter

    private lateinit var recyclerWeather24h: RecyclerView
    private lateinit var adapterWeather24h: Weather24hAdapter

    private lateinit var recyclerWeather14d: RecyclerView
    private lateinit var adapterWeather14d: Weather14dAdapter

    private lateinit var permissionHelper: LocationPermissionHelper
    private lateinit var locationManager: LocationManager

    private var latitude: Double? = null
    private var longitude: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        (application as App).appComponent.inject(this)

        permissionHelper = LocationPermissionHelper(this)
        permissionHelper.checkLocationPermission()

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0.5f, this)

        initWeather24hRecycler()
        initWeather14dRecycler()

        weatherPresenter.attachView(this)

        weatherPresenter.getWeatherNow()
        weatherPresenter.getWeather24h()
        weatherPresenter.getWeather14d(null)
    }

    @SuppressLint("MissingPermission")
    fun getLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if (hasGps || hasNetwork) {
            if (hasGps) {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 120000, 0F
                ) { p0: Location? ->
                    if (p0 != null) {
                        latitude = p0.latitude
                        longitude = p0.longitude
                    }
                }
            }
            if (hasNetwork) {
                locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 120000, 0F
                ) { p0: Location? ->
                    if (p0 != null) {
                        latitude = p0.latitude
                        longitude = p0.longitude
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun showProgress() {
        TODO("Not yet implemented")
    }

    override fun hideProgress() {
        TODO("Not yet implemented")
    }

    override fun showWeatherNow(displayWeatherNow: DisplayWeatherNow) {
        binding.mainTemp.text = displayWeatherNow.temperature
        binding.maxMin.text = displayWeatherNow.maxMinTemperature
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
            summaryTemperatureValue.text = summary.max_min_temperature
            summaryPrecipitationProbabilityValue.text = summary.drop
            summaryWindValue.text = summary.wind
            summaryApparentTempValue.text = summary.apparent_max_min_temperature
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

    override fun onLocationChanged(p0: Location) {
        latitude = p0.latitude
        longitude = p0.longitude
        Log.e("onLocationChanged", p0.toString())
    }

    override fun onProviderEnabled(provider: String) {
        getLocation()
        Log.e("Provider", "Enabled")
    }

    override fun onProviderDisabled(provider: String) {
        Log.e("Provider", "Disabled")
    }
}