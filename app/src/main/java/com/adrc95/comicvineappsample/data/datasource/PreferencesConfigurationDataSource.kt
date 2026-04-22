package com.adrc95.comicvineappsample.data.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.adrc95.comicvineappsample.data.util.Constants.PREF_DARK_MODE
import com.adrc95.data.datasource.LocalConfigurationDataSource
import com.adrc95.data.model.StoredThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferencesConfigurationDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : LocalConfigurationDataSource {

    private val darkModeKey = intPreferencesKey(PREF_DARK_MODE)

    override fun getThemeMode(): Flow<StoredThemeMode> = dataStore.data.map { preferences ->
        StoredThemeMode.fromValue(preferences[darkModeKey] ?: StoredThemeMode.AUTOMATIC.value)
    }

    override suspend fun setThemeMode(mode: StoredThemeMode) {
        dataStore.edit { preferences -> preferences[darkModeKey] = mode.value }
    }
}
