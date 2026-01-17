package com.miyazaki.weatherforecast.data.remote.dto

import com.squareup.moshi.Json

data class WeatherResponseDto(
    @Json(name = "list") val list: List<ForecastItemDto>
)

data class ForecastItemDto(
    @Json(name = "dt") val dt: Long,
    @Json(name = "main") val main: MainDto,
    @Json(name = "weather") val weather: List<WeatherDto>
)

data class MainDto(
    @Json(name = "temp") val temp: Double
)

data class WeatherDto(
    @Json(name = "description") val description: String,
    @Json(name = "icon") val icon: String
)