package com.adrc95.marvelappsample.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

object Character {
    const val TABLE_NAME = "characters"
    const val COLUMN_ID = "id"
    const val COLUMN_NAME = "name"
    const val COLUMN_DESCRIPTION = "description"
    const val COLUMN_URI = "uri"
    const val COLUMN_THUMBNAIL = "thumbnail"
    const val COLUMN_FAVORITE = "favorite"
}

@Entity(tableName = Character.TABLE_NAME)
data class CharacterEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = Character.COLUMN_ID) val id: Long,
    @ColumnInfo(name = Character.COLUMN_NAME) val name: String,
    @ColumnInfo(name = Character.COLUMN_DESCRIPTION) val description: String?,
    @ColumnInfo(name = Character.COLUMN_URI) val uri: String,
    @ColumnInfo(name = Character.COLUMN_THUMBNAIL) val thumbnail: String,
    @ColumnInfo(name = Character.COLUMN_FAVORITE) val favorite: Boolean?
)
