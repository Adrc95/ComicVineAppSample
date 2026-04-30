package com.adrc95.core.database.mapper

import com.adrc95.core.database.entity.CharacterEntity
import com.adrc95.testing.builder.character
import org.junit.Assert.assertEquals
import org.junit.Test

class CharacterEntityMapperTest {

    @Test
    fun `given character entity when mapped to domain then returns character`() {
        // Given
        val entity = CharacterEntity(
            id = 1L,
            name = "Spider-Man",
            shortDescription = "Friendly neighborhood hero.",
            longDescription = "Detailed description.",
            uri = "https://comicvine.gamespot.com/spider-man/4005-1443/",
            thumbnail = "https://example.com/spiderman.png",
            favorite = true
        )
        val expectedCharacter = character {
            withId(1L)
            withName("Spider-Man")
            withShortDescription("Friendly neighborhood hero.")
            withLongDescription("Detailed description.")
            withUri("https://comicvine.gamespot.com/spider-man/4005-1443/")
            withThumbnail("https://example.com/spiderman.png")
            withFavorite(true)
        }

        // When
        val result = entity.toDomain()

        // Then
        assertEquals(expectedCharacter, result)
    }

    @Test
    fun `given character when mapped to entity then returns character entity`() {
        // Given
        val character = character {
            withId(7L)
            withName("Batman")
            withShortDescription("Dark Knight")
            withLongDescription("Protector of Gotham.")
            withUri("https://comicvine.gamespot.com/batman/4005-1699/")
            withThumbnail("https://example.com/batman.png")
            withFavorite(false)
        }
        val expectedEntity = CharacterEntity(
            id = 7L,
            name = "Batman",
            shortDescription = "Dark Knight",
            longDescription = "Protector of Gotham.",
            uri = "https://comicvine.gamespot.com/batman/4005-1699/",
            thumbnail = "https://example.com/batman.png",
            favorite = false
        )

        // When
        val result = character.toEntity()

        // Then
        assertEquals(expectedEntity, result)
    }

    @Test
    fun `given character entity with null values when mapped to domain then returns default favorite and description`() {
        // Given
        val entity = CharacterEntity(
            id = 2L,
            name = "Unknown",
            shortDescription = null,
            longDescription = null,
            uri = "https://example.com/unknown",
            thumbnail = "https://example.com/unknown.png",
            favorite = null
        )
        val expectedCharacter = character {
            withId(2L)
            withName("Unknown")
            withShortDescription("")
            withLongDescription(null)
            withUri("https://example.com/unknown")
            withThumbnail("https://example.com/unknown.png")
            withFavorite(false)
        }

        // When
        val result = entity.toDomain()

        // Then
        assertEquals(expectedCharacter, result)
    }
}
