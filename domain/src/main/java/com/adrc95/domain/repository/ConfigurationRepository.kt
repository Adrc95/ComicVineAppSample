package com.adrc95.domain.repository

import com.adrc95.domain.model.ThemeMode
import kotlinx.coroutines.flow.Flow

interface ConfigurationRepository {
    fun getThemeMode(): Flow<ThemeMode>
    suspend fun setThemeMode(mode: ThemeMode)
}
