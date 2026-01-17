package com.miyazaki.weatherforecast.presentation.ui.weather

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.miyazaki.weatherforecast.presentation.ui.weather.components.ErrorView
import com.miyazaki.weatherforecast.presentation.ui.weather.components.LoadingView
import com.miyazaki.weatherforecast.presentation.ui.weather.components.WeatherItem
import androidx.compose.ui.res.stringResource
import com.miyazaki.weatherforecast.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(
    navController: NavController,
    viewModel: WeatherViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.weather_title_format, viewModel.city)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "戻る"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        when (val state = uiState) {
            is WeatherUiState.Loading -> {
                LoadingView()
            }
            is WeatherUiState.Error -> {
                ErrorView(
                    message = state.message,
                    onRetry = { viewModel.fetchWeather() }
                )
            }
            is WeatherUiState.Success -> {
                LazyColumn(
                    modifier = Modifier.padding(paddingValues)
                ) {
                    items(state.weatherList) { weather ->
                        WeatherItem(info = weather)
                    }
                }
            }
        }
    }
}