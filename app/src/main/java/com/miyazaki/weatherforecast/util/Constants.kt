package com.miyazaki.weatherforecast.util

object Constants {
    const val DATABASE_NAME = "weather_database"

    val CITY_NAME_MAP = mapOf(
        "東京" to "Tokyo",
        "兵庫" to "Hyogo",
        "大分" to "Oita",
        "北海道" to "Hokkaido"
    )

    // ホーム画面の都市リスト
    val HOME_CITIES = listOf("東京", "兵庫", "大分", "北海道")
}