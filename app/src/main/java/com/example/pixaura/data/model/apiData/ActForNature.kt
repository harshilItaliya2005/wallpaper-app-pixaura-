package com.example.pixaura.data.model.apiData


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActForNature(
    @SerialName("approved_on")
    val approvedOn: String,
    @SerialName("status")
    val status: String
)