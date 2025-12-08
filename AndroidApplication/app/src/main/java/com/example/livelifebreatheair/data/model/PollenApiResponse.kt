package com.example.livelifebreatheair.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PollenData(
    val pollen: Pollen
)

@Serializable
data class Pollen(
    val regionCode: String,
    val dailyInfo: List<DailyInfo>
)

@Serializable
data class DailyInfo(
    val date: DateInfo,
    val pollenTypeInfo: List<PollenTypeInfo>
)

@Serializable
data class DateInfo(
    val year: Int,
    val month: Int,
    val day: Int
)

@Serializable
data class PollenTypeInfo(
    val code: String,
    val displayName: String,
    val indexInfo: IndexInfo
)

@Serializable
data class IndexInfo(
    val value: Int,
    val category: String,
    val indexDescription: String
)