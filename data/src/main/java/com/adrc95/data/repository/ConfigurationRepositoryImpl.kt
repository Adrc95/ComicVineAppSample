package com.adrc95.data.repository

import com.adrc95.data.datasource.LocalConfigurationDataSource
import com.adrc95.domain.model.ThemeMode
import com.adrc95.domain.repository.ConfigurationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ConfigurationRepositoryImpl @Inject constructor(
    private val localConfigurationDataSource: LocalConfigurationDataSource
) : ConfigurationRepository {

    override fun getThemeMode(): Flow<ThemeMode> =
        localConfigurationDataSource.getThemeMode()

    override suspend fun setThemeMode(mode: ThemeMode) {
        localConfigurationDataSource.setThemeMode(mode)
    }
}
