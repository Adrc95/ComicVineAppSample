package com.adrc95.domain.usecase

import com.adrc95.domain.model.ThemeMode
import com.adrc95.testing.repository.FakeConfigurationRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class ChangeThemeModeTest {
    @Test
    fun `Given a theme mode When invoke is called Then repository stores the new mode`() = runTest {
        // Given
        val repository = FakeConfigurationRepository()
        val useCase = ChangeThemeMode(repository)
        val mode = ThemeMode.NIGHT

        // When
        useCase(mode)

        // Then
        assertEquals(listOf(mode), repository.receivedThemeModes)
        assertEquals(mode, repository.themeModeState.value)
    }
}
