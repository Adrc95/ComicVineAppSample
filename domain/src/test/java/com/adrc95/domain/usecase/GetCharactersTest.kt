package com.adrc95.domain.usecase

import com.adrc95.testing.builder.character
import com.adrc95.testing.repository.FakeCharactersRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetCharactersTest {
    @Test
    fun `Given characters for an offset When invoke is called Then the flow emits the characters for that offset`() = runTest {
        // Given
        val characters = listOf(
            character { withName("Spider-Man") },
            character { withId(2L).withName("Iron Man") }
        )
        val repository = FakeCharactersRepository().apply {
            setCharacters(offset = 20, items = characters)
        }
        val useCase = GetCharacters(repository)

        // When
        val result = useCase(20).first()

        // Then
        assertEquals(characters, result)
        assertEquals(listOf(20), repository.requestedOffsets)
    }

}
