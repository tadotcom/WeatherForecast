package com.miyazaki.weatherforecast.data.local

import androidx.room.TypeConverter
import com.miyazaki.weatherforecast.domain.model.WeatherInfo
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class Converters {
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val listType = Types.newParameterizedType(List::class.java, WeatherInfo::class.java)
    private val adapter = moshi.adapter<List<WeatherInfo>>(listType)

    @TypeConverter
    fun fromWeatherInfoList(value: List<WeatherInfo>): String {
        return adapter.toJson(value)
    }

    @TypeConverter
    fun toWeatherInfoList(value: String): List<WeatherInfo> {
        return try {
            adapter.fromJson(value) ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}