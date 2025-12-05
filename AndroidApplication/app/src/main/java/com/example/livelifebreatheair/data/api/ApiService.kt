package com.example.livelifebreatheair.data.api
import com.example.livelifebreatheair.data.model.AirQualityData
import com.example.livelifebreatheair.data.model.PollenData
import com.example.livelifebreatheair.data.model.WeatherData
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.json.Json

interface IApiService {
    suspend fun getAirQualityData(): Result<AirQualityData>
    suspend fun getPollenData(): Result<PollenData>
    suspend fun getWeatherData(): Result<WeatherData>
}

class ApiService : IApiService {
    private val client = ApiClient.client
    private val baseUrl = "http://10.0.2.2:8000/api"

    override suspend fun getAirQualityData(): Result<AirQualityData> {
        return try {
            val response = client.get("${baseUrl}/aqi_data") {
                contentType(ContentType.Application.Json)
            }
            val aiq = response.body<String>()
            if (response.status != HttpStatusCode.OK) {
                return Result.failure(Exception("Failed to get air quality data: ${response.status}"))
            }
            Result.success(Json.decodeFromString<AirQualityData>(aiq ))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getPollenData(): Result<PollenData> {
        return try {
            val response = client.get("${baseUrl}/pollen_data") {
                contentType(ContentType.Application.Json)
            }
            val metadata = response.body<String>()
            if (response.status != HttpStatusCode.OK) {
                return Result.failure(Exception("Failed to get group metadata: ${response.status}"))
            }
            Result.success(Json.decodeFromString<PollenData>(metadata))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getWeatherData(): Result<WeatherData> {
        return try {
            val response = client.get("${baseUrl}/weather_data") {
                contentType(ContentType.Application.Json)
            }
            val metadata = response.body<String>()
            if (response.status != HttpStatusCode.OK) {
                return Result.failure(Exception("Failed to get weather data: ${response.status}"))
            }
            Result.success(Json.decodeFromString<WeatherData>(metadata))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}