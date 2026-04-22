package com.adrc95.comicvineappsample.ui.detail

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable

class FavoriteMenuObservable : BaseObservable() {

    @Bindable
    var favorite: Boolean = false
        get() = field
        set(value) {
            field = value
            notifyChange()
        }

    @Bindable
    var enabled: Boolean = false
        get() = field
        set(value) {
            field = value
            notifyChange()
        }
}
