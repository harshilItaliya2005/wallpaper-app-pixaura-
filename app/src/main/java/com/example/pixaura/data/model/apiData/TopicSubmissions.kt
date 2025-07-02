package com.example.pixaura.data.model.apiData


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TopicSubmissions(
    @SerialName("act-for-nature")
    val actForNature: ActForNature,
    @SerialName("animals")
    val animals: Animals,
    @SerialName("color-of-water")
    val colorOfWater: ColorOfWater,
    @SerialName("cool-tones")
    val coolTones: CoolTones,
    @SerialName("health")
    val health: Health,
    @SerialName("macro-moments")
    val macroMoments: MacroMoments,
    @SerialName("nature")
    val nature: Nature,
    @SerialName("spirituality")
    val spirituality: Spirituality,
    @SerialName("spring")
    val spring: Spring,
    @SerialName("sustainability")
    val sustainability: Sustainability,
    @SerialName("textures-patterns")
    val texturesPatterns: TexturesPatterns,
    @SerialName("travel")
    val travel: Travel,
    @SerialName("wallpapers")
    val wallpapers: Wallpapers
)