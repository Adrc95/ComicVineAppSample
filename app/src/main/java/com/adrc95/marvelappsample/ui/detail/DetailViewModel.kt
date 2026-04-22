package com.adrc95.marvelappsample.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adrc95.data.exception.Failure
import com.adrc95.domain.Character
import com.adrc95.usecases.FavoriteCharacter
import com.adrc95.usecases.GetCharacter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(savedStateHandle: SavedStateHandle,
                                          private  val getCharacter: GetCharacter,
    private val favoriteCharacter: FavoriteCharacter) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState = _uiState.asStateFlow()

    private val args = DetailFragmentArgs.fromSavedStateHandle(savedStateHandle)

    init {
        getCharacter(args.id)
    }

    private fun getCharacter(id : Long) {
        getCharacter(GetCharacter.Params(id),viewModelScope) {
            it.fold(::onCharacterError,::onCharacterSuccess)
        }
    }

    private fun onCharacterSuccess(character: Character) {
        _uiState.update { DetailUiState(loading = false, character = character) }
    }

    @Suppress("UNUSED_PARAMETER")
    private fun onCharacterError(failure: Failure) = Unit

    fun onFavoriteActionClicked() {
        val character = uiState.value.character
        character?.let {
            favoriteCharacter(FavoriteCharacter.Params(it.id, !it.favorite)){ result ->
                result.fold(::onFavoriteError,::onFavoriteSuccess)
            }
        }
    }

    @Suppress("UNUSED_PARAMETER")
    private fun onFavoriteSuccess(unit: Unit) {
        getCharacter(args.id)
    }

    @Suppress("UNUSED_PARAMETER")
    private fun onFavoriteError(failure: Failure) = Unit

    data class DetailUiState(
        val loading : Boolean = true,
        val character : Character? = null,
    )

}
