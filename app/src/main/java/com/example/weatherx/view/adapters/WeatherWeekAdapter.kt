package com.example.weatherx.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherx.databinding.ItemWeatherWeekBinding
import com.example.weatherx.model.Weather.ForecastDay
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

class WeatherWeekAdapter(private var forecastList: List<ForecastDay>) :
    RecyclerView.Adapter<WeatherWeekAdapter.WeatherWeekViewHolder>() {

    inner class WeatherWeekViewHolder(private val binding: ItemWeatherWeekBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(forecast: ForecastDay) {
            // Format date to be more readable
            val formattedDate = formatDate(forecast.date)
            binding.tvDateWeek.text = formattedDate

            binding.tvWeatherConditionWeek.text = forecast.dayInfo.condition.text
            binding.tvCelsiusWeek.text = "${forecast.dayInfo.avgTempC}°C"
            binding.tvWindInfoWeek.text = "Wind: ${forecast.dayInfo.windSpeed} km/h"
            binding.tvRainProbabilityWeek.text = "Rain probability: ${forecast.dayInfo.rainChance}%"

            // Load weather icon with Glide
            Glide.with(binding.root.context)
                .load("https:" + forecast.dayInfo.condition.icon)
                .into(binding.ivWeatherIconWeek)
        }

        private fun formatDate(dateString: String): String {
            return try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val outputFormat = SimpleDateFormat("EEEE, MMMM dd", Locale.getDefault())
                val date = inputFormat.parse(dateString)
                date?.let { outputFormat.format(it) } ?: dateString
            } catch (e: Exception) {
                Log.e("WeatherAdapter", "Error formatting date: ${e.message}")
                dateString
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherWeekViewHolder {
        val binding =
            ItemWeatherWeekBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherWeekViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherWeekViewHolder, position: Int) {
        holder.bind(forecastList[position])
    }

    override fun getItemCount(): Int = forecastList.size

    // Método para actualizar la lista de datos
    fun updateData(newList: List<ForecastDay>) {
        forecastList = newList
        notifyDataSetChanged() // ✅ Refresca toda la lista
    }
}