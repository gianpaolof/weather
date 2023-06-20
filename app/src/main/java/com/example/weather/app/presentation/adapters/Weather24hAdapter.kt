package com.example.weather.app.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.weather.R
import com.example.weather.app.presentation.adapters.diff_callbacks.Weather24hItemDiffCallback
import com.example.weather.app.presentation.adapters.viewholders.Weather24hViewHolder
import com.example.weather.domain.models.todisplay.DisplayWeather24h
import com.example.weather.data.WeatherType.*

class Weather24hAdapter :
    ListAdapter<DisplayWeather24h, Weather24hViewHolder>(Weather24hItemDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Weather24hViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.weather_24h_item,
            parent,
            false
        )

        return Weather24hViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: Weather24hViewHolder, position: Int) {
        val currentItem = getItem(position)

        val temperature = "${currentItem.temperature}°"
        val time = currentItem.time

        holder.time.text = time
        holder.temp.text = temperature

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
}