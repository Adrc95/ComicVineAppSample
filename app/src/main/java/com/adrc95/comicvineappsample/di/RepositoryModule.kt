package com.adrc95.comicvineappsample.di

import com.adrc95.domain.repository.CharactersRepository
import com.adrc95.domain.repository.ConfigurationRepository
import com.adrc95.data.repository.CharactersRepositoryImpl
import com.adrc95.data.repository.ConfigurationRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindCharactersRepository(
        charactersRepository: CharactersRepositoryImpl
    ): CharactersRepository

    @Binds
    abstract fun bindConfigurationRepository(
        configurationRepository: ConfigurationRepositoryImpl
    ): ConfigurationRepository
}
