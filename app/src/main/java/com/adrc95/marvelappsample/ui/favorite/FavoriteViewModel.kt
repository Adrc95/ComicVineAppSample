package com.adrc95.marvelappsample.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adrc95.data.exception.Failure
import com.adrc95.domain.Character
import com.adrc95.usecases.FavoriteCharacter
import com.adrc95.usecases.GetFavoriteCharacters
import com.adrc95.usecases.UseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(private val getFavoriteCharacters: GetFavoriteCharacters,
private val favoriteCharacters: FavoriteCharacter): ViewModel() {

    private val _uiState = MutableStateFlow(FavoriteUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getFavoriteCharacters()
    }

    fun onResume() {
        getFavoriteCharacters()
    }

    private fun getFavoriteCharacters() {
        getFavoriteCharacters(UseCase.None(), viewModelScope) {
            it.fold(::onGetFavoriteCharactersError, ::onGetFavoriteCharactersSuccess)
        }
    }

    private fun onGetFavoriteCharactersSuccess(characters : List<Character>) {
        _uiState.update { FavoriteUiState(characters = characters, emptyFavorites = characters.isEmpty() ,
            loading = false) }
    }

    @Suppress("UNUSED_PARAMETER")
    private fun onGetFavoriteCharactersError(failure: Failure) {
        _uiState.update { FavoriteUiState(serverError = true ,loading = false) }
    }

    fun onCharacterDelete(id: Long) {
        favoriteCharacters(FavoriteCharacter.Params(id, false)) {
            it.fold(::onFavoriteError, ::onFavoriteSuccess)
        }
    }

    @Suppress("UNUSED_PARAMETER")
    private fun onFavoriteSuccess(unit :Unit) {
        getFavoriteCharacters()
    }

    @Suppress("UNUSED_PARAMETER")
    private fun onFavoriteError(failure: Failure) {
        TODO()
    }

    fun onTryAgainClicked() {
        getFavoriteCharacters()
    }

    data class FavoriteUiState(
        val loading : Boolean = true,
        val characters : List<Character>? = null,
        val emptyFavorites : Boolean = false,
        val serverError : Boolean = false,
    )

}
