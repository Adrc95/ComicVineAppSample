package com.adrc95.comicvineappsample.di

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.adrc95.core.database.AppDatabase
import com.adrc95.core.database.dao.CharacterDao
import com.adrc95.core.database.di.DatabaseModule
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatabaseModule::class]
)
object TestDatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(): AppDatabase = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(),
        AppDatabase::class.java
    ).build()

    @Provides
    @Singleton
    fun provideCharacterDao(db: AppDatabase): CharacterDao = db.characterDao()
}
