package com.miyazaki.weatherforecast.presentation.ui.weather

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.miyazaki.weatherforecast.R
import com.miyazaki.weatherforecast.presentation.ui.weather.components.CurrentWeatherCard
import com.miyazaki.weatherforecast.presentation.ui.weather.components.ErrorView
import com.miyazaki.weatherforecast.presentation.ui.weather.components.LoadingView
import com.miyazaki.weatherforecast.presentation.ui.weather.components.WeatherItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(
    navController: NavController,
    viewModel: WeatherViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val screenTitle = when (val state = uiState) {
        is WeatherUiState.Success -> "${state.cityName}の天気"
        else -> stringResource(R.string.app_name)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(screenTitle)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_button_desc)
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
                    item {
                        state.weatherList.firstOrNull()?.let { currentWeather ->
                            CurrentWeatherCard(info = currentWeather)

                            Text(
                                text = stringResource(R.string.forecast_title),
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }
                    }

                    items(state.weatherList.drop(1)) { weather ->
                        WeatherItem(info = weather)
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}