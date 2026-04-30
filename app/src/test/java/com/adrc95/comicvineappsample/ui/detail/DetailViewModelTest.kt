package com.adrc95.comicvineappsample.ui.detail

import app.cash.turbine.test
import com.adrc95.comicvineappsample.ui.common.MainDispatcherRule
import com.adrc95.comicvineappsample.ui.mapper.toDisplayModel
import com.adrc95.domain.exception.Failure
import com.adrc95.domain.usecase.FavoriteCharacter
import com.adrc95.domain.usecase.GetCharacter
import com.adrc95.testing.builder.character
import com.adrc95.testing.repository.FakeCharactersRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import arrow.core.left
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DetailViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private fun createViewModel(
        repository: FakeCharactersRepository,
        id: Long,
    ): DetailViewModel = DetailViewModel(
        getCharacter = GetCharacter(repository),
        characterId = id,
        favoriteCharacter = FavoriteCharacter(repository)
    )

    @Test
    fun `given existing character when ui state is collected then emits character detail state`() =
        runTest(mainDispatcherRule.scheduler) {
            // Given
            val character = character()
            val repository = FakeCharactersRepository().apply {
                setCharacter(character)
            }
            val viewModel = createViewModel(repository, id = character.id)
            val expectedState = DetailViewModel.DetailUiState(
                loading = false,
                character = character.toDisplayModel()
            )

            // When
            viewModel.uiState.test {
                val firstState = awaitItem()

                // Then
                val loadedState = if (firstState.character == null) awaitItem() else firstState
                assertEquals(expectedState, loadedState)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `given favorite character when favorite action is clicked then emits updated non favorite state`() =
        runTest(mainDispatcherRule.scheduler) {
            // Given
            val character = character { withFavorite(true) }
            val repository = FakeCharactersRepository().apply {
                setCharacter(character)
            }
            val viewModel = createViewModel(repository, id = character.id)
            val expectedState = DetailViewModel.DetailUiState(
                loading = false,
                character = character.copy(favorite = false).toDisplayModel()
            )

            // When
            viewModel.uiState.test {
                val firstState = awaitItem()
                if (firstState.character == null) {
                    awaitItem()
                }
                viewModel.onFavoriteActionClicked()

                // Then
                assertEquals(expectedState, awaitItem())
                assertEquals(listOf(character.id to false), repository.favoriteUpdates)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `given non favorite character when favorite action is clicked then emits updated favorite state`() =
        runTest(mainDispatcherRule.scheduler) {
            // Given
            val character = character { withFavorite(false) }
            val repository = FakeCharactersRepository().apply {
                setCharacter(character)
            }
            val viewModel = createViewModel(repository, id = character.id)
            val expectedState = DetailViewModel.DetailUiState(
                loading = false,
                character = character.copy(favorite = true).toDisplayModel()
            )

            // When
            viewModel.uiState.test {
                val firstState = awaitItem()
                if (firstState.character == null) {
                    awaitItem()
                }
                viewModel.onFavoriteActionClicked()

                // Then
                assertEquals(expectedState, awaitItem())
                assertEquals(listOf(character.id to true), repository.favoriteUpdates)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `given favorite character when favorite action fails then keeps current state`() =
        runTest(mainDispatcherRule.scheduler) {
            // Given
            val character = character { withFavorite(true) }
            val repository = FakeCharactersRepository().apply {
                setCharacter(character)
                setUpdateFavoriteCharacterResult(Failure.Unknown("error").left())
            }
            val viewModel = createViewModel(repository, id = character.id)
            val expectedState = DetailViewModel.DetailUiState(
                loading = false,
                character = character.toDisplayModel()
            )

            // When
            viewModel.uiState.test {
                val firstState = awaitItem()
                val loadedState = if (firstState.character == null) awaitItem() else firstState
                viewModel.onFavoriteActionClicked()

                // Then
                assertEquals(expectedState, loadedState)
                assertEquals(listOf(character.id to false), repository.favoriteUpdates)
                expectNoEvents()
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `given missing character when favorite action is clicked then does not request favorite update`() =
        runTest(mainDispatcherRule.scheduler) {
            // Given
            val repository = FakeCharactersRepository()
            val viewModel = createViewModel(repository, id = 1L)

            // When
            viewModel.uiState.test {
                assertEquals(DetailViewModel.DetailUiState(), awaitItem())
                viewModel.onFavoriteActionClicked()

                // Then
                assertEquals(emptyList<Pair<Long, Boolean>>(), repository.favoriteUpdates)
                cancelAndIgnoreRemainingEvents()
            }
        }
}
