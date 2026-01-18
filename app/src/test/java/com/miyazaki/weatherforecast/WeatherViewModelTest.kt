package com.miyazaki.weatherforecast.presentation.ui.weather

import androidx.lifecycle.SavedStateHandle
import com.miyazaki.weatherforecast.MainDispatcherRule
import com.miyazaki.weatherforecast.domain.model.WeatherInfo
import com.miyazaki.weatherforecast.domain.usecase.GetWeatherByCityUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

/**
 * WeatherViewModelの単体テスト
 *
 * テスト項目:
 * - 都市名指定時のデータ取得成功
 * - データ取得失敗時のエラーハンドリング
 * - 緯度経度指定時のデータ取得
 * - 不正なパラメータのハンドリング
 */
class WeatherViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getWeatherByCityUseCase: GetWeatherByCityUseCase = mockk()

    @Test
    fun `都市名指定時、データ取得に成功してSuccessになること`() {
        // Arrange
        val savedStateHandle = SavedStateHandle(mapOf("city" to "Tokyo"))
        val dummyList = listOf(
            WeatherInfo("10:00", 20.0, "Sunny", "01d", "url")
        )
        coEvery { getWeatherByCityUseCase("Tokyo") } returns flowOf(
            Result.success(Pair("Tokyo", dummyList))
        )

        // Act
        val viewModel = WeatherViewModel(getWeatherByCityUseCase, savedStateHandle)
        val state = viewModel.uiState.value

        // Assert
        assertTrue("状態がSuccessであること", state is WeatherUiState.Success)
        assertEquals("天気リストが一致すること", dummyList, (state as WeatherUiState.Success).weatherList)
    }

    @Test
    fun `データ取得失敗時、UiStateがErrorになること`() {
        // Arrange
        val savedStateHandle = SavedStateHandle(mapOf("city" to "Tokyo"))
        val errorMessage = "通信エラー"
        coEvery { getWeatherByCityUseCase("Tokyo") } returns flowOf(
            Result.failure(Exception(errorMessage))
        )

        // Act
        val viewModel = WeatherViewModel(getWeatherByCityUseCase, savedStateHandle)
        val state = viewModel.uiState.value

        // Assert
        assertTrue("状態がErrorであること", state is WeatherUiState.Error)
        assertEquals("エラーメッセージが一致すること", errorMessage, (state as WeatherUiState.Error).message)
    }

    @Test
    fun `緯度経度指定時、データ取得に成功してSuccessになること`() {
        // Arrange
        val lat = 35.6762
        val lon = 139.6503
        val savedStateHandle = SavedStateHandle(
            mapOf(
                "city" to null,
                "lat" to lat.toString(),
                "lon" to lon.toString()
            )
        )
        val dummyList = listOf(
            WeatherInfo("12:00", 18.0, "Cloudy", "02d", "url")
        )
        coEvery { getWeatherByCityUseCase(lat, lon) } returns flowOf(
            Result.success(Pair("現在地", dummyList))
        )

        // Act
        val viewModel = WeatherViewModel(getWeatherByCityUseCase, savedStateHandle)
        val state = viewModel.uiState.value

        // Assert
        assertTrue("状態がSuccessであること", state is WeatherUiState.Success)
        assertEquals("天気リストが一致すること", dummyList, (state as WeatherUiState.Success).weatherList)
    }

    @Test
    fun `緯度経度が不正な場合、UiStateがErrorになること`() {
        // Arrange
        val savedStateHandle = SavedStateHandle(
            mapOf(
                "city" to null,
                "lat" to "invalid",
                "lon" to "123.45"
            )
        )

        // Act
        val viewModel = WeatherViewModel(getWeatherByCityUseCase, savedStateHandle)
        val state = viewModel.uiState.value

        // Assert
        assertTrue("状態がErrorであること", state is WeatherUiState.Error)
        assertEquals(
            "エラーメッセージが「位置情報が無効です」",
            "位置情報が無効です",
            (state as WeatherUiState.Error).message
        )
    }

    @Test
    fun `都市名も緯度経度もnullの場合、UiStateがErrorになること`() {
        // Arrange
        val savedStateHandle = SavedStateHandle(
            mapOf(
                "city" to null,
                "lat" to null,
                "lon" to null
            )
        )

        // Act
        val viewModel = WeatherViewModel(getWeatherByCityUseCase, savedStateHandle)
        val state = viewModel.uiState.value

        // Assert
        assertTrue("状態がErrorであること", state is WeatherUiState.Error)
        assertEquals(
            "エラーメッセージが「都市名または位置情報が必要です」",
            "都市名または位置情報が必要です",
            (state as WeatherUiState.Error).message
        )
    }
}