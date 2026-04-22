package com.adrc95.comicvineappsample.ui.navhost

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.adrc95.comicvineappsample.ui.common.ModeTypeDisplayModel

fun Activity.buildNavHostState(context: Context = this) = NavHostState(context)

class NavHostState(private val context: Context) {

    fun onChangeTheme(mode : ModeTypeDisplayModel) {
        when (mode) {
         ModeTypeDisplayModel.NIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
         ModeTypeDisplayModel.DAY ->  AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }
}
