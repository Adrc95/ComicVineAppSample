package com.adrc95.comicvineappsample.ui.mapper

import com.adrc95.comicvineappsample.ui.common.ModeTypeDisplayModel
import com.adrc95.domain.model.ThemeMode
import org.junit.Assert.assertEquals
import org.junit.Test

class ThemeModeUiMapperTest {

    @Test
    fun `given day theme mode when mapped then returns day display mode`() {
        // Given
        val themeMode = ThemeMode.DAY

        // When
        val result = themeMode.toDisplayModel()

        // Then
        assertEquals(ModeTypeDisplayModel.DAY, result)
    }

    @Test
    fun `given night theme mode when mapped then returns night display mode`() {
        // Given
        val themeMode = ThemeMode.NIGHT

        // When
        val result = themeMode.toDisplayModel()

        // Then
        assertEquals(ModeTypeDisplayModel.NIGHT, result)
    }

    @Test
    fun `given automatic theme mode when mapped then returns automatic display mode`() {
        // Given
        val themeMode = ThemeMode.AUTOMATIC

        // When
        val result = themeMode.toDisplayModel()

        // Then
        assertEquals(ModeTypeDisplayModel.AUTOMATIC, result)
    }

    @Test
    fun `given day display mode when mapped then returns day domain mode`() {
        // Given
        val displayMode = ModeTypeDisplayModel.DAY

        // When
        val result = displayMode.toDomain()

        // Then
        assertEquals(ThemeMode.DAY, result)
    }

    @Test
    fun `given night display mode when mapped then returns night domain mode`() {
        // Given
        val displayMode = ModeTypeDisplayModel.NIGHT

        // When
        val result = displayMode.toDomain()

        // Then
        assertEquals(ThemeMode.NIGHT, result)
    }

    @Test
    fun `given automatic display mode when mapped then returns automatic domain mode`() {
        // Given
        val displayMode = ModeTypeDisplayModel.AUTOMATIC

        // When
        val result = displayMode.toDomain()

        // Then
        assertEquals(ThemeMode.AUTOMATIC, result)
    }
}
