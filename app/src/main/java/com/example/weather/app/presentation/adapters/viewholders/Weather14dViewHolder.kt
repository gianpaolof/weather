package com.example.weather.app.presentation.adapters.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R

class Weather14dViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val dayOfWeek: TextView = itemView.findViewById(R.id.dayOfTheWeek)
    val date: TextView = itemView.findViewById(R.id.date)
    val temperature: TextView = itemView.findViewById(R.id.tempMaxMin)
    val img: ImageView = itemView.findViewById(R.id.img)
}