package com.miyazaki.weatherforecast.presentation.navigation

sealed class Screen(val route: String) {
    // ホーム画面
    data object Home : Screen("home_screen")

    // 天気画面
    data object Weather : Screen("weather_screen/{city}") {
        fun createRoute(city: String) = "weather_screen/$city"
    }
}