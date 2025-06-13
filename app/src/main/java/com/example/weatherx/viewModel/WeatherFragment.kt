package com.example.proyectoshopifyka.view.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.proyectoshopifyka.databinding.FragmentWeatherBinding
import com.example.proyectoshopifyka.model.Weather
import com.example.proyectoshopifyka.view.home.viewModel.WeatherFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

import android. widget. Toast
import androidx.lifecycle.LiveData
import androidx. recyclerview. widget. LinearLayoutManager
import androidx. lifecycle. MutableLiveData
import com. example. proyectoshopifyka. model. ForecastResponse
import com. example. proyectoshopifyka. view. home. adapters. ForecastDayAdapter
import java.time.LocalTime

@AndroidEntryPoint
class WeatherFragment : Fragment() {

    private val viewModel: WeatherFragmentViewModel by viewModels() // ✅ Corrección

    private var _binding: FragmentWeatherBinding? = null
    private val binding get() = _binding!!


    private lateinit var forecastAdapter: ForecastDayAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializar adapter con lista vacía
        forecastAdapter = ForecastDayAdapter(emptyList())
        binding.recyclerViewWeather.apply {
            adapter = forecastAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        viewModel.fetchForecast("dee6bfa4fe7f459f97e15507252005")


        viewModel.forecastInfo.observe(viewLifecycleOwner) { forecastResponse ->
            val today = forecastResponse.current

            binding.textTemp.text = "${today.tempc} °C"
            binding.textNumWind.text = "${today.windkph} km/h"
            binding.textNumTemperatura.text = "${today.feelslikec}°C"
            Glide.with(this)
                .load("https:${today.condition.icon}")
                .into(binding.imgClima)

            binding.textSaludo.text = "${getGreeting()} WASIM"
            binding.textSemana.text = formatLastUpdated(today.lastupdated)

            //Log.d("Fragment", "Lista de días antes de actualizar el RecyclerView: ${forecastResponse.forecast.forecastday}")
            forecastAdapter.updateData(forecastResponse.forecast.forecastday)

        }
    }



    private fun getGreeting(): String {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        return when (hour) {
            in 0..11 -> "BUENOS DÍAS"
            in 12..17 -> "BUENAS TARDES"
            else -> "BUENAS TARDES"
        }
    }

    private fun formatLastUpdated(raw: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val outputFormat = SimpleDateFormat("EEEE, hh:mm a", Locale.getDefault())
        val date = inputFormat.parse(raw)
        return date?.let { outputFormat.format(it).uppercase(Locale.getDefault()) } ?: raw
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
