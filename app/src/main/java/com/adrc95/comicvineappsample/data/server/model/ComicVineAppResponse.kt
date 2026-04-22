package com.adrc95.comicvineappsample.data.server.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CharacterDataResponse(
    @SerialName("status_code")
    val statusCode: Int,
    @SerialName("error")
    val error: String,
    @SerialName("limit")
    val limit: Int,
    @SerialName("offset")
    val offset: Int,
    @SerialName("number_of_total_results")
    val totalResults: Int,
    @SerialName("number_of_page_results")
    val pageResults: Int,
    @SerialName("results")
    val results: List<Character>
)

@Serializable
data class CharacterDetailResponse(
    @SerialName("status_code")
    val statusCode: Int,
    @SerialName("error")
    val error: String,
    @SerialName("limit")
    val limit: Int,
    @SerialName("offset")
    val offset: Int,
    @SerialName("number_of_total_results")
    val totalResults: Int,
    @SerialName("number_of_page_results")
    val pageResults: Int,
    @SerialName("results")
    val results: Character
)

@Serializable
data class Character(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String = "",
    @SerialName("deck")
    val deck: String? = null,
    @SerialName("description")
    val description: String? = null,
    @SerialName("site_detail_url")
    val siteDetailUrl: String? = null,
    @SerialName("api_detail_url")
    val apiDetailUrl: String? = null,
    @SerialName("image")
    val image: Image? = null
)

@Serializable
data class Image(
    @SerialName("original_url")
    val originalUrl: String? = null,
    @SerialName("medium_url")
    val mediumUrl: String? = null,
    @SerialName("icon_url")
    val iconUrl: String? = null
)
