package com.adrc95.core.datastore.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.adrc95.core.datastore.DatastoreConstants.PREF_DARK_MODE
import com.adrc95.core.datastore.mapper.toDomain
import com.adrc95.core.datastore.mapper.toStoredThemeMode
import com.adrc95.core.datastore.model.StoredThemeMode
import com.adrc95.data.datasource.LocalConfigurationDataSource
import com.adrc95.domain.model.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferencesConfigurationDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : LocalConfigurationDataSource {

    private val darkModeKey = intPreferencesKey(PREF_DARK_MODE)

    override fun getThemeMode(): Flow<ThemeMode> = dataStore.data.map { preferences ->
        StoredThemeMode.fromValue(preferences[darkModeKey] ?: StoredThemeMode.AUTOMATIC.value).toDomain()
    }

    override suspend fun setThemeMode(mode: ThemeMode) {
        dataStore.edit { preferences -> preferences[darkModeKey] = mode.toStoredThemeMode().value }
    }
}
