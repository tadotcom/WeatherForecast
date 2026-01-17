package com.miyazaki.weatherforecast.presentation.ui.home

import androidx.lifecycle.ViewModel
import com.miyazaki.weatherforecast.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
    val cities = Constants.HOME_CITIES
}