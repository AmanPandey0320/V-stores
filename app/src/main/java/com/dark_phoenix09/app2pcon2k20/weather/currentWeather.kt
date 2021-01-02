package com.dark_phoenix09.app2pcon2k20.weather


import com.google.gson.annotations.SerializedName

data class currentWeather(
    val current: Current,
    val location: Location,
    val request: Request
)