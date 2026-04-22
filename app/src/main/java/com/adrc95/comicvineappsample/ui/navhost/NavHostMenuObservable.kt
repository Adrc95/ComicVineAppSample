package com.adrc95.comicvineappsample.ui.navhost

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.adrc95.comicvineappsample.ui.common.ModeTypeDisplayModel

class NavHostMenuObservable : BaseObservable() {

    @Bindable
    var darkmode: ModeTypeDisplayModel = ModeTypeDisplayModel.AUTOMATIC
        get() = field
        set(value) {
            field = value
            notifyChange()
        }
}
