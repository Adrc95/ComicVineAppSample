package com.adrc95.comicvineappsample.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.adrc95.domain.model.ThemeMode
import com.adrc95.domain.repository.ConfigurationRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ConfigurationRepositoryIntegrationTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repository: ConfigurationRepository

    @Inject
    lateinit var dataStore: DataStore<Preferences>

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @After
    fun tearDown() {
        runBlocking {
            dataStore.edit { preferences -> preferences.clear() }
        }
    }

    @Test
    fun getThemeMode_whenNoPreferenceIsStored_returnsAutomatic() = runTest {
        // When
        val themeMode = repository.getThemeMode().first()

        // Then
        assertEquals(ThemeMode.AUTOMATIC, themeMode)
    }

    @Test
    fun setThemeMode_whenModeChanges_persistsNewValue() = runTest {
        // When
        repository.setThemeMode(ThemeMode.NIGHT)
        val themeMode = repository.getThemeMode().first()

        // Then
        assertEquals(ThemeMode.NIGHT, themeMode)
    }
}
