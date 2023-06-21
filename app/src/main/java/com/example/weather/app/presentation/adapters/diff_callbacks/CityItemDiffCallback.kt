package com.example.weather.app.presentation.adapters.diff_callbacks

import androidx.recyclerview.widget.DiffUtil
import com.example.weather.domain.models.city.CityModel

class CityItemDiffCallback: DiffUtil.ItemCallback<CityModel>() {

    override fun areItemsTheSame(
        oldItem: CityModel,
        newItem: CityModel
    ): Boolean = oldItem.place_id == newItem.place_id

    override fun areContentsTheSame(
        oldItem: CityModel,
        newItem: CityModel
    ): Boolean = oldItem == newItem

}