package com.adrc95.comicvineappsample.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.adrc95.comicvineappsample.data.database.dao.CharacterDao
import com.adrc95.comicvineappsample.data.database.entity.CharacterEntity

@Database(
    entities = [
        CharacterEntity::class,
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun characterDao(): CharacterDao
}
