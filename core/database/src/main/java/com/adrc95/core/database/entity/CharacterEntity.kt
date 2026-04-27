package com.adrc95.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.adrc95.core.database.DatabaseConstants

@Entity(tableName = DatabaseConstants.TABLE_NAME)
data class CharacterEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = DatabaseConstants.COLUMN_ID) val id: Long,
    @ColumnInfo(name = DatabaseConstants.COLUMN_NAME) val name: String,
    @ColumnInfo(name = DatabaseConstants.COLUMN_SHORT_DESCRIPTION) val shortDescription: String?,
    @ColumnInfo(name = DatabaseConstants.COLUMN_LONG_DESCRIPTION) val longDescription: String?,
    @ColumnInfo(name = DatabaseConstants.COLUMN_URI) val uri: String,
    @ColumnInfo(name = DatabaseConstants.COLUMN_THUMBNAIL) val thumbnail: String,
    @ColumnInfo(name = DatabaseConstants.COLUMN_FAVORITE) val favorite: Boolean?
)
