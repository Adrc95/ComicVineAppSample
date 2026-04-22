package com.adrc95.comicvineappsample.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adrc95.comicvineappsample.ui.mapper.toDisplayModel
import com.adrc95.comicvineappsample.ui.model.CharacterDisplayModel
import com.adrc95.domain.exception.Failure
import com.adrc95.domain.usecase.FavoriteCharacter
import com.adrc95.domain.usecase.GetFavoriteCharacters
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    getFavoriteCharacters: GetFavoriteCharacters,
    private val favoriteCharacters: FavoriteCharacter
) : ViewModel() {

    val uiState = getFavoriteCharacters()
        .map { characters ->
            FavoriteUiState(
                loading = false,
                characters = characters.map { it.toDisplayModel() },
                emptyFavorites = characters.isEmpty(),
                serverError = false
            )
        }
        .catch {
            emit(FavoriteUiState(loading = false, serverError = true))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = FavoriteUiState()
        )

    fun onCharacterDelete(id: Long) {
        viewModelScope.launch {
            favoriteCharacters.invoke(
                id = id,
                isFavorite = false,
            ).fold(
                ifLeft = ::onFavoriteError,
                ifRight = ::onFavoriteSuccess,
            )
        }
    }

    @Suppress("UNUSED_PARAMETER")
    private fun onFavoriteSuccess(unit: Unit) = Unit

    @Suppress("UNUSED_PARAMETER")
    private fun onFavoriteError(failure: Failure) = Unit

    data class FavoriteUiState(
        val loading: Boolean = true,
        val characters: List<CharacterDisplayModel>? = null,
        val emptyFavorites: Boolean = false,
        val serverError: Boolean = false,
    )
}
