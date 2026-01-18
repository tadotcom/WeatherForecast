package com.miyazaki.weatherforecast.presentation.navigation

/**
 * アプリ内の画面遷移を定義
 */
sealed class Screen(val route: String) {
    // ホーム画面
    data object Home : Screen("home_screen")

    /** 天気画面(都市名指定) */
    data object Weather : Screen("weather_screen/{city}") {
        fun createRoute(city: String) = "weather_screen/$city"
    }

    /** 天気画面(現在地・緯度経度指定) */
    data object WeatherLocation : Screen("weather_location_screen/{lat}/{lon}") {
        fun createRoute(lat: Double, lon: Double) = "weather_location_screen/$lat/$lon"
    }
}