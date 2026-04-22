package com.adrc95.comicvineappsample.ui.mapper

import com.adrc95.comicvineappsample.ui.common.ModeTypeDisplayModel
import com.adrc95.domain.model.ThemeMode

fun ThemeMode.toDisplayModel(): ModeTypeDisplayModel =
    when (this) {
        ThemeMode.DAY -> ModeTypeDisplayModel.DAY
        ThemeMode.NIGHT -> ModeTypeDisplayModel.NIGHT
        ThemeMode.AUTOMATIC -> ModeTypeDisplayModel.AUTOMATIC
    }

fun ModeTypeDisplayModel.toDomain(): ThemeMode =
    when (this) {
        ModeTypeDisplayModel.DAY -> ThemeMode.DAY
        ModeTypeDisplayModel.NIGHT -> ThemeMode.NIGHT
        ModeTypeDisplayModel.AUTOMATIC -> ThemeMode.AUTOMATIC
    }
