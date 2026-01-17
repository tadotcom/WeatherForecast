package com.miyazaki.weatherforecast.domain.model

/**
 * 天気情報のドメインモデル
 */
data class WeatherInfo(
    val dateTime: String,
    val temperature: Double,
    val description: String,
    val icon: String,
    val iconUrl: String
)