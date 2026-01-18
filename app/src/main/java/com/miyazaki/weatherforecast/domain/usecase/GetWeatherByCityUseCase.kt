package com.miyazaki.weatherforecast.domain.usecase

import com.miyazaki.weatherforecast.domain.model.WeatherInfo
import com.miyazaki.weatherforecast.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 天気情報を取得するユースケース
 *
 * 都市名または緯度経度から天気予報を取得します。
 */
class GetWeatherByCityUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    /** 都市名から天気情報を取得 */
    operator fun invoke(city: String): Flow<Result<Pair<String, List<WeatherInfo>>>> {
        return repository.getWeatherByCity(city)
    }

    /** 緯度経度から天気情報を取得 */
    operator fun invoke(lat: Double, lon: Double): Flow<Result<Pair<String, List<WeatherInfo>>>> {
        return repository.getWeatherByLocation(lat, lon)
    }
}