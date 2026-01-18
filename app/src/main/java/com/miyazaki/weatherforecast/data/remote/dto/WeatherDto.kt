package com.miyazaki.weatherforecast.data.remote.dto

import com.squareup.moshi.Json

data class WeatherResponseDto(
    @Json(name = "list") val list: List<ForecastItemDto>,
    @Json(name = "city") val city: CityDto
)

data class CityDto(
    @Json(name = "name") val name: String,
    @Json(name = "coord") val coord: CoordDto? = null,
    @Json(name = "country") val country: String? = null
)

data class CoordDto(
    @Json(name = "lat") val lat: Double,
    @Json(name = "lon") val lon: Double
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