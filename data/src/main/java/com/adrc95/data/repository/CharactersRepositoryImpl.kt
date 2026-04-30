package com.adrc95.data.repository

import arrow.core.Either
import arrow.core.getOrElse
import arrow.core.right
import com.adrc95.data.PageSize
import com.adrc95.data.datasource.LocalCharactersDataSource
import com.adrc95.data.datasource.RemoteCharactersDataSource
import com.adrc95.domain.exception.Failure
import com.adrc95.domain.model.Character
import com.adrc95.domain.repository.CharactersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

class CharactersRepositoryImpl @Inject constructor(
    private val remoteCharactersDataSource: RemoteCharactersDataSource,
    private val localCharactersDataSource: LocalCharactersDataSource
) : CharactersRepository {

    private val refreshMutex = Mutex()

    override fun getCharacters(offset: Int): Flow<List<Character>> =
        localCharactersDataSource.getCharacters()
            .onStart {
                refreshCharacters(offset)
            }

    override suspend fun refreshCharacters(offset: Int): Either<Failure, Unit> =
        refreshMutex.withLock {
            if (offset > 0) {
                val localCount = localCharactersDataSource.countCharacters().getOrElse { 0 }
                if (localCount >= offset + PageSize) {
                    return@withLock Unit.right()
                }
            }

            remoteCharactersDataSource.getCharacters(PageSize, offset).fold(
                ifLeft = { failure -> Either.Left(failure) },
                ifRight = { remoteCharacters ->
                    val mergedCharacters = remoteCharacters.map { remoteCharacter ->
                        val cachedCharacter = localCharactersDataSource.getCharacter(remoteCharacter.id)
                            .getOrElse { null }
                        remoteCharacter.copy(
                            favorite = cachedCharacter?.favorite ?: remoteCharacter.favorite,
                            longDescription = cachedCharacter?.longDescription
                                ?: remoteCharacter.longDescription
                        )
                    }
                    localCharactersDataSource.saveCharacters(mergedCharacters)
                }
            )
        }

    override fun getCharacter(id: Long): Flow<Character?> =
        localCharactersDataSource.observeCharacter(id)
            .onStart {
                refreshCharacterDetail(id)
            }

    override fun getFavoriteCharacters(): Flow<List<Character>> =
        localCharactersDataSource.getFavoriteCharacters()

    override suspend fun updateFavoriteCharacter(id: Long, favorite: Boolean): Either<Failure, Unit> =
        localCharactersDataSource.updateFavoriteCharacter(id, favorite)

    private suspend fun refreshCharacterDetail(id: Long): Either<Failure, Unit> =
        refreshMutex.withLock {
            val cachedCharacter = localCharactersDataSource.getCharacter(id).getOrElse { null }
            if (!cachedCharacter?.longDescription.isNullOrBlank()) {
                return@withLock Unit.right()
            }
            remoteCharactersDataSource.getCharacter(id).fold(
                ifLeft = { failure -> Either.Left(failure) },
                ifRight = { remoteCharacter ->
                    val mergedCharacter = remoteCharacter.copy(
                        favorite = cachedCharacter?.favorite ?: remoteCharacter.favorite
                    )
                    localCharactersDataSource.saveCharacter(mergedCharacter)
                }
            )
        }
}
