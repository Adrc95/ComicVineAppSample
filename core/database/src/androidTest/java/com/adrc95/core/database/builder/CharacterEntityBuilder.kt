package com.adrc95.core.database.builder

import com.adrc95.core.database.entity.CharacterEntity

class CharacterEntityBuilder {
    var id: Long = 1L
    var name: String = "Spider-Man"
    var shortDescription: String? = "Friendly neighborhood hero."
    var longDescription: String? = "Detailed description."
    var uri: String = "https://comicvine.gamespot.com/spider-man/4005-1443/"
    var thumbnail: String = "https://example.com/spiderman.png"
    var favorite: Boolean? = false

    fun withId(id: Long) = apply { this.id = id }

    fun withName(name: String) = apply { this.name = name }

    fun withShortDescription(shortDescription: String?) = apply {
        this.shortDescription = shortDescription
    }

    fun withLongDescription(longDescription: String?) = apply {
        this.longDescription = longDescription
    }

    fun withUri(uri: String) = apply { this.uri = uri }

    fun withThumbnail(thumbnail: String) = apply { this.thumbnail = thumbnail }

    fun withFavorite(favorite: Boolean?) = apply { this.favorite = favorite }

    fun build() = CharacterEntity(
        id = id,
        name = name,
        shortDescription = shortDescription,
        longDescription = longDescription,
        uri = uri,
        thumbnail = thumbnail,
        favorite = favorite
    )
}

fun characterEntity(block: CharacterEntityBuilder.() -> Unit = {}) =
    CharacterEntityBuilder().apply(block).build()
