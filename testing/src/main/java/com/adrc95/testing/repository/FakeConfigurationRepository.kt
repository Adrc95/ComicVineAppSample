package com.adrc95.testing.repository

import com.adrc95.domain.model.ThemeMode
import com.adrc95.domain.repository.ConfigurationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeConfigurationRepository : ConfigurationRepository {
    val themeModeState = MutableStateFlow(ThemeMode.AUTOMATIC)
    val receivedThemeModes = mutableListOf<ThemeMode>()

    override fun getThemeMode(): Flow<ThemeMode> = themeModeState

    override suspend fun setThemeMode(mode: ThemeMode) {
        receivedThemeModes += mode
        themeModeState.value = mode
    }
}
