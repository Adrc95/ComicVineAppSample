package com.adrc95.data.datasource

import com.adrc95.domain.model.ThemeMode
import kotlinx.coroutines.flow.Flow

interface LocalConfigurationDataSource {
    fun getThemeMode(): Flow<ThemeMode>

    suspend fun setThemeMode(mode: ThemeMode)
}
