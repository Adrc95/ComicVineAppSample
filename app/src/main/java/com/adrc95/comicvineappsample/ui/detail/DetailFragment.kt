package com.adrc95.comicvineappsample.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import com.adrc95.comicvineappsample.ui.navhost.NavHostActivity
import com.adrc95.comicvineappsample.R
import com.adrc95.comicvineappsample.databinding.FragmentDetailBinding
import com.adrc95.comicvineappsample.databinding.MenuActionFavoriteBinding
import com.adrc95.comicvineappsample.ui.common.BaseFragment
import com.adrc95.comicvineappsample.ui.common.launchAndCollect
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : BaseFragment<FragmentDetailBinding>() {

    private val favoriteMenuObservable by lazy { FavoriteMenuObservable() }

    override val viewModel: DetailViewModel by viewModels()

    override val bindView: (LayoutInflater, ViewGroup?, Boolean) -> FragmentDetailBinding
        get() = FragmentDetailBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as NavHostActivity).hideBottomBar()
        setupMenu()

        viewLifecycleOwner.launchAndCollect(viewModel.uiState) {
           manageUiState(it)
        }
    }

    private fun setupMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: android.view.MenuInflater) {
                menu.clear()
                menuInflater.inflate(R.menu.detail_menu, menu)
                val menuItemFavorite = menu.findItem(R.id.action_favorite)

                val binding: MenuActionFavoriteBinding =
                    MenuActionFavoriteBinding.inflate(layoutInflater)
                binding.apply {
                    data = favoriteMenuObservable
                    onFavoriteActionClicked = viewModel::onFavoriteActionClicked
                }
                menuItemFavorite?.apply {
                    actionView = binding.root
                    setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean = false
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onDestroyView() {
        (activity as NavHostActivity).showBottomBar()
        super.onDestroyView()
    }

    private fun manageUiState(state : DetailViewModel.DetailUiState) = with(binding) {
        loading = state.loading
        character = state.character
        favoriteMenuObservable.apply {
            favorite = state.character?.favorite ?: false
            enabled = true
        }
    }

}
