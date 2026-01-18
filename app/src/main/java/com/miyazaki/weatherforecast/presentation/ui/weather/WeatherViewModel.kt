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

/**
 * 天気画面のViewModel
 *
 * 都市名または緯度経度から天気情報を取得し、UI状態を管理します。
 */
@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getWeatherUseCase: GetWeatherByCityUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    /** Navigation引数から取得した都市名(現在地の場合はnull) */
    private val inputCity: String? = savedStateHandle["city"]

    /** 画面タイトル用の表示名(都市名または"現在地") */
    val city: String = inputCity ?: "現在地"

    /** Navigation引数から取得した緯度と緯度 */
    private val latString: String? = savedStateHandle["lat"]
    private val lonString: String? = savedStateHandle["lon"]

    private var fetchJob: Job? = null

    init {
        fetchWeather()
    }

    /**
     * 天気情報を取得
     *
     * 緯度経度が指定されている場合は位置情報から、
     * 都市名が指定されている場合は都市名から取得します。
     */
    fun fetchWeather() {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading

            val resultFlow = when {
                // 緯度経度による取得
                latString != null && lonString != null -> {
                    val lat = latString.toDoubleOrNull()
                    val lon = lonString.toDoubleOrNull()

                    if (lat != null && lon != null) {
                        getWeatherUseCase(lat, lon)
                    } else {
                        // パラメータ変換失敗
                        kotlinx.coroutines.flow.flow {
                            emit(Result.failure(Exception("位置情報が無効です")))
                        }
                    }
                }
                // 都市名による取得
                inputCity != null -> {
                    getWeatherUseCase(inputCity)
                }
                // パラメータなし(異常系)
                else -> {
                    kotlinx.coroutines.flow.flow {
                        emit(Result.failure(Exception("都市名または位置情報が必要です")))
                    }
                }
            }

            resultFlow.collect { result ->
                _uiState.value = result.fold(
                    onSuccess = { WeatherUiState.Success(it) },
                    onFailure = { error ->
                        WeatherUiState.Error(error.message ?: "通信に失敗しました")
                    }
                )
            }
        }
    }
}