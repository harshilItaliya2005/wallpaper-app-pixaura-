package com.example.pixaura.data.model.apiData


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TexturesPatterns(
    @SerialName("status")
    val status: String
)