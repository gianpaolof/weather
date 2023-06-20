package com.example.weather.app.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.weather.R
import com.example.weather.app.presentation.adapters.diff_callbacks.Weather14dItemDiffCallback
import com.example.weather.app.presentation.adapters.viewholders.Weather14dViewHolder
import com.example.weather.data.WeatherType.*
import com.example.weather.domain.models.todisplay.DisplayWeather14d

class Weather14dAdapter :
    ListAdapter<DisplayWeather14d, Weather14dViewHolder>(Weather14dItemDiffCallback()) {

    var onWeatherClickListener: ((DisplayWeather14d) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Weather14dViewHolder {
        val layout = when (viewType) {
            UNCHECKED_VIEW_TYPE -> R.layout.weather_14d_unchecked_item
            CHECKED_VIEW_TYPE -> R.layout.weather_14d_checked_item
            else -> throw RuntimeException("Unknown view type $viewType")
        }
        val itemView = LayoutInflater.from(parent.context).inflate(
            layout,
            parent,
            false
        )
        return Weather14dViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: Weather14dViewHolder, position: Int) {
        val currentItem = getItem(position)

        val minTemperature = "${currentItem.maxTemperature}°/"
        val maxTemperature = "${currentItem.minTemperature}°"

        val temperature = minTemperature + maxTemperature

        holder.date.text = currentItem.date
        holder.dayOfWeek.text = currentItem.dayOfWeek
        holder.temperature.text = temperature

        holder.itemView.setOnClickListener {
            onWeatherClickListener?.invoke(currentItem)
        }

        val img = when (currentItem.typeOfWeather) {
            RAIN.type -> R.drawable.ic_rainshower
            SHOWERS.type -> R.drawable.ic_rainythunder
            SNOW.type -> R.drawable.ic_heavysnow
            DAY.type -> R.drawable.ic_day
            NIGHT.type -> R.drawable.ic_night
            else -> throw RuntimeException("Unknown type of weather - ${currentItem.typeOfWeather}")
        }

        holder.img.setImageResource(img)
    }

    override fun getItemViewType(position: Int): Int {
        val currentItem = getItem(position)

        return if (currentItem.isPicked == null || currentItem.isPicked == false) {
            UNCHECKED_VIEW_TYPE
        } else {
            CHECKED_VIEW_TYPE
        }
    }

    companion object {
        const val UNCHECKED_VIEW_TYPE = 0
        const val CHECKED_VIEW_TYPE = 1
        const val MAX_POOL_SIZE_UNCHECKED = 5
        const val MAX_POOL_SIZE_CHECKED = 1
    }
}