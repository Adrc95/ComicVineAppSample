package com.adrc95.domain.usecase

import arrow.core.left
import arrow.core.right
import com.adrc95.domain.exception.Failure
import com.adrc95.testing.repository.FakeCharactersRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class RefreshCharactersTest {
    @Test
    fun `Given a successful refresh When invoke is called Then repository refresh is requested for the offset`() = runTest {
        // Given
        val repository = FakeCharactersRepository().apply {
            setRefreshCharactersResult(Unit.right())
        }
        val useCase = RefreshCharacters(repository)

        // When
        val result = useCase(40)

        // Then
        assertEquals(Unit.right(), result)
        assertEquals(listOf(40), repository.refreshedOffsets)
    }

    @Test
    fun `Given a failed refresh When invoke is called Then the failure is returned`() = runTest {
        // Given
        val failure = Failure.Server(500)
        val repository = FakeCharactersRepository().apply {
            setRefreshCharactersResult(failure.left())
        }
        val useCase = RefreshCharacters(repository)

        // When
        val result = useCase(60)

        // Then
        assertEquals(failure.left(), result)
        assertEquals(listOf(60), repository.refreshedOffsets)
    }
}
