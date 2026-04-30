package com.adrc95.comicvineappsample.ui.navhost

import app.cash.turbine.test
import com.adrc95.comicvineappsample.ui.common.MainDispatcherRule
import com.adrc95.comicvineappsample.ui.common.ModeTypeDisplayModel
import com.adrc95.domain.model.ThemeMode
import com.adrc95.domain.usecase.ChangeThemeMode
import com.adrc95.domain.usecase.GetThemeMode
import com.adrc95.testing.repository.FakeConfigurationRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NavHostViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private fun createViewModel(
        repository: FakeConfigurationRepository
    ): NavHostViewModel = NavHostViewModel(
        changeThemeMode = ChangeThemeMode(repository),
        getThemeMode = GetThemeMode(repository)
    )

    @Test
    fun `given stored theme mode when ui state is collected then emits current display mode`() =
        runTest(mainDispatcherRule.scheduler) {
            // Given
            val repository = FakeConfigurationRepository().apply {
                themeModeState.value = ThemeMode.NIGHT
            }
            val viewModel = createViewModel(repository)
            val expectedState = NavHostViewModel.NavHostUiState(mode = ModeTypeDisplayModel.NIGHT)

            // When
            viewModel.uiState.test {
                val firstState = awaitItem()

                // Then
                val loadedState = if (firstState.mode != expectedState.mode) awaitItem() else firstState
                assertEquals(expectedState, loadedState)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `given automatic mode when on change theme is called then emits day mode`() =
        runTest(mainDispatcherRule.scheduler) {
            // Given
            val repository = FakeConfigurationRepository()
            val viewModel = createViewModel(repository)
            val expectedState = NavHostViewModel.NavHostUiState(mode = ModeTypeDisplayModel.DAY)

            // When
            viewModel.uiState.test {
                awaitItem()
                viewModel.onChangeTheme()

                // Then
                assertEquals(expectedState, awaitItem())
                assertEquals(listOf(ThemeMode.DAY), repository.receivedThemeModes)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `given day mode when on change theme is called then emits night mode`() =
        runTest(mainDispatcherRule.scheduler) {
            // Given
            val repository = FakeConfigurationRepository().apply {
                themeModeState.value = ThemeMode.DAY
            }
            val viewModel = createViewModel(repository)
            val expectedState = NavHostViewModel.NavHostUiState(mode = ModeTypeDisplayModel.NIGHT)

            // When
            viewModel.uiState.test {
                val firstState = awaitItem()
                if (firstState.mode != ModeTypeDisplayModel.DAY) {
                    awaitItem()
                }
                viewModel.onChangeTheme()

                // Then
                assertEquals(expectedState, awaitItem())
                assertEquals(listOf(ThemeMode.NIGHT), repository.receivedThemeModes)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `given night mode when on change theme is called then emits day mode`() =
        runTest(mainDispatcherRule.scheduler) {
            // Given
            val repository = FakeConfigurationRepository().apply {
                themeModeState.value = ThemeMode.NIGHT
            }
            val viewModel = createViewModel(repository)
            val expectedState = NavHostViewModel.NavHostUiState(mode = ModeTypeDisplayModel.DAY)

            // When
            viewModel.uiState.test {
                val firstState = awaitItem()
                if (firstState.mode != ModeTypeDisplayModel.NIGHT) {
                    awaitItem()
                }
                viewModel.onChangeTheme()

                // Then
                assertEquals(expectedState, awaitItem())
                assertEquals(listOf(ThemeMode.DAY), repository.receivedThemeModes)
                cancelAndIgnoreRemainingEvents()
            }
        }
}
