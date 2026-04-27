package com.adrc95.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.adrc95.core.database.dao.CharacterDao
import com.adrc95.core.database.entity.CharacterEntity

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
