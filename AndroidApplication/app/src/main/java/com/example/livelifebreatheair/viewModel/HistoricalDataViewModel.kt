package com.example.livelifebreatheair.viewModel

import androidx.lifecycle.ViewModel
import com.example.livelifebreatheair.data.repository.ApiRepository

class HistoricalDataViewModel (
    private val repository: ApiRepository = ApiRepository()
): ViewModel(){

    val _historicalAirQualityData = List<AirQualityData>
    suspend fun loadHistoricalAirQualityData() = repository.getAirQualityData()
}