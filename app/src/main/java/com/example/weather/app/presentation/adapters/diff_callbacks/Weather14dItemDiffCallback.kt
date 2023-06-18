package com.example.weather.app.presentation.adapters.diff_callbacks

import androidx.recyclerview.widget.DiffUtil
import com.example.weather.domain.models.todisplay.DisplayWeather14d

class Weather14dItemDiffCallback: DiffUtil.ItemCallback<DisplayWeather14d>() {
    override fun areItemsTheSame(
        oldItem: DisplayWeather14d,
        newItem: DisplayWeather14d
    ): Boolean = oldItem.date == newItem.date

    override fun areContentsTheSame(
        oldItem: DisplayWeather14d,
        newItem: DisplayWeather14d
    ): Boolean = oldItem == newItem
}