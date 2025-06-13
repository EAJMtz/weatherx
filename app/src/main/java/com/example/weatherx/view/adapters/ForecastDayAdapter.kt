package com.example.proyectoshopifyka.view.home.adapters
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyectoshopifyka.databinding.FragmentItemForecastDayBinding
import com.example.proyectoshopifyka.model.ForecastDay
import java.text.SimpleDateFormat
import java.util.*

class ForecastDayAdapter(
    private var forecastList: List<ForecastDay> = emptyList()
) : RecyclerView.Adapter<ForecastDayAdapter.ViewHolder>() {

    fun updateData(newList: List<ForecastDay>) {
        forecastList = newList
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: FragmentItemForecastDayBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(day: ForecastDay) {
            // Convertir fecha a dia >:(
            binding.textDate.text = getDayOfWeek(day.date)
            binding.textMaxTemp.text = "Max: ${day.day.maxtempC}°C"
            binding.textMinTemp.text = "Min: ${day.day.mintempC}°C"

            Glide.with(binding.root)
                .load("https:${day.day.condition.icon}")
                .placeholder(android.R.drawable.ic_menu_report_image)
                .into(binding.imageWeatherIcon)
        }


        private fun getDayOfWeek(dateString: String): String {
            return try {
                val sdfInput = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val date = sdfInput.parse(dateString)
                val sdfOutput = SimpleDateFormat("EEEE", Locale.getDefault())
                sdfOutput.format(date ?: Date())
            } catch (e: Exception) {
                "Día desconocido"
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FragmentItemForecastDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(forecastList[position])
    }

    override fun getItemCount(): Int = forecastList.size
}
