package com.example.proyectoshopifyka.model

import com.google.gson.annotations.SerializedName


data class Weather(
    @SerializedName("location") val location: Location,
    @SerializedName("current") val current: Current
)


data class Location(
    @SerializedName("name") val name: String,
    @SerializedName("localtime") val localtime: String
)

data class Current(
    @SerializedName("last_updated") val lastupdated: String,
    @SerializedName("temp_c") val tempc: Float,
    @SerializedName("feelslike_c") val feelslikec: Float,
    @SerializedName("condition") val condition: Condition,
    @SerializedName("wind_kph") val windkph: Float,
    @SerializedName("humidity") val humidity: Int
)

data class Condition(
    @SerializedName("text") val text: String,
    @SerializedName("icon") val icon: String
)

