package com.miyazaki.weatherforecast.data.repository

import com.miyazaki.weatherforecast.BuildConfig
import com.miyazaki.weatherforecast.data.local.WeatherDao
import com.miyazaki.weatherforecast.data.local.entity.WeatherCacheEntity
import com.miyazaki.weatherforecast.data.mapper.toDomain
import com.miyazaki.weatherforecast.data.remote.WeatherApi
import com.miyazaki.weatherforecast.domain.model.WeatherInfo
import com.miyazaki.weatherforecast.domain.repository.WeatherRepository
import com.miyazaki.weatherforecast.util.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlin.math.roundToInt

class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApi,
    private val weatherDao: WeatherDao
) : WeatherRepository {

    override fun getWeatherByCity(city: String): Flow<Result<Pair<String, List<WeatherInfo>>>> = flow {
        val queryCity = Constants.CITY_NAME_MAP[city] ?: city

        val today = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
        val cacheKey = "weather_${queryCity}_$today"

        try {
            val cachedData = weatherDao.getWeatherByKey(cacheKey)

            if (cachedData != null) {
                emit(Result.success(Pair(city, cachedData.weatherList)))
            } else {
                val response = api.get5DayForecast(
                    city = queryCity,
                    apiKey = BuildConfig.WEATHER_API_KEY
                )
                val domainData = response.list.map { it.toDomain() }
                val newCache = WeatherCacheEntity(
                    cacheKey = cacheKey,
                    weatherList = domainData,
                    cachedAt = System.currentTimeMillis()
                )
                weatherDao.insertWeather(newCache)
                emit(Result.success(Pair(response.city.name, domainData)))
            }

        } catch (_: IOException) {
            emit(Result.failure(Exception("通信に失敗しました。")))
        } catch (_: HttpException) {
            emit(Result.failure(Exception("サーバーエラーが発生しました。時間を置いて再度お試しください。")))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.failure(e))
        }
    }

    override fun getWeatherByLocation(lat: Double, lon: Double): Flow<Result<Pair<String, List<WeatherInfo>>>> = flow {
        val today = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
        val roundedLat = (lat * 100).roundToInt() / 100.0
        val roundedLon = (lon * 100).roundToInt() / 100.0
        val cacheKey = "location_${roundedLat}_${roundedLon}_$today"

        try {
            val cachedData = weatherDao.getWeatherByKey(cacheKey)

            if (cachedData != null) {
                emit(Result.success(Pair("現在地の天気", cachedData.weatherList)))
            } else {
                val response = api.getForecastByGeo(
                    lat = lat,
                    lon = lon,
                    apiKey = BuildConfig.WEATHER_API_KEY
                )
                val domainData = response.list.map { it.toDomain() }
                val newCache = WeatherCacheEntity(
                    cacheKey = cacheKey,
                    weatherList = domainData,
                    cachedAt = System.currentTimeMillis()
                )
                weatherDao.insertWeather(newCache)
                emit(Result.success(Pair(response.city.name, domainData)))
            }

        } catch (_: IOException) {
            emit(Result.failure(Exception("通信に失敗しました。")))
        } catch (_: HttpException) {
            emit(Result.failure(Exception("サーバーエラーが発生しました。時間を置いて再度お試しください。")))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.failure(e))
        }
    }
}