package com.miyazaki.weatherforecast.presentation.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.miyazaki.weatherforecast.presentation.navigation.Screen
import com.miyazaki.weatherforecast.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text(stringResource(R.string.home_title)) })
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(viewModel.cities) { city ->
                CityItem(city = city, onClick = {
                    navController.navigate(Screen.Weather.createRoute(city))
                })
                HorizontalDivider()
            }
        }
    }
}

@Composable
fun CityItem(city: String, onClick: () -> Unit) {
    ListItem(
        headlineContent = { Text(text = city, style = MaterialTheme.typography.titleMedium) },
        modifier = Modifier.clickable { onClick() }
    )
}