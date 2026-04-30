package com.adrc95.comicvineappsample.ui.main

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import app.cash.turbine.test
import com.adrc95.comicvineappsample.ui.common.MainDispatcherRule
import com.adrc95.comicvineappsample.ui.mapper.toDisplayModel
import com.adrc95.domain.exception.Failure
import com.adrc95.domain.model.Character
import com.adrc95.domain.repository.CharactersRepository
import com.adrc95.domain.usecase.FilterCharacters
import com.adrc95.domain.usecase.GetCharacters
import com.adrc95.domain.usecase.RefreshCharacters
import com.adrc95.testing.builder.character
import com.adrc95.testing.repository.FakeCharactersRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private fun createViewModel(
        repository: FakeCharactersRepository
    ): MainViewModel = MainViewModel(
        filterCharacters = FilterCharacters(),
        getCharacters = GetCharacters(repository),
        refreshCharactersUseCase = RefreshCharacters(repository)
    )

    private fun createViewModel(
        repository: CharactersRepository
    ): MainViewModel = MainViewModel(
        filterCharacters = FilterCharacters(),
        getCharacters = GetCharacters(repository),
        refreshCharactersUseCase = RefreshCharacters(repository)
    )

    @Test
    fun `given stored characters when ui state is collected then emits loaded state`() =
        runTest(mainDispatcherRule.scheduler) {
            // Given
            val characters = listOf(
                character(),
                character { withId(2L).withName("Iron Man") }
            )
            val repository = FakeCharactersRepository().apply {
                setCharacters(offset = 0, items = characters)
            }
            val viewModel = createViewModel(repository)
            val expectedState = MainViewModel.MainUiState(
                loading = false,
                characters = characters.map { it.toDisplayModel() },
                enabledSearch = true,
                filterQuery = null,
                pagingLoading = false,
                serverError = false,
                emptySearchResults = false
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
    fun `given characters when search text changes then emits filtered state`() =
        runTest(mainDispatcherRule.scheduler) {
            // Given
            val spiderMan = character()
            val ironMan = character { withId(2L).withName("Iron Man") }
            val repository = FakeCharactersRepository().apply {
                setCharacters(offset = 0, items = listOf(spiderMan, ironMan))
            }
            val viewModel = createViewModel(repository)
            val expectedState = MainViewModel.MainUiState(
                loading = false,
                characters = listOf(ironMan.toDisplayModel()),
                enabledSearch = true,
                filterQuery = "iron",
                pagingLoading = false,
                serverError = false,
                emptySearchResults = false
            )

            // When
            viewModel.uiState.test {
                val firstState = awaitItem()
                if (firstState.characters == null) {
                    awaitItem()
                }
                viewModel.onSearchTextChanged("iron")

                // Then
                val filteredState = awaitItem()
                val settledState = if (filteredState != expectedState) awaitItem() else filteredState
                assertEquals(expectedState, settledState)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `given characters when search text has no matches then emits empty search results state`() =
        runTest(mainDispatcherRule.scheduler) {
            // Given
            val repository = FakeCharactersRepository().apply {
                setCharacters(offset = 0, items = listOf(character()))
            }
            val viewModel = createViewModel(repository)
            val expectedState = MainViewModel.MainUiState(
                loading = false,
                characters = emptyList(),
                enabledSearch = true,
                filterQuery = "thor",
                pagingLoading = false,
                serverError = false,
                emptySearchResults = true
            )

            // When
            viewModel.uiState.test {
                val firstState = awaitItem()
                if (firstState.characters == null) {
                    awaitItem()
                }
                viewModel.onSearchTextChanged("thor")

                // Then
                val filteredState = awaitItem()
                val settledState = if (filteredState != expectedState) awaitItem() else filteredState
                assertEquals(expectedState, settledState)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `given loaded characters when fetch more is called then refreshes requested page`() =
        runTest(mainDispatcherRule.scheduler) {
            // Given
            val repository = FakeCharactersRepository().apply {
                setCharacters(offset = 0, items = listOf(character()))
            }
            val viewModel = createViewModel(repository)

            // When
            viewModel.uiState.test {
                val firstState = awaitItem()
                if (firstState.characters == null) {
                    awaitItem()
                }
                viewModel.fetchMoreCharacters(offset = 20)

                // Then
                assertEquals(listOf(20), repository.refreshedOffsets)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `given loading state when fetch more is called then does not refresh next page`() =
        runTest(mainDispatcherRule.scheduler) {
            // Given
            val repository = FakeCharactersRepository()
            val viewModel = createViewModel(repository)

            // When
            viewModel.uiState.test {
                val loadingState = awaitItem()
                viewModel.fetchMoreCharacters(offset = 20)

                // Then
                assertEquals(true, loadingState.loading)
                assertEquals(emptyList<Int>(), repository.refreshedOffsets)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `given active search when fetch more is called then does not refresh next page`() =
        runTest(mainDispatcherRule.scheduler) {
            // Given
            val repository = FakeCharactersRepository().apply {
                setCharacters(offset = 0, items = listOf(character()))
            }
            val viewModel = createViewModel(repository)

            // When
            viewModel.uiState.test {
                val firstState = awaitItem()
                if (firstState.characters == null) {
                    awaitItem()
                }
                viewModel.onSearchTextChanged("spider")
                awaitItem()
                viewModel.fetchMoreCharacters(offset = 20)

                // Then
                assertEquals(emptyList<Int>(), repository.refreshedOffsets)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `given loaded characters when paging refresh fails then keeps server error disabled`() =
        runTest(mainDispatcherRule.scheduler) {
            // Given
            val characters = listOf(character())
            val repository = FakeCharactersRepository().apply {
                setCharacters(offset = 0, items = characters)
                setRefreshCharactersResult(Failure.Unknown("error").left())
            }
            val viewModel = createViewModel(repository)
            val expectedState = MainViewModel.MainUiState(
                loading = false,
                characters = characters.map { it.toDisplayModel() },
                enabledSearch = true,
                filterQuery = null,
                pagingLoading = false,
                serverError = false,
                emptySearchResults = false
            )

            // When
            viewModel.uiState.test {
                val firstState = awaitItem()
                if (firstState.characters == null) {
                    awaitItem()
                }
                viewModel.fetchMoreCharacters(offset = 20)

                // Then
                val pagingState = awaitItem()
                val settledState = if (pagingState == expectedState) pagingState else awaitItem()
                assertEquals(expectedState, settledState)
                assertEquals(listOf(20), repository.refreshedOffsets)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `given paging in progress when fetch more is called then does not refresh another page`() =
        runTest(mainDispatcherRule.scheduler) {
            // Given
            val characters = listOf(character())
            val repository = DelayedRefreshCharactersRepository(characters)
            val viewModel = createViewModel(repository)

            // When
            viewModel.uiState.test {
                val firstState = awaitItem()
                if (firstState.characters == null) {
                    awaitItem()
                }
                viewModel.fetchMoreCharacters(offset = 20)
                awaitItem()
                viewModel.fetchMoreCharacters(offset = 40)

                // Then
                assertEquals(listOf(20), repository.refreshedOffsets)
                repository.completeRefresh(Unit.right())
                awaitItem()
                cancelAndIgnoreRemainingEvents()
            }
        }

    private class DelayedRefreshCharactersRepository(
        private val characters: List<Character>
    ) : CharactersRepository {
        private val refreshResult = CompletableDeferred<Either<Failure, Unit>>()
        val refreshedOffsets = mutableListOf<Int>()

        override fun getCharacters(offset: Int): Flow<List<Character>> = flowOf(characters)

        override suspend fun refreshCharacters(offset: Int): Either<Failure, Unit> {
            refreshedOffsets += offset
            return refreshResult.await()
        }

        fun completeRefresh(result: Either<Failure, Unit>) {
            refreshResult.complete(result)
        }

        override fun getCharacter(id: Long): Flow<Character?> = flowOf(null)

        override fun getFavoriteCharacters(): Flow<List<Character>> = flowOf(emptyList())

        override suspend fun updateFavoriteCharacter(
            id: Long,
            favorite: Boolean
        ): Either<Failure, Unit> = Unit.right()
    }
}
