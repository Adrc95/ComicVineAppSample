package com.adrc95.domain.usecase

import com.adrc95.testing.builder.character
import org.junit.Assert.assertEquals
import org.junit.Test

class FilterCharactersTest {
    @Test
    fun `Given an empty query When invoke is called Then all characters are returned`() {
        // Given
        val characters = listOf(
            character { withName("Spider-Man") },
            character { withId(2L).withName("Iron Man") }
        )
        val useCase = FilterCharacters()

        // When
        val result = useCase(characters, " ")

        // Then
        assertEquals(characters, result)
    }

    @Test
    fun `Given a query When invoke is called Then characters are filtered by name ignoring case`() {
        // Given
        val spiderMan = character { withName("Spider-Man") }
        val ironMan = character { withId(2L).withName("Iron Man") }
        val characters = listOf(spiderMan, ironMan)
        val useCase = FilterCharacters()

        // When
        val result = useCase(characters, "spider")

        // Then
        assertEquals(listOf(spiderMan), result)
    }
}
