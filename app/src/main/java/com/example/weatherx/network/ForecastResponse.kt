package com.example.proyectoshopifyka.model

import com.google.gson.annotations.SerializedName

data class ForecastResponse(
    @SerializedName("location") val location: Location,
    @SerializedName("current") val current: Current,
    @SerializedName("forecast") val forecast: Forecast
)

data class Forecast(
    @SerializedName("forecastday") val forecastday: List<ForecastDay>
)

data class ForecastDay(
    @SerializedName("date") val date: String,
    @SerializedName("day") val day: Day
)

data class Day(
    @SerializedName("maxtemp_c") val maxtempC: Float,
    @SerializedName("mintemp_c") val mintempC: Float,
    @SerializedName("condition") val condition: Condition
)
