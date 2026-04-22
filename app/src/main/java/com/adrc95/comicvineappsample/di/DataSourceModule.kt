package com.adrc95.comicvineappsample.di

import com.adrc95.data.datasource.LocalCharactersDataSource
import com.adrc95.data.datasource.LocalConfigurationDataSource
import com.adrc95.data.datasource.RemoteCharactersDataSource
import com.adrc95.comicvineappsample.data.datasource.ComicVineCharactersDataSource
import com.adrc95.comicvineappsample.data.datasource.PreferencesConfigurationDataSource
import com.adrc95.comicvineappsample.data.datasource.RoomCharactersDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {
    @Binds
    abstract fun bindCharactersDataSource(remote: ComicVineCharactersDataSource): RemoteCharactersDataSource

    @Binds
    abstract fun bindLocalCharactersDataSource(local: RoomCharactersDataSource): LocalCharactersDataSource

    @Binds
    abstract fun bindLocalConfigurationDataSource(preferences: PreferencesConfigurationDataSource):
        LocalConfigurationDataSource
}
