package com.adrc95.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.adrc95.core.database.DatabaseConstants.COLUMN_FAVORITE
import com.adrc95.core.database.DatabaseConstants.TABLE_NAME
import com.adrc95.core.database.entity.CharacterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(character: CharacterEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(characters: List<CharacterEntity>)

    @Delete
    suspend fun delete(character: CharacterEntity): Int

    @Query("SELECT * FROM $TABLE_NAME  ORDER BY name ASC")
    fun loadAllCharacters(): Flow<List<CharacterEntity>>

    @Query("SELECT * FROM $TABLE_NAME WHERE favorite = 1  ORDER BY name ASC")
    fun loadAllFavoriteCharacters(): Flow<List<CharacterEntity>>

    @Query("SELECT * FROM $TABLE_NAME WHERE name LIKE '%' || :name || '%' ORDER BY name ASC")
    suspend fun searchCharacterByName(name: String): List<CharacterEntity>

    @Query("SELECT * FROM $TABLE_NAME WHERE id = :id")
    suspend fun findCharacterById(id: Long): CharacterEntity?

    @Query("SELECT * FROM $TABLE_NAME WHERE id = :id")
    fun getCharacterById(id: Long): Flow<CharacterEntity?>

    @Query("SELECT COUNT(*) FROM $TABLE_NAME")
    suspend fun countCharacters(): Int

    @Query("UPDATE $TABLE_NAME SET $COLUMN_FAVORITE = :favorite  WHERE id=:id")
    suspend fun updateFavoriteCharacter(id: Long, favorite: Boolean): Int
}
