package com.adrc95.marvelappsample.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adrc95.data.exception.Failure
import com.adrc95.domain.Character
import com.adrc95.marvelappsample.data.util.Constants.LIMIT_CHARACTER_OF_PAGE
import com.adrc95.usecases.GetCharacters
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val getCharacters: GetCharacters) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getCharacters()
    }

    fun onSearchTextChanged(text: CharSequence) {
        val query = text.toString()
        _uiState.update { it.copy(filterQuery = query) }
    }

    fun fetchMoreCharacters(count : Int) {
        _uiState.update { it.copy(loading = true, moreCharacters = true) }
        getCharacters(count)
    }

    @Suppress("UNUSED_PARAMETER")
    private fun onCharactersError(error: Failure) {
        _uiState.update { MainUiState(loading = false, serverError = true) }
    }

    private fun onCharactersSuccess(characters : List<Character>) {
        _uiState.update { MainUiState(characters = characters, loading = false, enabledSearch = true)}
    }

    private fun getCharacters(count : Int = 0) {
        _uiState.update { it.copy(enabledSearch = false) }
        getCharacters(GetCharacters.Params(limit = LIMIT_CHARACTER_OF_PAGE, offset = count), viewModelScope) {
            it.fold(::onCharactersError,::onCharactersSuccess)
        }
    }

    fun onTryAgainClicked() {
        getCharacters()
    }

    data class MainUiState(
        val loading : Boolean = true,
        val characters : List<Character>? = null,
        val enabledSearch : Boolean = false,
        val filterQuery : String? = null,
        val moreCharacters : Boolean = false,
        val serverError : Boolean = false
    )
}
