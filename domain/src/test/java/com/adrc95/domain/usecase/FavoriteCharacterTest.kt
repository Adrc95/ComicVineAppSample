package com.adrc95.domain.usecase

import arrow.core.left
import arrow.core.right
import com.adrc95.domain.exception.Failure
import com.adrc95.testing.repository.FakeCharactersRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class FavoriteCharacterTest {
    @Test
    fun `Given a successful repository update When invoke is called Then favorite state is updated`() = runTest {
        // Given
        val repository = FakeCharactersRepository().apply {
            setUpdateFavoriteCharacterResult(Unit.right())
        }
        val useCase = FavoriteCharacter(repository)

        // When
        val result = useCase(id = 7L, isFavorite = true)

        // Then
        assertEquals(Unit.right(), result)
        assertEquals(listOf(7L to true), repository.favoriteUpdates)
    }

    @Test
    fun `Given a repository failure When invoke is called Then the failure is returned`() = runTest {
        // Given
        val failure = Failure.Connectivity
        val repository = FakeCharactersRepository().apply {
            setUpdateFavoriteCharacterResult(failure.left())
        }
        val useCase = FavoriteCharacter(repository)

        // When
        val result = useCase(id = 9L, isFavorite = false)

        // Then
        assertEquals(failure.left(), result)
        assertEquals(listOf(9L to false), repository.favoriteUpdates)
    }
}
