package com.adrc95.testing.builder

import com.adrc95.domain.model.ThemeMode

class ThemeModeBuilder {
    var value: ThemeMode = ThemeMode.AUTOMATIC

    fun withValue(value: ThemeMode) = apply { this.value = value }

    fun build(): ThemeMode = value
}

fun themeMode(block: ThemeModeBuilder.() -> Unit = {}) = ThemeModeBuilder().apply(block).build()
