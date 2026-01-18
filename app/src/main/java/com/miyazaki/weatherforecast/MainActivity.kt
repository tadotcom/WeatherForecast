package com.miyazaki.weatherforecast

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.miyazaki.weatherforecast.presentation.navigation.Screen
import com.miyazaki.weatherforecast.presentation.ui.home.HomeScreen
import com.miyazaki.weatherforecast.presentation.ui.theme.WeatherForecastTheme
import com.miyazaki.weatherforecast.presentation.ui.weather.WeatherScreen
import dagger.hilt.android.AndroidEntryPoint

/**
 * アプリのメインActivity
 *
 * Navigation Composeを使用した画面遷移を管理します。
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherForecastTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = Screen.Home.route
                    ) {
                        // ホーム画面（地域選択）
                        composable(Screen.Home.route) {
                            HomeScreen(navController = navController)
                        }

                        // 天気画面（都市名指定）
                        composable(
                            route = Screen.Weather.route,
                            arguments = listOf(
                                navArgument("city") { type = NavType.StringType }
                            )
                        ) {
                            WeatherScreen(navController = navController)
                        }

                        // 天気画面(現在地・緯度経度指定)
                        composable(
                            route = Screen.WeatherLocation.route,
                            arguments = listOf(
                                navArgument("lat") { type = NavType.StringType },
                                navArgument("lon") { type = NavType.StringType }
                            )
                        ) {
                            WeatherScreen(navController = navController)
                        }
                    }
                }
            }
        }
    }
}