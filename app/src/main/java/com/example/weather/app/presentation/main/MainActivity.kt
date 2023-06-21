package com.example.weather.app.presentation.main

import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
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
import java.util.*
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        (application as App).appComponent.inject(this)

        permissionHelper = LocationPermissionHelper(this)
        permissionHelper.checkLocationPermission()

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0.5f, this)
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 0.5f, this)

        initWeather24hRecycler()
        initWeather14dRecycler()
        initCityRecycler()

        weatherPresenter.attachView(this)
        cityPresenter.attachView(this)
        weatherPresenter.getLocation()
    }

    private fun setDefaultWeather() {
        weatherPresenter.getWeatherNow(DEFAULT_LATITUDE, DEFAULT_LONGITUDE)
        weatherPresenter.getWeather24h(DEFAULT_LATITUDE, DEFAULT_LONGITUDE)
        weatherPresenter.getWeather14d(null, DEFAULT_LATITUDE, DEFAULT_LONGITUDE)
        binding.searchLayout.hint = resources.getString(R.string.default_city)
    }

    private fun setLocationWeather(latitude: Double, longitude: Double, city: String) {
        weatherPresenter.getWeatherNow(latitude, longitude)
        weatherPresenter.getWeather24h(latitude, longitude)
        weatherPresenter.getWeather14d(null, latitude, longitude)
        binding.searchLayout.hint = city
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
        binding.progress.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        binding.progress.visibility = View.GONE
    }

    override fun showWeatherNow(displayWeatherNow: DisplayWeatherNow) {
        val img = when (displayWeatherNow.typeOfWeatherNow) {
            RAIN.type -> R.drawable.ic_rainshower
            SHOWERS.type -> R.drawable.ic_rainythunder
            SNOW.type -> R.drawable.ic_heavysnow
            DAY.type -> R.drawable.ic_sun_3d
            NIGHT.type -> R.drawable.ic_moon_3d
            else -> throw RuntimeException("Unknown type of weather - ${displayWeatherNow.typeOfWeatherNow}")
        }

        with(binding) {
            mainTemp.text = displayWeatherNow.temperature
            maxMin.text = displayWeatherNow.maxMinTemperature
            precipitationProbability.text = displayWeatherNow.precipitationProbability
            relativeHumidity.text = displayWeatherNow.relativeHumidity
            windSpeed.text = displayWeatherNow.windSpeed
            todayDate.text = displayWeatherNow.todayDate
            mainImg.setImageResource(img)

            if (displayWeatherNow.isDay) {
                linearLayout.background =
                    ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_background_day)
                mainConstraint.background = ContextCompat.getDrawable(this@MainActivity, R.color.sky)
            } else {
                linearLayout.background =
                    ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_background_night)
                mainConstraint.background = ContextCompat.getDrawable(this@MainActivity, R.color.blue)
            }
        }

    }

    override fun showWeather24h(
        displayWeather24h: DisplayWeather24h,
        listDisplayWeather24h: List<DisplayWeather24h>
    ) {
        adapterWeather24h.submitList(listDisplayWeather24h)
    }

    override fun showWeather14d(
        displayWeather14d: DisplayWeather14d,
        listDisplayWeather14d: List<DisplayWeather14d>
    ) {
        adapterWeather14d.submitList(listDisplayWeather14d)
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

    override fun showCitiesResult(city: List<CityModel>) { cityAdapter.submitList(city) }

    override fun getLocation(location: LiveData<Pair<Double, Double>>) {
        if (location != null) {
            setDefaultWeather()
            adapterWeather14d.onWeatherClickListener = {
                weatherPresenter.getWeather14d(it.date, DEFAULT_LATITUDE, DEFAULT_LONGITUDE)
            }
        }
        location.observe(this) { pair ->
            val latitude = pair.first
            val longitude = pair.second
            val geocoder = Geocoder(this, Locale.getDefault())
            val result = geocoder.getFromLocation(latitude, longitude, 1)[0]
            setLocationWeather(latitude, longitude, result.locality)
            adapterWeather14d.onWeatherClickListener = {
                weatherPresenter.getWeather14d(it.date, latitude, longitude)
            }
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
            weatherPresenter.setLocation(it.lat.toDouble(), it.lon.toDouble())

            binding.search.clearFocus()
            binding.search.editableText.clear()
        }

        searchRequest()
    }

    override fun onDestroy() {
        super.onDestroy()
        weatherPresenter.detachView()
        cityPresenter.detachView()
        locationManager.removeUpdates(this)
    }

    override fun onLocationChanged(p0: Location) {
        weatherPresenter.setLocation(p0.latitude, p0.longitude)
    }

    override fun onProviderEnabled(provider: String) {}

    override fun onProviderDisabled(provider: String) {}

    companion object {
        private const val DEFAULT_LATITUDE = 53.22
        private const val DEFAULT_LONGITUDE = 55.56
    }
}