package com.example.weather.app.presentation.main

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.app.App
import com.example.weather.app.presentation.adapters.CityAdapter
import com.example.weather.app.presentation.adapters.Weather14dAdapter
import com.example.weather.app.presentation.adapters.Weather24hAdapter
import com.example.weather.app.presentation.city_presenter.CityPresenter
import com.example.weather.app.presentation.permission.LocationPermissionHelper
import com.example.weather.app.presentation.weather_presenter.WeatherPresenter
import com.example.weather.data.WeatherType.*
import com.example.weather.databinding.ActivityMainBinding
import com.example.weather.domain.models.city.CityModel
import com.example.weather.domain.models.todisplay.DisplayWeather14d
import com.example.weather.domain.models.todisplay.DisplayWeather24h
import com.example.weather.domain.models.todisplay.DisplayWeatherNow
import com.example.weather.domain.models.todisplay.Summary
import javax.inject.Inject

class MainActivity : AppCompatActivity(), MainView, LocationListener {

    private lateinit var binding: ActivityMainBinding

    @Inject lateinit var weatherPresenter: WeatherPresenter
    @Inject lateinit var cityPresenter: CityPresenter

    private lateinit var recyclerWeather24h: RecyclerView
    private lateinit var adapterWeather24h: Weather24hAdapter

    private lateinit var recyclerWeather14d: RecyclerView
    private lateinit var adapterWeather14d: Weather14dAdapter

    private lateinit var cityRecycler: RecyclerView
    private lateinit var cityAdapter: CityAdapter

    private lateinit var permissionHelper: LocationPermissionHelper
    private lateinit var locationManager: LocationManager

    private lateinit var latitude: String
    private lateinit var longitude: String

    private val isLocation = this::latitude.isInitialized && this::longitude.isInitialized

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
        initCityRecycler()

        weatherPresenter.attachView(this)
        cityPresenter.attachView(this)

        if (!isLocation) {
            setDefaultWeather()
        } else {
            setLocationWeather(latitude.toDouble(), longitude.toDouble())
        }
    }

    private fun setDefaultWeather() {
        weatherPresenter.getWeatherNow(DEFAULT_LATITUDE, DEFAULT_LONGITUDE)
        weatherPresenter.getWeather24h(DEFAULT_LATITUDE, DEFAULT_LONGITUDE)
        weatherPresenter.getWeather14d(null, DEFAULT_LATITUDE, DEFAULT_LONGITUDE)
    }

    private fun setLocationWeather(latitude: Double, longitude: Double) {
        weatherPresenter.getWeatherNow(latitude, longitude)
        weatherPresenter.getWeather24h(latitude, longitude)
        weatherPresenter.getWeather14d(null, latitude, longitude)
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
                        latitude = p0.latitude.toString()
                        longitude = p0.longitude.toString()
                    }
                }
            }
            if (hasNetwork) {
                locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 120000, 0F
                ) { p0: Location? ->
                    if (p0 != null) {
                        latitude = p0.latitude.toString()
                        longitude = p0.longitude.toString()
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun searchRequest() {
        binding.search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0 != null) {
                    cityPresenter.getCity(p0.toString())
                }
            }

            override fun afterTextChanged(p0: Editable?) {}

        })

        if (binding.search.editableText.isEmpty()) {
            cityAdapter.currentList.clear()
        }
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
            if (isLocation) {
                weatherPresenter.getWeather14d(it.date, latitude.toDouble(), longitude.toDouble())
            } else {
                weatherPresenter.getWeather14d(it.date, DEFAULT_LATITUDE, DEFAULT_LONGITUDE)
            }
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

    override fun showCitiesResult(city: List<CityModel>) {
        cityAdapter.submitList(city)
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

    private fun initCityRecycler() {
        cityRecycler = binding.recyclerCity
        cityRecycler.layoutManager =
            LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
        cityAdapter = CityAdapter()
        cityRecycler.adapter = cityAdapter

        cityRecycler.visibility = if (cityAdapter.currentList.isNotEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }

        cityAdapter.onCityClickListener = {
            latitude = it.lat
            longitude = it.lon

            setLocationWeather(latitude.toDouble(), longitude.toDouble())

            binding.search.clearFocus()
            binding.search.editableText.clear()
        }

        searchRequest()
    }

    override fun onDestroy() {
        super.onDestroy()
        weatherPresenter.detachView()
        cityPresenter.detachView()
    }

    override fun onLocationChanged(p0: Location) {
        latitude = p0.latitude.toString()
        longitude = p0.longitude.toString()
        Log.e("onLocationChanged", p0.toString())
    }

    override fun onProviderEnabled(provider: String) {
        getLocation()
        Log.e("Provider", "Enabled")
    }

    override fun onProviderDisabled(provider: String) {
        Log.e("Provider", "Disabled")
    }

    companion object {
        private const val DEFAULT_LATITUDE = 53.38
        private const val DEFAULT_LONGITUDE = 55.91
        private const val DEFAULT_CITY = "Салават"
    }
}