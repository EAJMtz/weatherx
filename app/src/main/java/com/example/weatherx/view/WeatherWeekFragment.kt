package com.example.weatherx.view

import com.example.weatherx.viewmodel.WeatherWeekViewModel
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherx.view.adapters.WeatherWeekAdapter
import com.example.weatherx.databinding.FragmentWeatherWeekBinding
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherWeekFragment : Fragment() {

    private var _binding: FragmentWeatherWeekBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<WeatherWeekViewModel>()

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
        binding.rvWeekForecast.layoutManager = LinearLayoutManager(requireContext())
        binding.rvWeekForecast.adapter = WeatherWeekAdapter(emptyList())
    }

    private fun setupObservers() {
        viewModel.weatherForecast.observe(viewLifecycleOwner) { forecast ->
            forecast?.let {
                Log.d("FragmentDebug", "Días pasados al adapter: ${it.forecastDays.map { day -> day.date }}")
                (binding.rvWeekForecast.adapter as? WeatherWeekAdapter)?.updateData(it.forecastDays)
            } ?: Log.e("FragmentDebug", "El ViewModel no está enviando datos")
        }

        viewModel.loaderState.observe(viewLifecycleOwner) { isLoading ->
            binding.pbLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun fetchWeatherData() {
        val coordinates = "19.32871829633027,-99.16549389549148"
        viewModel.getWeatherWeekDetail(coordinates)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}