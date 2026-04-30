package com.adrc95.domain.usecase

import com.adrc95.testing.builder.character
import com.adrc95.testing.repository.FakeCharactersRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetFavoriteCharactersTest {
    @Test
    fun `Given favorite characters When invoke is called Then the favorites flow is returned`() = runTest {
        // Given
        val favorites = listOf(
            character { withId(3L).withName("Hulk").withFavorite(true) },
            character { withId(4L).withName("Thor").withFavorite(true) }
        )
        val repository = FakeCharactersRepository().apply {
            setFavoriteCharacters(favorites)
        }
        val useCase = GetFavoriteCharacters(repository)

        // When
        val result = useCase().first()

        // Then
        assertEquals(favorites, result)
    }

}
