package com.adrc95.core.datastore.mapper

import com.adrc95.core.datastore.model.StoredThemeMode
import com.adrc95.domain.model.ThemeMode
import org.junit.Assert.assertEquals
import org.junit.Test

class ThemeModeMapperTest {

    @Test
    fun `given day stored theme mode when mapped then returns day domain mode`() {
        // Given
        val storedThemeMode = StoredThemeMode.DAY

        // When
        val result = storedThemeMode.toDomain()

        // Then
        assertEquals(ThemeMode.DAY, result)
    }

    @Test
    fun `given night stored theme mode when mapped then returns night domain mode`() {
        // Given
        val storedThemeMode = StoredThemeMode.NIGHT

        // When
        val result = storedThemeMode.toDomain()

        // Then
        assertEquals(ThemeMode.NIGHT, result)
    }

    @Test
    fun `given automatic stored theme mode when mapped then returns automatic domain mode`() {
        // Given
        val storedThemeMode = StoredThemeMode.AUTOMATIC

        // When
        val result = storedThemeMode.toDomain()

        // Then
        assertEquals(ThemeMode.AUTOMATIC, result)
    }

    @Test
    fun `given day domain theme mode when mapped then returns day stored mode`() {
        // Given
        val themeMode = ThemeMode.DAY

        // When
        val result = themeMode.toStoredThemeMode()

        // Then
        assertEquals(StoredThemeMode.DAY, result)
    }

    @Test
    fun `given night domain theme mode when mapped then returns night stored mode`() {
        // Given
        val themeMode = ThemeMode.NIGHT

        // When
        val result = themeMode.toStoredThemeMode()

        // Then
        assertEquals(StoredThemeMode.NIGHT, result)
    }

    @Test
    fun `given automatic domain theme mode when mapped then returns automatic stored mode`() {
        // Given
        val themeMode = ThemeMode.AUTOMATIC

        // When
        val result = themeMode.toStoredThemeMode()

        // Then
        assertEquals(StoredThemeMode.AUTOMATIC, result)
    }
}
