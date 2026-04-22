package com.adrc95.marvelappsample.ui.navhost

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.adrc95.marvelappsample.ui.common.ModeType

class NavHostMenuObservable : BaseObservable() {

    @Bindable
    var darkmode: ModeType = ModeType.AUTOMATIC
        get() = field
        set(value) {
            field = value
            notifyChange()
        }
}
