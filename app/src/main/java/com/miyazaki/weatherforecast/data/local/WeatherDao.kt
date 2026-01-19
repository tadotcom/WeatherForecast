package com.miyazaki.weatherforecast.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.miyazaki.weatherforecast.data.local.entity.WeatherCacheEntity

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(entity: WeatherCacheEntity)

    @Query("SELECT * FROM weather_cache WHERE cacheKey = :key")
    suspend fun getWeatherByKey(key: String): WeatherCacheEntity?

    @Query("DELETE FROM weather_cache WHERE cacheKey NOT LIKE '%' || :todayDate || '%'")
    suspend fun deletePastCache(todayDate: String)

    @Query("DELETE FROM weather_cache")
    suspend fun deleteAll()
}