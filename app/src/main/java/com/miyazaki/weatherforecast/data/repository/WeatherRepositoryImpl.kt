package com.miyazaki.weatherforecast.data.repository

import com.miyazaki.weatherforecast.BuildConfig
import com.miyazaki.weatherforecast.data.mapper.toDomain
import com.miyazaki.weatherforecast.data.remote.WeatherApi
import com.miyazaki.weatherforecast.domain.model.WeatherInfo
import com.miyazaki.weatherforecast.domain.repository.WeatherRepository
import com.miyazaki.weatherforecast.util.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApi
) : WeatherRepository {

    override fun getWeatherByCity(city: String): Flow<Result<List<WeatherInfo>>> = flow {
        try {
            val queryCity = Constants.CITY_NAME_MAP[city] ?: city

            val response = api.get5DayForecast(
                city = queryCity,
                apiKey = BuildConfig.WEATHER_API_KEY
            )

            val domainData = response.list.map { it.toDomain() }
            emit(Result.success(domainData))

        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.failure(e))
        }
    }
}