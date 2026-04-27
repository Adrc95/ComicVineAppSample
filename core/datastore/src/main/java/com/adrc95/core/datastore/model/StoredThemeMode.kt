package com.adrc95.core.datastore.model

@Suppress("MagicNumber")
enum class StoredThemeMode(val value: Int) {
    DAY(1),
    NIGHT(2),
    AUTOMATIC(3);

    companion object {
        fun fromValue(value: Int): StoredThemeMode =
            when (value) {
                1 -> DAY
                2 -> NIGHT
                else -> AUTOMATIC
            }
    }
}
