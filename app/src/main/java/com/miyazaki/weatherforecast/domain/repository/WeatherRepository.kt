package com.miyazaki.weatherforecast.domain.repository

import com.miyazaki.weatherforecast.domain.model.WeatherInfo
import kotlinx.coroutines.flow.Flow

/**
 * 天気情報を取得するリポジトリ
 *
 * OpenWeatherMap APIを使用して5日間の天気予報を取得
 */
interface WeatherRepository {

    /** 都市名から天気情報を取得 */
    fun getWeatherByCity(city: String): Flow<Result<Pair<String, List<WeatherInfo>>>>

    /** 緯度経度から天気情報を取得 */
    fun getWeatherByLocation(lat: Double, lon: Double): Flow<Result<Pair<String, List<WeatherInfo>>>>
}