package com.adrc95.data.datasource

import arrow.core.Either
import com.adrc95.domain.exception.Failure
import com.adrc95.domain.model.Character

interface RemoteCharactersDataSource {
    suspend fun getCharacters(limit: Int, offset: Int): Either<Failure, List<Character>>

    suspend fun getCharacter(id: Long): Either<Failure, Character>
}
