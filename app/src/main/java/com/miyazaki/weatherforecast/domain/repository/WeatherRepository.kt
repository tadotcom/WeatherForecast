package com.miyazaki.weatherforecast.domain.repository

import com.miyazaki.weatherforecast.domain.model.WeatherInfo
import kotlinx.coroutines.flow.Flow

/**
 * 天気情報リポジトリ
 */
interface WeatherRepository {

    fun getWeatherByCity(city: String): Flow<Result<List<WeatherInfo>>>
}