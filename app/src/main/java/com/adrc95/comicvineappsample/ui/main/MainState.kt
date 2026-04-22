package com.adrc95.comicvineappsample.ui.main

import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.adrc95.comicvineappsample.ui.model.CharacterDisplayModel

fun Fragment.buildMainState(
    navController: NavController = findNavController(),
) = MainEventState(navController)

class MainEventState(private val navController: NavController) {

    fun onCharacterClicked(character: CharacterDisplayModel) {
        val action = MainFragmentDirections.actionMainFragmentToDetailFragment(character.id, character.name)
        navController.navigate(action)
    }
}
