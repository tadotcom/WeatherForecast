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
    private val getWeatherUseCase: GetWeatherByCityUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    // Navigation引数の取得
    val city: String? = savedStateHandle["city"]
    private val latString: String? = savedStateHandle["lat"]
    private val lonString: String? = savedStateHandle["lon"]

    private var fetchJob: Job? = null

    init {
        fetchWeather()
    }

    fun fetchWeather() {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading

            val resultFlow = when {
                // 緯度経度が渡された場合
                latString != null && lonString != null -> {
                    val lat = latString.toDoubleOrNull()
                    val lon = lonString.toDoubleOrNull()

                    if (lat != null && lon != null) {
                        getWeatherUseCase(lat, lon)
                    } else {
                        kotlinx.coroutines.flow.flow {
                            emit(Result.failure(Exception("位置情報が無効です")))
                        }
                    }
                }
                // 都市名が渡された場合
                city != null -> {
                    getWeatherUseCase(city)
                }
                else -> {
                    kotlinx.coroutines.flow.flow {
                        emit(Result.failure(Exception("都市名または位置情報が必要です")))
                    }
                }
            }

            resultFlow.collect { result ->
                _uiState.value = result.fold(
                    onSuccess = { (cityName, list) ->
                        WeatherUiState.Success(
                            weatherList = list,
                            cityName = cityName
                        )
                    },
                    onFailure = { error ->
                        WeatherUiState.Error(error.message ?: "通信に失敗しました")
                    }
                )
            }
        }
    }
}