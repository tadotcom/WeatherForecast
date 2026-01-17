package com.miyazaki.weatherforecast.data.mapper

import com.miyazaki.weatherforecast.data.remote.dto.ForecastItemDto
import com.miyazaki.weatherforecast.domain.model.WeatherInfo
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

private val dateFormatter = DateTimeFormatter.ofPattern("M月d日(E) H:mm", Locale.JAPAN)

fun ForecastItemDto.toDomain(): WeatherInfo {
    val zoneId = ZoneId.of("Asia/Tokyo")
    val zonedDateTime = Instant.ofEpochSecond(this.dt).atZone(zoneId)

    return WeatherInfo(
        dateTime = zonedDateTime.format(dateFormatter),
        temperature = this.main.temp,
        description = this.weather.firstOrNull()?.description ?: "",
        icon = this.weather.firstOrNull()?.icon ?: "",
        iconUrl = "https://openweathermap.org/img/wn/${this.weather.firstOrNull()?.icon}@2x.png"
    )
}