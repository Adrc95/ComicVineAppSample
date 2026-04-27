package com.adrc95.data.datasource

import arrow.core.Either
import com.adrc95.domain.exception.Failure
import com.adrc95.domain.model.Character
import kotlinx.coroutines.flow.Flow

interface LocalCharactersDataSource {
    fun getCharacters(): Flow<List<Character>>

    fun getFavoriteCharacters(): Flow<List<Character>>

    fun observeCharacter(id: Long): Flow<Character?>

    suspend fun getCharacter(id: Long): Either<Failure, Character>

    suspend fun countCharacters(): Either<Failure, Int>

    suspend fun saveCharacter(character: Character): Either<Failure, Unit>

    suspend fun saveCharacters(characters: List<Character>): Either<Failure, Unit>

    suspend fun updateFavoriteCharacter(id: Long, favorite: Boolean): Either<Failure, Unit>
}
