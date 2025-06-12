package com.example.weatherx.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherx.adapter.WeatherWeekAdapter
import com.example.weatherx.databinding.FragmentWeatherWeekBinding
import com.example.weatherx.viewmodel.WeatherWeekViewModel
import com.example.weatherx.model.ForecastDay
import com.example.weatherx.model.WeatherForecast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherWeekFragment : Fragment() {

    private var _binding: FragmentWeatherWeekBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<WeatherWeekViewModel>()
    private lateinit var weatherAdapter: WeatherWeekAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherWeekBinding.inflate(inflater, container, false)
        setupRecyclerView()
        setupObservers()
        fetchWeatherData()
        return binding.root
    }

    private fun setupRecyclerView() {
        weatherAdapter = WeatherWeekAdapter(emptyList<ForecastDay>())
        binding.rvWeekForecast.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = weatherAdapter
        }
    }

    private fun setupObservers() {
        viewModel.weatherForecast.observe(viewLifecycleOwner) { forecast ->
            forecast?.let {
                Log.d("WeatherWeekFragment", "Días recibidos: ${it.forecastDays.size}")
                Log.d("WeatherWeekFragment", "Fechas: ${it.forecastDays.map { day -> day.date }}")
                weatherAdapter.updateData(it.forecastDays)
            } ?: run {
                Log.e("WeatherWeekFragment", "No se recibieron datos del forecast")
            }
        }

        viewModel.loaderState.observe(viewLifecycleOwner) { isLoading ->
            binding.pbLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun fetchWeatherData() {
        val coordinates = "19.32871829633027,-99.16549389549148" // México City coordinates
        Log.d("WeatherWeekFragment", "Obteniendo datos para: $coordinates")
        viewModel.getWeatherWeekDetail(coordinates)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}