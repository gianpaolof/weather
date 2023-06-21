package com.example.weather.app.presentation.adapters.viewholders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R

class CityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val cityName: TextView = itemView.findViewById(R.id.cityName)
}