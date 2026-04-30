package com.adrc95.core.database.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.adrc95.core.database.AppDatabase
import com.adrc95.core.database.builder.characterEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CharacterDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var dao: CharacterDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java,
        ).build()
        dao = database.characterDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun given_missing_character_when_find_character_by_id_then_returns_null() = runTest {
        // When
        val result = dao.findCharacterById(id = 1L)

        // Then
        assertNull(result)
    }

    @Test
    fun given_stored_character_when_find_character_by_id_then_returns_character() = runTest {
        // Given
        val entity = characterEntity {
            withId(0L)
        }

        // When
        val insertedId = dao.insert(entity)
        val result = dao.findCharacterById(id = insertedId)

        // Then
        assertEquals(entity.copy(id = insertedId), result)
    }

    @Test
    fun given_stored_characters_when_load_all_characters_then_returns_sorted_characters() = runTest {
        // Given
        val spiderMan = characterEntity {
            withId(2L)
            withName("Spider-Man")
        }
        val batman = characterEntity {
            withId(1L)
            withName("Batman")
        }

        // When
        dao.insertAll(listOf(spiderMan, batman))
        val result = dao.loadAllCharacters().first()

        // Then
        assertEquals(listOf(batman, spiderMan), result)
    }

    @Test
    fun given_favorite_and_non_favorite_characters_when_load_all_favorite_characters_then_returns_only_favorites() = runTest {
        // Given
        val favorite = characterEntity {
            withId(1L)
            withName("Batman")
            withFavorite(true)
        }
        val nonFavorite = characterEntity {
            withId(2L)
            withName("Spider-Man")
            withFavorite(false)
        }

        // When
        dao.insertAll(listOf(nonFavorite, favorite))
        val result = dao.loadAllFavoriteCharacters().first()

        // Then
        assertEquals(listOf(favorite), result)
    }

    @Test
    fun given_stored_characters_when_search_character_by_name_then_returns_matching_characters() = runTest {
        // Given
        val spiderMan = characterEntity {
            withId(1L)
            withName("Spider-Man")
        }
        val ironMan = characterEntity {
            withId(2L)
            withName("Iron Man")
        }

        // When
        dao.insertAll(listOf(spiderMan, ironMan))
        val result = dao.searchCharacterByName(name = "Man")

        // Then
        assertEquals(listOf(ironMan, spiderMan), result)
    }

    @Test
    fun given_stored_characters_when_count_characters_then_returns_total_count() = runTest {
        // Given
        dao.insertAll(
            listOf(
                characterEntity {
                    withId(1L)
                    withName("Batman")
                },
                characterEntity {
                    withId(2L)
                    withName("Spider-Man")
                },
            )
        )

        // When
        val result = dao.countCharacters()

        // Then
        assertEquals(2, result)
    }

    @Test
    fun given_existing_character_when_update_favorite_character_then_updates_stored_value() = runTest {
        // Given
        val entity = characterEntity {
            withId(1L)
            withName("Batman")
            withFavorite(false)
        }
        dao.insert(entity)

        // When
        val updatedRows = dao.updateFavoriteCharacter(id = 1L, favorite = true)
        val result = dao.getCharacterById(id = 1L).first()

        // Then
        assertEquals(1, updatedRows)
        assertEquals(entity.copy(favorite = true), result)
    }
}
