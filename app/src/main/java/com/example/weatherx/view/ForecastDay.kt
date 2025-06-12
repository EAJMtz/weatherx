package com.example.weatherx.model

data class ForecastDay(
    val date: String,
    val maxTemp: Double,
    val minTemp: Double,
    val description: String,
    val icon: String,
    val humidity: Int,
    val windSpeed: Double
)

data class WeatherForecast(
    val forecastDays: List<ForecastDay>
)