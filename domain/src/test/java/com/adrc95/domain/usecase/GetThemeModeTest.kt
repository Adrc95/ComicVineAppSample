package com.adrc95.domain.usecase

import com.adrc95.domain.model.ThemeMode
import com.adrc95.testing.repository.FakeConfigurationRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetThemeModeTest {
    @Test
    fun `Given a stored theme mode When invoke is called Then the current mode flow is returned`() = runTest {
        // Given
        val repository = FakeConfigurationRepository().apply {
            themeModeState.value = ThemeMode.DAY
        }
        val useCase = GetThemeMode(repository)

        // When
        val result = useCase().first()

        // Then
        assertEquals(ThemeMode.DAY, result)
    }

    @Test
    fun `Given multiple theme changes When invoke is called Then the flow reflects the latest mode each time`() = runTest {
        // Given
        val repository = FakeConfigurationRepository()
        val useCase = GetThemeMode(repository)

        // When
        val initialMode = useCase().first()
        repository.setThemeMode(ThemeMode.DAY)
        val dayMode = useCase().first()
        repository.setThemeMode(ThemeMode.NIGHT)
        val nightMode = useCase().first()

        // Then
        assertEquals(ThemeMode.AUTOMATIC, initialMode)
        assertEquals(ThemeMode.DAY, dayMode)
        assertEquals(ThemeMode.NIGHT, nightMode)
    }
}
