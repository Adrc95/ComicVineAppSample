package com.adrc95.comicvineappsample.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.adrc95.comicvineappsample.databinding.FragmentFavoriteBinding
import com.adrc95.comicvineappsample.ui.common.BaseFragment
import com.adrc95.comicvineappsample.ui.common.launchAndCollect
import com.adrc95.comicvineappsample.ui.favorite.adapter.FavoriteCharactersAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : BaseFragment<FragmentFavoriteBinding>() {

    override val viewModel: FavoriteViewModel by viewModels()

    private val adapter = FavoriteCharactersAdapter { viewModel.onCharacterDelete(it) }

    override val bindView: (LayoutInflater, ViewGroup?, Boolean) -> FragmentFavoriteBinding
        get() = FragmentFavoriteBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            rvCharacters.adapter = adapter
        }

        viewLifecycleOwner.launchAndCollect(viewModel.uiState) {
            manageUiState(it)
        }
    }

    private fun manageUiState(state : FavoriteViewModel.FavoriteUiState) = with(binding){
        loading = state.loading
        characters = state.characters
        emptyFavorites = state.emptyFavorites
        serverError = state.serverError
    }
}
