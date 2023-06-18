package com.example.weather.app.presentation.adapters.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R

class Weather24hViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val time: TextView = itemView.findViewById(R.id.todayTime)
    val temp: TextView = itemView.findViewById(R.id.todayTemp)
    val img: ImageView = itemView.findViewById(R.id.todayImg)
}