package com.miyazaki.weatherforecast.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.miyazaki.weatherforecast.domain.model.WeatherInfo

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromWeatherInfoList(value: List<WeatherInfo>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toWeatherInfoList(value: String): List<WeatherInfo> {
        val listType = object : TypeToken<List<WeatherInfo>>() {}.type
        return gson.fromJson(value, listType)
    }
}