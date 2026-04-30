package com.adrc95.comicvineappsample.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adrc95.comicvineappsample.ui.mapper.toDisplayModel
import com.adrc95.comicvineappsample.ui.model.CharacterDisplayModel
import com.adrc95.domain.exception.Failure
import com.adrc95.domain.usecase.FavoriteCharacter
import com.adrc95.domain.usecase.GetCharacter
import com.adrc95.comicvineappsample.ui.detail.di.qualifier.CharacterId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    getCharacter: GetCharacter,
    @CharacterId characterId: Long,
    private val favoriteCharacter: FavoriteCharacter
) : ViewModel() {

    val uiState = getCharacter(characterId)
        .filterNotNull()
        .map { character ->
            DetailUiState(loading = false, character = character.toDisplayModel())
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DetailUiState()
        )

    fun onFavoriteActionClicked() {
        val character = uiState.value.character
        character?.let {
            viewModelScope.launch {
                favoriteCharacter.invoke(
                    id = it.id,
                    isFavorite = !it.favorite,
                ).fold(
                    ifLeft = ::onFavoriteError,
                    ifRight = { Unit }
                )
            }
        }
    }

    @Suppress("UNUSED_PARAMETER")
    private fun onFavoriteError(failure: Failure) = Unit

    data class DetailUiState(
        val loading: Boolean = true,
        val character: CharacterDisplayModel? = null,
    )
}
