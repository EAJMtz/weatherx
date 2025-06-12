package com.example.weatherx.model.weather

data class Forecast(
    val forecastDays: List<ForecastDay>
)

data class ForecastDay(
    val date: String,
    val dayInfo: DayInfo
)

data class DayInfo(
    val avgTempC: Double,
    val condition: WeatherCondition,
    val windSpeed: Double,
    val rainChance: Int
)

data class WeatherCondition(
    val text: String,
    val icon: String
)