package com.adrc95.comicvineappsample.data.datasource

import arrow.core.Either
import com.adrc95.domain.exception.Failure
import com.adrc95.data.datasource.RemoteCharactersDataSource
import com.adrc95.domain.model.Character
import com.adrc95.comicvineappsample.data.mapper.toDomain
import com.adrc95.comicvineappsample.data.server.service.CharacterService
import com.adrc95.comicvineappsample.data.util.tryCall
import javax.inject.Inject

class ComicVineCharactersDataSource @Inject constructor(private val api: CharacterService) :
    RemoteCharactersDataSource {

    override suspend fun getCharacters(limit: Int, offset: Int): Either<Failure, List<Character>> = tryCall {
        api.getCharacters(limit, offset).toDomain()
    }

    override suspend fun getCharacter(id: Long): Either<Failure, Character> = tryCall{
        api.getCharacter(id).toDomain()
    }
}
