package com.example.livelifebreatheair.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.livelifebreatheair.data.model.AirQualityIndexApiResponse
import com.example.livelifebreatheair.data.model.PollenData
import com.example.livelifebreatheair.data.model.WeatherData
import com.example.livelifebreatheair.data.repository.ApiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HistoricalDataViewModel (
    private val repository: ApiRepository = ApiRepository()
): ViewModel(){
    private val _airQuality = MutableStateFlow<Result<AirQualityIndexApiResponse>?>(null)
    val airQuality: StateFlow<Result<AirQualityIndexApiResponse>?> = _airQuality.asStateFlow()

    private val _pollen = MutableStateFlow<Result<PollenData>?>(null)
    val pollen: StateFlow<Result<PollenData>?> = _pollen.asStateFlow()

    private val _weather = MutableStateFlow<Result<WeatherData>?>(null)
    val weather: StateFlow<Result<WeatherData>?> = _weather.asStateFlow()

    fun loadAirQuality() {
        viewModelScope.launch {
            _airQuality.value = repository.getAirQualityData()
        }
    }

    fun loadPollen() {
        viewModelScope.launch {
            _pollen.value = repository.getPollenData()
        }
    }

    fun loadWeather() {
        viewModelScope.launch {
            _weather.value = repository.getWeatherData()
        }
    }
}