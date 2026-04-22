package com.adrc95.comicvineappsample.ui.mapper

import com.adrc95.comicvineappsample.ui.model.CharacterDisplayModel
import com.adrc95.domain.model.Character

fun Character.toDisplayModel(): CharacterDisplayModel = CharacterDisplayModel(
    id = id,
    name = name,
    shortDescription = shortDescription,
    longDescription = longDescription,
    thumbnail = thumbnail,
    favorite = favorite
)
