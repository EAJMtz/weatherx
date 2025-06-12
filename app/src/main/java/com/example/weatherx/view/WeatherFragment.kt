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
import com.example.weatherx.viewModel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherFragment : Fragment() {

    private var _binding: FragmentWeatherBinding? = null

    private val binding get() = _binding!!
    private val viewModel by viewModels<WeatherViewModel>()
    private lateinit var communicator: FragmentCommunicator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)

        // Determine which activity is hosting this fragment
        communicator = when (requireActivity()) {
            is HomeActivity -> requireActivity() as HomeActivity
            is OnboardingActivity -> requireActivity() as OnboardingActivity
            else -> throw IllegalStateException("Fragment must be hosted by HomeActivity or OnboardingActivity")
        }

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
        binding.tvDate.text = if (weather.current.isDay == 1)
            "${weather.location.localtime.split(" ")[1]} a.m"
        else
            "${weather.location.localtime.split(" ")[1]} p.m"
        binding.tvCelsius.text = "${weather.current.tempC} °C"
        binding.tvGreeting.text = if (weather.current.isDay == 1)
            "Good morning"
        else
            "Good night"
        binding.tvSunriseInfo.text = if (weather.current.isDay == 1)
            "${weather.location.localtime.split(" ")[1]} a.m"
        else
            "${weather.location.localtime.split(" ")[1]} p.m"
        binding.tvWindInfo.text = "${weather.current.windMph} m/s"
        binding.tvTempratureInfo.text = "${weather.current.tempC} °C"

        Glide.with(this)
            .load("https:${weather.current.condition.icon}")
            .placeholder(R.drawable.pending_24px)
            .error(android.R.drawable.ic_dialog_alert)
            .into(binding.ivWeather)
    }

    private fun getUserCoordinates(): String {
        return "19.32871829633027, -99.16549389549148"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}