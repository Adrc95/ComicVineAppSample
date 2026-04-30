package com.adrc95.domain.usecase

import com.adrc95.testing.builder.character
import com.adrc95.testing.repository.FakeCharactersRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetCharacterTest {
    @Test
    fun `Given a cached character When invoke is called Then the matching character flow is returned`() = runTest {
        // Given
        val expectedCharacter = character { withId(15L).withName("Black Panther") }
        val repository = FakeCharactersRepository().apply {
            setCharacter(expectedCharacter)
        }
        val useCase = GetCharacter(repository)

        // When
        val result = useCase(15L).first()

        // Then
        assertEquals(expectedCharacter, result)
        assertEquals(listOf(15L), repository.requestedCharacterIds)
    }

    @Test
    fun `Given no cached character When invoke is called Then the flow emits null`() = runTest {
        // Given
        val repository = FakeCharactersRepository()
        val useCase = GetCharacter(repository)

        // When
        val result = useCase(99L).first()

        // Then
        assertEquals(null, result)
        assertEquals(listOf(99L), repository.requestedCharacterIds)
    }
}
