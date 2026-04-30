package com.adrc95.comicvineappsample.ui.detail.di

import androidx.lifecycle.SavedStateHandle
import com.adrc95.comicvineappsample.ui.detail.DetailFragmentArgs
import com.adrc95.comicvineappsample.ui.detail.di.qualifier.CharacterId
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object DetailViewModelModule {

    @Provides
    @ViewModelScoped
    @CharacterId
    fun provideCharacterId(savedStateHandle: SavedStateHandle): Long =
        DetailFragmentArgs.fromSavedStateHandle(savedStateHandle).id
}
