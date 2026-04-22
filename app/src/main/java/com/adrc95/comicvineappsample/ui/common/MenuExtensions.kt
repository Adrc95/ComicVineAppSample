package com.adrc95.comicvineappsample.ui.common

import android.graphics.Color
import android.view.Menu
import androidx.annotation.ColorInt
import androidx.core.view.forEach

fun Menu.tint(@ColorInt color: Int = Color.WHITE) {
    forEach { it.icon?.setTint(color) }
}
