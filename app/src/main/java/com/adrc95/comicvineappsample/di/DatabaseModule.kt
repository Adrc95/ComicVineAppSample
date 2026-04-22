package com.adrc95.comicvineappsample.di

import android.content.Context
import androidx.room.Room
import com.adrc95.comicvineappsample.data.database.dao.CharacterDao
import com.adrc95.comicvineappsample.data.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context = context,
            klass = AppDatabase::class.java,
            name = "ComicVine"
        ).build()

    @Provides
    @Singleton
    fun providesCharacterDao(db: AppDatabase): CharacterDao = db.characterDao()
}
