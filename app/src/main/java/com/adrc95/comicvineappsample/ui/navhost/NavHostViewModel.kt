package com.adrc95.comicvineappsample.ui.navhost

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adrc95.comicvineappsample.ui.common.ModeTypeDisplayModel
import com.adrc95.comicvineappsample.ui.mapper.toDisplayModel
import com.adrc95.comicvineappsample.ui.mapper.toDomain
import com.adrc95.domain.usecase.ChangeThemeMode
import com.adrc95.domain.usecase.GetThemeMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NavHostViewModel @Inject constructor(
    private val changeThemeMode: ChangeThemeMode,
    getThemeMode: GetThemeMode
) : ViewModel() {

    val uiState = getThemeMode()
        .map { themeMode ->
            NavHostUiState(mode = themeMode.toDisplayModel())
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = NavHostUiState()
        )

    fun onChangeTheme() {
        viewModelScope.launch {
            val current = uiState.value.mode
            val newMode =
                if (current == ModeTypeDisplayModel.DAY) ModeTypeDisplayModel.NIGHT
                else ModeTypeDisplayModel.DAY

            changeThemeMode(newMode.toDomain())
        }
    }

    data class NavHostUiState(
        val mode: ModeTypeDisplayModel = ModeTypeDisplayModel.AUTOMATIC,
    )
}
