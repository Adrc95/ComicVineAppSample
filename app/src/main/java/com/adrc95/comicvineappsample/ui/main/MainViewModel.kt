package com.adrc95.comicvineappsample.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adrc95.domain.exception.Failure
import com.adrc95.domain.usecase.FilterCharacters
import com.adrc95.domain.usecase.GetCharacters
import com.adrc95.domain.usecase.RefreshCharacters
import com.adrc95.comicvineappsample.ui.mapper.toDisplayModel
import com.adrc95.comicvineappsample.ui.model.CharacterDisplayModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class MainViewModel @Inject constructor(
    private val filterCharacters: FilterCharacters,
    private val getCharacters: GetCharacters,
    private val refreshCharactersUseCase: RefreshCharacters
) : ViewModel() {

    private val retryTrigger = MutableStateFlow(0)
    private val initialLoading = MutableStateFlow(true)
    private val filterQuery = MutableStateFlow<String?>(null)
    private val pagingLoading = MutableStateFlow(false)
    private val serverError = MutableStateFlow(false)

    private val characters = retryTrigger.flatMapLatest {
        getCharacters(offset = 0)
    }

    private val filteredCharacters = combine(characters, filterQuery) { characters, query ->
        filterCharacters(characters, query)
    }

    val uiState = combine(
        characters,
        filteredCharacters,
        combine(
            initialLoading,
            filterQuery,
            pagingLoading,
            serverError
        ) { loading, query, isPagingLoading, hasServerError ->
            UiTransientState(
                loading = loading,
                filterQuery = query,
                pagingLoading = isPagingLoading,
                serverError = hasServerError
            )
        }
    ) { sourceCharacters, filteredCharacters, state ->
        val emptySearchResults = sourceCharacters.isNotEmpty() &&
            filteredCharacters.isEmpty() &&
            !state.filterQuery.isNullOrBlank()

        MainUiState(
            loading = state.loading && sourceCharacters.isEmpty(),
            characters = filteredCharacters.map { it.toDisplayModel() },
            enabledSearch = sourceCharacters.isNotEmpty(),
            filterQuery = state.filterQuery,
            pagingLoading = state.pagingLoading,
            serverError = if (sourceCharacters.isEmpty()) state.serverError else false,
            emptySearchResults = emptySearchResults
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MainUiState()
    )

    fun onSearchTextChanged(text: CharSequence) {
        filterQuery.value = text.toString()
    }

    fun fetchMoreCharacters(offset : Int) {
        if (uiState.value.loading || uiState.value.pagingLoading || !filterQuery.value.isNullOrBlank()) return
        pagingLoading.value = true
        refreshCharactersPage(offset)
    }

    @Suppress("UNUSED_PARAMETER")
    private fun onCharactersError(error: Failure) {
        initialLoading.value = false
        pagingLoading.value = false
        serverError.value = uiState.value.characters.isNullOrEmpty()
    }

    private fun refreshCharactersPage(offset : Int = 0) {
        if (offset == 0 && uiState.value.characters.isNullOrEmpty()) {
            initialLoading.value = true
        }
        serverError.value = false

        viewModelScope.launch {
            refreshCharactersUseCase(offset = offset).fold(
                ifLeft = ::onCharactersError,
                ifRight = {
                    initialLoading.value = false
                    pagingLoading.value = false
                },
            )
        }
    }

    fun onTryAgainClicked() {
        initialLoading.value = true
        pagingLoading.value = false
        serverError.value = false
        retryTrigger.update { it + 1 }
    }

    private data class UiTransientState(
        val loading: Boolean,
        val filterQuery: String?,
        val pagingLoading: Boolean,
        val serverError: Boolean
    )

    data class MainUiState(
        val loading : Boolean = true,
        val characters : List<CharacterDisplayModel>? = null,
        val enabledSearch : Boolean = false,
        val filterQuery : String? = null,
        val pagingLoading : Boolean = false,
        val serverError : Boolean = false,
        val emptySearchResults: Boolean = false
    )
}
