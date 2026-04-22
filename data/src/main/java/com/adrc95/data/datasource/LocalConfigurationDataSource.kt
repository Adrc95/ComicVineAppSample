package com.adrc95.data.datasource

import com.adrc95.data.model.StoredThemeMode
import kotlinx.coroutines.flow.Flow

interface LocalConfigurationDataSource {
    fun getThemeMode(): Flow<StoredThemeMode>

    suspend fun setThemeMode(mode : StoredThemeMode)
}
