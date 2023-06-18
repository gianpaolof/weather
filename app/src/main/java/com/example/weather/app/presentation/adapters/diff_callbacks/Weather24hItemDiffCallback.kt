package com.example.weather.app.presentation.adapters.diff_callbacks

import androidx.recyclerview.widget.DiffUtil
import com.example.weather.domain.models.todisplay.DisplayWeather24h

class Weather24hItemDiffCallback: DiffUtil.ItemCallback<DisplayWeather24h>() {
    override fun areItemsTheSame(
        oldItem: DisplayWeather24h,
        newItem: DisplayWeather24h
    ): Boolean = oldItem.time == newItem.time

    override fun areContentsTheSame(
        oldItem: DisplayWeather24h,
        newItem: DisplayWeather24h
    ): Boolean = oldItem == newItem
}