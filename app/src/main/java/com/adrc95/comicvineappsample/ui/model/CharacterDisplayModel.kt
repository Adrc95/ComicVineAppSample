package com.adrc95.comicvineappsample.ui.model

data class CharacterDisplayModel(
    val id: Long,
    val name: String,
    val shortDescription: String,
    val longDescription: String?,
    val thumbnail: String,
    val favorite: Boolean
)
