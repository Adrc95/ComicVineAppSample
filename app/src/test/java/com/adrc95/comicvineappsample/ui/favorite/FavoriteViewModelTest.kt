package com.adrc95.comicvineappsample.ui.favorite

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import app.cash.turbine.test
import com.adrc95.comicvineappsample.ui.common.MainDispatcherRule
import com.adrc95.comicvineappsample.ui.mapper.toDisplayModel
import com.adrc95.domain.exception.Failure
import com.adrc95.domain.model.Character
import com.adrc95.domain.repository.CharactersRepository
import com.adrc95.domain.usecase.FavoriteCharacter
import com.adrc95.domain.usecase.GetFavoriteCharacters
import com.adrc95.testing.builder.character
import com.adrc95.testing.repository.FakeCharactersRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FavoriteViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private fun createViewModel(
        repository: FakeCharactersRepository
    ): FavoriteViewModel = FavoriteViewModel(
        getFavoriteCharacters = GetFavoriteCharacters(repository),
        favoriteCharacters = FavoriteCharacter(repository)
    )

    private fun createViewModel(
        repository: CharactersRepository
    ): FavoriteViewModel = FavoriteViewModel(
        getFavoriteCharacters = GetFavoriteCharacters(repository),
        favoriteCharacters = FavoriteCharacter(repository)
    )

    @Test
    fun `given favorite characters when ui state is collected then emits loaded favorites state`() =
        runTest(mainDispatcherRule.scheduler) {
            // Given
            val favorites = listOf(
                character { withFavorite(true) },
                character { withId(2L).withName("Iron Man").withFavorite(true) }
            )
            val repository = FakeCharactersRepository().apply {
                setFavoriteCharacters(favorites)
            }
            val viewModel = createViewModel(repository)
            val expectedState = FavoriteViewModel.FavoriteUiState(
                loading = false,
                characters = favorites.map { it.toDisplayModel() },
                emptyFavorites = false,
                serverError = false
            )

            // When
            viewModel.uiState.test {
                val firstState = awaitItem()

                // Then
                val loadedState = if (firstState.characters == null) awaitItem() else firstState
                assertEquals(expectedState, loadedState)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `given no favorite characters when ui state is collected then emits empty favorites state`() =
        runTest(mainDispatcherRule.scheduler) {
            // Given
            val repository = FakeCharactersRepository()
            val viewModel = createViewModel(repository)
            val expectedState = FavoriteViewModel.FavoriteUiState(
                loading = false,
                characters = emptyList(),
                emptyFavorites = true,
                serverError = false
            )

            // When
            viewModel.uiState.test {
                val firstState = awaitItem()

                // Then
                val loadedState = if (firstState.characters == null) awaitItem() else firstState
                assertEquals(expectedState, loadedState)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `given favorite character when delete is clicked then emits updated favorites state`() =
        runTest(mainDispatcherRule.scheduler) {
            // Given
            val favoriteCharacter = character { withId(1L).withFavorite(true) }
            val repository = FakeCharactersRepository().apply {
                setCharacter(favoriteCharacter)
                setFavoriteCharacters(listOf(favoriteCharacter))
            }
            val viewModel = createViewModel(repository)
            val expectedState = FavoriteViewModel.FavoriteUiState(
                loading = false,
                characters = emptyList(),
                emptyFavorites = true,
                serverError = false
            )

            // When
            viewModel.uiState.test {
                val firstState = awaitItem()
                if (firstState.characters == null) {
                    awaitItem()
                }
                viewModel.onCharacterDelete(id = favoriteCharacter.id)

                // Then
                assertEquals(expectedState, awaitItem())
                assertEquals(listOf(favoriteCharacter.id to false), repository.favoriteUpdates)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `given favorite character when delete fails then keeps current favorites state`() =
        runTest(mainDispatcherRule.scheduler) {
            // Given
            val favoriteCharacter = character { withId(1L).withFavorite(true) }
            val repository = FakeCharactersRepository().apply {
                setCharacter(favoriteCharacter)
                setFavoriteCharacters(listOf(favoriteCharacter))
                setUpdateFavoriteCharacterResult(Failure.Unknown("error").left())
            }
            val viewModel = createViewModel(repository)
            val expectedState = FavoriteViewModel.FavoriteUiState(
                loading = false,
                characters = listOf(favoriteCharacter.toDisplayModel()),
                emptyFavorites = false,
                serverError = false
            )

            // When
            viewModel.uiState.test {
                val firstState = awaitItem()
                val loadedState = if (firstState.characters == null) awaitItem() else firstState
                viewModel.onCharacterDelete(id = favoriteCharacter.id)

                // Then
                assertEquals(expectedState, loadedState)
                assertEquals(listOf(favoriteCharacter.id to false), repository.favoriteUpdates)
                expectNoEvents()
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `given favorite characters flow failure when ui state is collected then emits error state`() =
        runTest(mainDispatcherRule.scheduler) {
            // Given
            val repository = FailingFavoriteCharactersRepository()
            val viewModel = createViewModel(repository)
            val expectedState = FavoriteViewModel.FavoriteUiState(
                loading = false,
                characters = null,
                emptyFavorites = false,
                serverError = true
            )

            // When
            viewModel.uiState.test {
                val firstState = awaitItem()

                // Then
                val errorState = if (!firstState.serverError) awaitItem() else firstState
                assertEquals(expectedState, errorState)
                cancelAndIgnoreRemainingEvents()
            }
        }

    private class FailingFavoriteCharactersRepository : CharactersRepository {
        override fun getCharacters(offset: Int): Flow<List<Character>> = flow { emit(emptyList()) }

        override suspend fun refreshCharacters(offset: Int): Either<Failure, Unit> = Unit.right()

        override fun getCharacter(id: Long): Flow<Character?> = flow { emit(null) }

        override fun getFavoriteCharacters(): Flow<List<Character>> = flow {
            throw IllegalStateException("error")
        }

        override suspend fun updateFavoriteCharacter(
            id: Long,
            favorite: Boolean
        ): Either<Failure, Unit> = Unit.right()
    }
}
