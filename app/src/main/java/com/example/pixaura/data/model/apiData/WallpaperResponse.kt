package com.example.pixaura.data.model.apiData


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WallpaperResponse(
    @SerialName("results")
    val results: List<Result>,
    @SerialName("total")
    val total: Int,
    @SerialName("total_pages")
    val totalPages: Int
)