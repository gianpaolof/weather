package com.example.weather.app.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.weather.R
import com.example.weather.app.presentation.adapters.diff_callbacks.CityItemDiffCallback
import com.example.weather.app.presentation.adapters.viewholders.CityViewHolder
import com.example.weather.domain.models.city.CityModel

class CityAdapter : ListAdapter<CityModel, CityViewHolder>(CityItemDiffCallback()) {

    var onCityClickListener: ((CityModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.city_item,
            parent,
            false
        )
        return CityViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.cityName.text = currentItem.display_name

        holder.itemView.setOnClickListener {
            onCityClickListener?.invoke(currentItem)
        }
    }
}