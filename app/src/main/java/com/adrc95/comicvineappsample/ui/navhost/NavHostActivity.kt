package com.adrc95.comicvineappsample.ui.navhost

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.adrc95.comicvineappsample.R
import com.adrc95.comicvineappsample.databinding.ActivityNavHostBinding
import com.adrc95.comicvineappsample.databinding.MenuActionDarkmodeBinding
import com.adrc95.comicvineappsample.ui.common.launchAndCollect
import com.adrc95.comicvineappsample.ui.common.setVisible
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NavHostActivity : AppCompatActivity() {

    private val binding by lazy { ActivityNavHostBinding.inflate(layoutInflater) }

    private val menuObservable by lazy { NavHostMenuObservable() }

    private val navHostState by lazy { buildNavHostState() }

    private val viewModel: NavHostViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        with(binding) {
            val navController = findNavController(R.id.nav_host_fragment)
            val appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.navigation_main,
                    R.id.navigation_favorite
                )
            )
            toolbar.title = getString(R.string.characters)
            setSupportActionBar(toolbar)
            toolbar.setupWithNavController(navController, appBarConfiguration)
            bottomNavigation.setupWithNavController(navController)

            launchAndCollect(viewModel.uiState){
                manageUiState(it)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.nav_host_menu, menu)
        val menuDarkMode = menu.findItem(R.id.action_darkmode)
        val binding: MenuActionDarkmodeBinding = MenuActionDarkmodeBinding.inflate(layoutInflater)
        binding.apply {
            menuObservable.darkmode
            onChangeTheme =  viewModel::onChangeTheme
        }
        menuDarkMode?.apply {
            actionView = binding.root
            setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
        }
        return super.onCreateOptionsMenu(menu)
    }

    private fun manageUiState(state : NavHostViewModel.NavHostUiState) {
        menuObservable.apply {
            darkmode = state.mode
            navHostState.onChangeTheme(state.mode)
        }
    }

    fun showBottomBar() = with(binding) {
        bottomNavigation.setVisible(true)
    }

    fun hideBottomBar() = with(binding) {
        bottomNavigation.setVisible(false)
    }
}
