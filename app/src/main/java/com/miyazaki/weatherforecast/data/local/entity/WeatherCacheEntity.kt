package com.miyazaki.weatherforecast.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.miyazaki.weatherforecast.data.local.Converters
import com.miyazaki.weatherforecast.domain.model.WeatherInfo

@Entity(tableName = "weather_cache")
data class WeatherCacheEntity(
    @PrimaryKey
    val cacheKey: String,

    @TypeConverters(Converters::class)
    val weatherList: List<WeatherInfo>,
    val cachedAt: Long
)