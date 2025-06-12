package com.example.weatherx.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.weatherx.R
import com.example.weatherx.model.Weather
import com.example.weatherx.utils.FragmentCommunicator
import com.example.weatherx.databinding.FragmentWeatherBinding
import com.example.weatherx.view.HomeActivity
import com.example.weatherx.viewModel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherFragment : Fragment() {

    private var _binding: FragmentWeatherBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<WeatherViewModel>()
    private lateinit var communicator: FragmentCommunicator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        communicator = requireActivity() as HomeActivity
        setupView()
        return binding.root
    }

    private fun setupView() {
        setupObservers()
        val coordinates = getUserCoordinates()
        viewModel.getWeatherDetail(coordinates)
    }

    private fun setupObservers() {
        viewModel.weatherInfo.observe(viewLifecycleOwner) { weather ->
            showWeatherInfo(weather)
        }

        viewModel.loaderState.observe(viewLifecycleOwner) { loaderState ->
            communicator.showLoader(loaderState)
        }
    }

    private fun showWeatherInfo(weather: Weather) {
        binding.tvCity.text = weather.location.name

        // Format time properly
        val timeString = weather.location.localtime.split(" ")[1]
        val greeting = if (weather.current.isDay == 1) {
            getString(R.string.good_morning)
        } else {
            getString(R.string.good_evening)
        }

        val timeWithPeriod = if (weather.current.isDay == 1) {
            getString(R.string.time_am, timeString)
        } else {
            getString(R.string.time_pm, timeString)
        }

        binding.tvDate.text = timeWithPeriod
        binding.tvCelsius.text = getString(R.string.temperature_celsius, weather.current.tempC)
        binding.tvGreeting.text = greeting
        binding.tvSunriseInfo.text = timeWithPeriod

        // Fixed: Use proper string formatting for wind speed
        binding.tvWindInfo.text = getString(R.string.wind_speed_ms, weather.current.windMph.toString())
        binding.tvTempratureInfo.text = getString(R.string.temperature_celsius, weather.current.tempC)

        // Load weather icon - Fixed: Check if icon URL is valid
        val iconUrl = weather.current.condition.icon
        val fullIconUrl = if (iconUrl.startsWith("http")) {
            iconUrl
        } else {
            "https:$iconUrl"
        }

        Glide.with(this)
            .load(fullIconUrl)
            .placeholder(R.drawable.pending_24px)
            .error(android.R.drawable.ic_dialog_alert) // Using system drawable as fallback
            .into(binding.ivWeather)
    }

    private fun getUserCoordinates(): String {
        // Mexico City coordinates (since you're located there)
        return "19.32871829633027, -99.16549389549148"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}