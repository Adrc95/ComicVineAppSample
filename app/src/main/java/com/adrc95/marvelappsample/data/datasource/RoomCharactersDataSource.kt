package com.adrc95.marvelappsample.data.datasource

import arrow.core.Either
import com.adrc95.data.exception.Failure
import com.adrc95.data.source.LocalCharactersDataSource
import com.adrc95.domain.Character
import com.adrc95.marvelappsample.data.database.CharacterDao
import com.adrc95.marvelappsample.data.mapper.toDomain
import com.adrc95.marvelappsample.data.mapper.toEntity

class RoomCharactersDataSource(private val dao : CharacterDao) : LocalCharactersDataSource {

    override suspend fun getFavoriteCharacters(): Either<Failure, List<Character>> =
        Either.catch {
            dao.loadAllFavoriteCharacters().map { it.toDomain() }
        }.fold(
            ifLeft = { Either.Left(Failure.LocalError) },
            ifRight = { Either.Right(it) }
        )

    override suspend fun getCharacter(id: Long): Either<Failure, Character>  =
        Either.catch {
            dao.findCharacterById(id).toDomain()
        }.fold(
            ifLeft = { Either.Left(Failure.LocalError) },
            ifRight = { Either.Right(it) }
        )

    override suspend fun saveCharacter(character: Character): Either<Failure, Long> {
        val characterId = dao.insert(character.toEntity())
        return if (characterId > 1) {
            Either.Right(characterId)
        } else {
            Either.Left(Failure.LocalError)
        }
    }

    override suspend fun updateFavoriteCharacter(id: Long, favorite: Boolean): Either<Failure, Unit> =
        Either.catch {
            dao.updateFavoriteCharacter(id, favorite)
        }.fold(
            ifLeft = { Either.Left(Failure.LocalError) },
            ifRight = { Either.Right(it) }
        )
}
