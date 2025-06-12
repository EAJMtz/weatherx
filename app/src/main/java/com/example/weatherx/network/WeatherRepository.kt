package com.example.weatherx.network

import android.util.Log
import com.example.weatherx.core.RetrofitInstance
import com.example.weatherx.core.WeatherAPI
import com.example.weatherx.model.Weather
import jakarta.inject.Inject

class WeatherRepository @Inject constructor(
    private val weatherAPI: WeatherAPI
) {

    suspend fun getWeatherDetail(coordinates: String): Weather? {
        val response = weatherAPI.getWeatherForecast("dc798fb4f525444bbad62116251904", coordinates)

        if (!response.isSuccessful) {
            Log.e("API_ERROR", "Request failed with code: ${response.code()}")
            return null
        }

        val weatherResponse = response.body()
        if (weatherResponse == null || weatherResponse.forecast == null) {
            Log.e("API_ERROR", "Response body or forecast is null")
            return null
        }

        Log.d("API_DEBUG", "Formato de fecha recibido: ${weatherResponse.forecast.forecastDays.map { it.date }}")

        return weatherResponse
    }
}
