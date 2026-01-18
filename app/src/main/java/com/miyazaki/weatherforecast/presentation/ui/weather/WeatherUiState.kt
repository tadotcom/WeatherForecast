package com.miyazaki.weatherforecast.presentation.ui.weather

import com.miyazaki.weatherforecast.domain.model.WeatherInfo

sealed interface WeatherUiState {

    data object Loading : WeatherUiState

    data class Success(
        val weatherList: List<WeatherInfo>,
        val cityName: String
    ) : WeatherUiState

    data class Error(val message: String) : WeatherUiState
}