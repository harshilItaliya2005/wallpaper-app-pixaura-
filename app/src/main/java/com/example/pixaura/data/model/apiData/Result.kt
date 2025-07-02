package com.example.pixaura.data.model.apiData


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Result(
    @SerialName("alt_description")
    val altDescription: String,
    @SerialName("alternative_slugs")
    val alternativeSlugs: AlternativeSlugs,
    @SerialName("asset_type")
    val assetType: String,
    @SerialName("blur_hash")
    val blurHash: String,
    @SerialName("color")
    val color: String,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("description")
    val description: String? = null,
    @SerialName("height")
    val height: Int,
    @SerialName("id")
    val id: String,
    @SerialName("liked_by_user")
    val likedByUser: Boolean,
    @SerialName("likes")
    val likes: Int,
    @SerialName("links")
    val links: Links,
    @SerialName("promoted_at")
    val promotedAt: String? = null,
    @SerialName("slug")
    val slug: String? = null,
    @SerialName("updated_at")
    val updatedAt: String,
    @SerialName("urls")
    val urls: Urls,
    @SerialName("width")
    val width: Int
)