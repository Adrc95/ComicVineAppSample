package com.adrc95.comicvineappsample.repository

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.adrc95.comicvineappsample.builder.characterEntity
import com.adrc95.comicvineappsample.mockwebserver.MockWebServerUrlHolder
import com.adrc95.comicvineappsample.mockwebserver.rules.MockWebServerRule
import com.adrc95.core.database.AppDatabase
import com.adrc95.domain.exception.Failure
import com.adrc95.domain.repository.CharactersRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class CharactersRepositoryIntegrationTest {

    @get:Rule(order = 0)
    val mockWebServerRule = MockWebServerRule()

    @get:Rule(order = 1)
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repository: CharactersRepository

    @Inject
    lateinit var database: AppDatabase

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @After
    fun tearDown() {
        MockWebServerUrlHolder.baseUrl = "http://localhost:8080/"
    }

    @Test
    fun getCharacter_whenStoredLocally_returnsStoredCharacter() = runTest {
        // Given
        database.characterDao().insert(
            characterEntity {
                withLongDescription("Detailed text for stored mapping")
                withThumbnail("https://example.com/spiderman-original.png")
            }
        )

        // When
        val character = repository.getCharacter(1L).filterNotNull().first()

        // Then
        assertEquals(1L, character.id)
        assertEquals("Spider-Man", character.name)
        assertEquals("Detailed text for stored mapping", character.longDescription)
    }

    @Test
    fun getFavoriteCharacters_whenFavoriteStoredLocally_returnsFavoriteCharacters() = runTest {
        // Given
        database.characterDao().insertAll(
            listOf(
                characterEntity {
                    withId(1L)
                    withName("Spider-Man")
                    withFavorite(true)
                },
                characterEntity {
                    withId(2L)
                    withName("Batman")
                    withFavorite(false)
                }
            )
        )

        // When
        val favoriteCharacters = repository.getFavoriteCharacters().first()

        // Then
        assertEquals(1, favoriteCharacters.size)
        assertEquals(1L, favoriteCharacters.first().id)
        assertEquals("Spider-Man", favoriteCharacters.first().name)
        assertEquals(true, favoriteCharacters.first().favorite)
    }

    @Test
    fun updateFavoriteCharacter_whenStoredLocally_updatesFavoriteFlag() = runTest {
        // Given
        database.characterDao().insert(
            characterEntity {
                withId(1L)
                withFavorite(false)
            }
        )

        // When
        val result = repository.updateFavoriteCharacter(id = 1L, favorite = true)
        val updatedCharacter = database.characterDao().findCharacterById(1L)

        // Then
        assertEquals(Unit, result.getOrNull())
        assertEquals(true, updatedCharacter?.favorite)
    }

    @Test
    fun updateFavoriteCharacter_whenCharacterIsMissing_returnsFailure() = runTest {
        // When
        val result = repository.updateFavoriteCharacter(id = 99L, favorite = true)
        val updatedCharacter = database.characterDao().findCharacterById(99L)

        // Then
        assertEquals(
            Failure.Unknown("Character with id 99 not found"),
            result.swap().getOrNull()
        )
        assertNull(updatedCharacter)
    }

    private fun readJsonAsset(fileName: String): String =
        InstrumentationRegistry.getInstrumentation()
            .context
            .assets
            .open(fileName)
            .bufferedReader()
            .use { it.readText() }
}
