package com.adrc95.domain.repository

import arrow.core.Either
import com.adrc95.domain.exception.Failure
import com.adrc95.domain.model.Character
import kotlinx.coroutines.flow.Flow

interface CharactersRepository {
    fun getCharacters(offset: Int) : Flow<List<Character>>
    suspend fun refreshCharacters(offset: Int) : Either<Failure, Unit>
    fun getCharacter(id : Long) : Flow<Character?>
    fun getFavoriteCharacters() : Flow<List<Character>>
    suspend fun updateFavoriteCharacter(id : Long, favorite: Boolean) : Either<Failure, Unit>
}
