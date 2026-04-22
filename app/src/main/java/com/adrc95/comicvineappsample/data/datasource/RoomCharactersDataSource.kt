package com.adrc95.comicvineappsample.data.datasource

import arrow.core.Either
import com.adrc95.domain.exception.Failure
import com.adrc95.data.datasource.LocalCharactersDataSource
import com.adrc95.domain.model.Character
import com.adrc95.comicvineappsample.data.database.dao.CharacterDao
import com.adrc95.comicvineappsample.data.mapper.toDomain
import com.adrc95.comicvineappsample.data.mapper.toEntity
import com.adrc95.comicvineappsample.data.util.tryCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoomCharactersDataSource @Inject constructor(private val dao : CharacterDao) : LocalCharactersDataSource {

    override fun getCharacters(): Flow<List<Character>> =
        dao.loadAllCharacters().map { characters -> characters.map { it.toDomain() } }

    override fun getFavoriteCharacters(): Flow<List<Character>> =
        dao.loadAllFavoriteCharacters().map { characters -> characters.map { it.toDomain() } }

    override fun observeCharacter(id: Long): Flow<Character?> =
        dao.getCharacterById(id).map { character -> character?.toDomain() }

    override suspend fun getCharacter(id: Long): Either<Failure, Character> = tryCall {
        dao.findCharacterById(id)?.toDomain()
            ?: throw NoSuchElementException("Character with id $id not found")
    }

    override suspend fun countCharacters(): Either<Failure, Int> = tryCall {
        dao.countCharacters()
    }

    override suspend fun saveCharacter(character: Character): Either<Failure, Unit> = tryCall {
        dao.insert(character.toEntity())
    }

    override suspend fun saveCharacters(characters: List<Character>): Either<Failure, Unit> = tryCall {
        dao.insertAll(characters.map { it.toEntity() })
    }

    override suspend fun updateFavoriteCharacter(id: Long, favorite: Boolean): Either<Failure, Unit> = tryCall {
        val updatedRows = dao.updateFavoriteCharacter(id, favorite)
        if (updatedRows == 0) {
            throw NoSuchElementException("Character with id $id not found")
        }
    }
}
