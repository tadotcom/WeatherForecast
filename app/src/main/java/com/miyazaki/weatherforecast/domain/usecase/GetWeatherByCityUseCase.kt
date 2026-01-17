package com.miyazaki.weatherforecast.domain.usecase

import com.miyazaki.weatherforecast.domain.model.WeatherInfo
import com.miyazaki.weatherforecast.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 都市名から天気情報を取得するユースケース
 */
class GetWeatherByCityUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    operator fun invoke(city: String): Flow<Result<List<WeatherInfo>>> {
        return repository.getWeatherByCity(city)
    }
}