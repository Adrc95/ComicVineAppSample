package com.adrc95.core.datastore.datasource

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.adrc95.core.datastore.DatastoreConstants.PREF_DARK_MODE
import com.adrc95.domain.model.ThemeMode
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File
import kotlin.io.path.createTempDirectory

@OptIn(ExperimentalCoroutinesApi::class)
class PreferencesConfigurationDataSourceTest {

    @Test
    fun `given empty preferences when get theme mode then returns automatic`() = runTest {
        // Given
        val dataStore = createDataStore(this)
        val dataSource = PreferencesConfigurationDataSource(dataStore)

        // When
        val result = dataSource.getThemeMode().first()

        // Then
        assertEquals(ThemeMode.AUTOMATIC, result)
    }

    @Test
    fun `given day theme mode when set theme mode then stores and returns day`() = runTest {
        // Given
        val dataStore = createDataStore(this)
        val dataSource = PreferencesConfigurationDataSource(dataStore)

        // When
        dataSource.setThemeMode(ThemeMode.DAY)
        val result = dataSource.getThemeMode().first()

        // Then
        assertEquals(ThemeMode.DAY, result)
    }

    @Test
    fun `given invalid stored value when get theme mode then returns automatic`() = runTest {
        // Given
        val dataStore = createDataStore(this)
        val darkModeKey = intPreferencesKey(PREF_DARK_MODE)
        dataStore.edit { preferences ->
            preferences[darkModeKey] = 99
        }
        val dataSource = PreferencesConfigurationDataSource(dataStore)

        // When
        val result = dataSource.getThemeMode().first()

        // Then
        assertEquals(ThemeMode.AUTOMATIC, result)
    }

    private fun createDataStore(scope: TestScope) = PreferenceDataStoreFactory.create(
        scope = scope.backgroundScope,
        produceFile = { createTempPreferencesFile() }
    )

    private fun createTempPreferencesFile(): File =
        createTempDirectory("preferences-test").toFile().resolve("settings.preferences_pb").apply {
            parentFile?.deleteOnExit()
            deleteOnExit()
        }
}
