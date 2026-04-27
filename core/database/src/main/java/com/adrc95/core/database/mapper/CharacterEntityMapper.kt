package com.adrc95.core.database.mapper

import com.adrc95.core.database.entity.CharacterEntity
import com.adrc95.domain.model.Character


fun CharacterEntity.toDomain(): Character = Character(
    id = id,
    name = name,
    shortDescription = shortDescription.orEmpty(),
    longDescription = longDescription,
    uri = uri,
    thumbnail = thumbnail,
    favorite = favorite ?: false,
)

fun Character.toEntity(): CharacterEntity = CharacterEntity(
    id = id,
    name = name,
    shortDescription = shortDescription,
    longDescription = longDescription,
    uri = uri,
    thumbnail = thumbnail,
    favorite = favorite
)
