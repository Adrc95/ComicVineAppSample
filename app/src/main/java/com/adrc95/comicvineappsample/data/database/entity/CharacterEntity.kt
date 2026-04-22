package com.adrc95.comicvineappsample.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.adrc95.comicvineappsample.data.util.Constants

@Entity(tableName = Constants.TABLE_NAME)
data class CharacterEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = Constants.COLUMN_ID) val id: Long,
    @ColumnInfo(name = Constants.COLUMN_NAME) val name: String,
    @ColumnInfo(name = Constants.COLUMN_SHORT_DESCRIPTION) val shortDescription: String?,
    @ColumnInfo(name = Constants.COLUMN_LONG_DESCRIPTION) val longDescription: String?,
    @ColumnInfo(name = Constants.COLUMN_URI) val uri: String,
    @ColumnInfo(name = Constants.COLUMN_THUMBNAIL) val thumbnail: String,
    @ColumnInfo(name = Constants.COLUMN_FAVORITE) val favorite: Boolean?
)
