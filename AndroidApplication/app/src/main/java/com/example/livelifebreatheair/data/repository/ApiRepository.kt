package com.example.livelifebreatheair.data.repository

import com.example.livelifebreatheair.data.api.ApiService

class ApiRepository (
    private val apiService: ApiService = ApiService()
){
    suspend fun getAirQualityData() = apiService.getAirQualityData()
    suspend fun getPollenData() = apiService.getPollenData()
    suspend fun getWeatherData() = apiService.getWeatherData()
    suspend fun getAirQualityIndexForecast() = apiService.getAirQualityIndexForecast()
}