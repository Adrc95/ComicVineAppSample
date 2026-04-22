package com.adrc95.data.mapper

import com.adrc95.data.model.StoredThemeMode
import com.adrc95.domain.model.ThemeMode

fun StoredThemeMode.toDomain(): ThemeMode =
    when (this) {
        StoredThemeMode.DAY -> ThemeMode.DAY
        StoredThemeMode.NIGHT -> ThemeMode.NIGHT
        StoredThemeMode.AUTOMATIC -> ThemeMode.AUTOMATIC
    }

fun ThemeMode.toData(): StoredThemeMode =
    when (this) {
        ThemeMode.DAY -> StoredThemeMode.DAY
        ThemeMode.NIGHT -> StoredThemeMode.NIGHT
        ThemeMode.AUTOMATIC -> StoredThemeMode.AUTOMATIC
    }
