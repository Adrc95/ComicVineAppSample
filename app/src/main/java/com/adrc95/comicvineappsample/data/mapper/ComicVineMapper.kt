package com.adrc95.comicvineappsample.data.mapper

import com.adrc95.domain.model.Character
import com.adrc95.comicvineappsample.data.database.entity.CharacterEntity
import com.adrc95.comicvineappsample.data.server.model.CharacterDataResponse
import com.adrc95.comicvineappsample.data.server.model.CharacterDetailResponse
import com.adrc95.comicvineappsample.data.server.model.Image
import com.adrc95.comicvineappsample.data.server.model.Character as ServerCharacter

fun CharacterDataResponse.toDomain(): List<Character> =
    results.map { it.toListDomain() }

fun CharacterDetailResponse.toDomain(): Character =
    results.toDetailDomain()

private fun ServerCharacter.toListDomain(): Character = Character(
    id = id,
    name = name,
    shortDescription = description.toDomainDescription(deck),
    longDescription = null,
    uri = siteDetailUrl ?: apiDetailUrl.orEmpty(),
    thumbnail = image.toListThumbnail(),
    favorite = false
)

private fun ServerCharacter.toDetailDomain(): Character = Character(
    id = id,
    name = name,
    shortDescription = deck.toDomainDescription(null),
    longDescription = description.toDomainDescription(deck),
    uri = siteDetailUrl ?: apiDetailUrl.orEmpty(),
    thumbnail = image.toDetailThumbnail(),
    favorite = false
)

fun CharacterEntity.toDomain(): Character = Character(
    this.id,
    this.name,
    this.shortDescription.orEmpty(),
    this.longDescription,
    this.uri,
    thumbnail,
    favorite ?: false,
)

fun Character.toEntity(): CharacterEntity = CharacterEntity(
    this.id,
    this.name,
    this.shortDescription,
    this.longDescription,
    this.uri,
    thumbnail,
    favorite
)

private fun String?.toDomainDescription(fallback: String?): String {
    val value = this?.ifBlank { null } ?: fallback.orEmpty()
    return value.replace(Regex("<[^>]+>"), " ").replace(Regex("\\s+"), " ").trim()
}

private fun Image?.toListThumbnail(): String =
    this?.mediumUrl ?: this?.iconUrl ?: this?.originalUrl.orEmpty()

private fun Image?.toDetailThumbnail(): String =
    this?.originalUrl ?: this?.mediumUrl ?: this?.iconUrl.orEmpty()
