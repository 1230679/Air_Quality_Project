package com.example.livelifebreatheair.data.api

import com.example.livelifebreatheair.data.model.AirQualityIndexApiResponse
import com.example.livelifebreatheair.data.model.PollenData
import com.example.livelifebreatheair.data.model.WeatherApiResponse
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.json.Json

interface IApiService {
    suspend fun getAirQualityData(): Result<AirQualityIndexApiResponse>
    suspend fun getPollenData(): Result<PollenData>
    suspend fun getWeatherData(): Result<WeatherApiResponse>
}

class ApiService : IApiService {
    private val client = ApiClient.client
    private val baseUrl = "http://10.0.2.2:8000/api"
    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun getAirQualityData(): Result<AirQualityIndexApiResponse> {
        return try {
            val response = client.get("${baseUrl}/aiq_data") {
                contentType(ContentType.Application.Json)
            }
            if (response.status != HttpStatusCode.OK) {
                return Result.failure(Exception("Failed to get air quality data: ${response.status}"))
            }
            val aiq = response.body<String>()
            Result.success(json.decodeFromString<AirQualityIndexApiResponse>(aiq))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getPollenData(): Result<PollenData> {
        return try {
            val response = client.get("${baseUrl}/pollen_data") {
                contentType(ContentType.Application.Json)
            }
            if (response.status != HttpStatusCode.OK) {
                return Result.failure(Exception("Failed to get pollen data: ${response.status}"))
            }
            val metadata = response.body<String>()
            Result.success(json.decodeFromString<PollenData>(metadata))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getWeatherData(): Result<WeatherApiResponse> {
        return try {
            val response = client.get("${baseUrl}/weather_data") {
                contentType(ContentType.Application.Json)
            }
            if (response.status != HttpStatusCode.OK) {
                return Result.failure(Exception("Failed to get weather data: ${response.status}"))
            }
            val metadata = response.body<String>()
            Result.success(json.decodeFromString<WeatherApiResponse>(metadata))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
