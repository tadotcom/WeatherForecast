package com.miyazaki.weatherforecast.presentation.ui.weather

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miyazaki.weatherforecast.domain.usecase.GetWeatherByCityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getWeatherByCityUseCase: GetWeatherByCityUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    val city: String = checkNotNull(savedStateHandle["city"])

    private var fetchJob: Job? = null

    init {
        fetchWeather()
    }

    fun fetchWeather() {
        fetchJob?.cancel()

        fetchJob = viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading

            getWeatherByCityUseCase(city).collect { result ->
                _uiState.value = result.fold(
                    onSuccess = { WeatherUiState.Success(it) },
                    onFailure = { error ->
                        WeatherUiState.Error(error.message ?: "通信に失敗しました。")
                    }
                )
            }
        }
    }
}