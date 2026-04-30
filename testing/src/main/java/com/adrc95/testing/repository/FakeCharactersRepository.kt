package com.adrc95.testing.repository

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.adrc95.domain.exception.Failure
import com.adrc95.domain.model.Character
import com.adrc95.domain.repository.CharactersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

class FakeCharactersRepository : CharactersRepository {
    private val charactersByOffsetState = MutableStateFlow<Map<Int, List<Character>>>(emptyMap())
    private val charactersByIdState = MutableStateFlow<Map<Long, Character>>(emptyMap())
    private val favoriteCharactersState = MutableStateFlow<List<Character>>(emptyList())

    private var refreshCharactersResult: Either<Failure, Unit> = Unit.right()
    private var updateFavoriteCharacterResult: Either<Failure, Unit> = Unit.right()

    val requestedOffsets = mutableListOf<Int>()
    val refreshedOffsets = mutableListOf<Int>()
    val requestedCharacterIds = mutableListOf<Long>()
    val favoriteUpdates = mutableListOf<Pair<Long, Boolean>>()

    override fun getCharacters(offset: Int): Flow<List<Character>> =
        charactersByOffsetState.asStateFlow().map { charactersByOffset ->
            requestedOffsets += offset
            charactersByOffset[offset].orEmpty()
        }

    fun setCharacters(offset: Int, items: List<Character>) {
        charactersByOffsetState.value = charactersByOffsetState.value.toMutableMap().apply {
            this[offset] = items
        }
        charactersByIdState.value = charactersByIdState.value.toMutableMap().apply {
            items.forEach { character -> this[character.id] = character }
        }
    }

    override suspend fun refreshCharacters(offset: Int): Either<Failure, Unit> {
        refreshedOffsets += offset
        return refreshCharactersResult
    }

    fun setRefreshCharactersResult(result: Either<Failure, Unit>) {
        refreshCharactersResult = result
    }

    override fun getCharacter(id: Long): Flow<Character?> =
        charactersByIdState.asStateFlow().map { charactersById ->
            requestedCharacterIds += id
            charactersById[id]
        }

    fun setCharacter(item: Character) {
        charactersByIdState.value = charactersByIdState.value.toMutableMap().apply {
            this[item.id] = item
        }
    }

    override fun getFavoriteCharacters(): Flow<List<Character>> = favoriteCharactersState.asStateFlow()

    fun setFavoriteCharacters(items: List<Character>) {
        favoriteCharactersState.value = items
        charactersByIdState.value = charactersByIdState.value.toMutableMap().apply {
            items.forEach { character -> this[character.id] = character }
        }
    }

    fun setUpdateFavoriteCharacterResult(result: Either<Failure, Unit>) {
        updateFavoriteCharacterResult = result
    }

    override suspend fun updateFavoriteCharacter(id: Long, favorite: Boolean): Either<Failure, Unit> {
        favoriteUpdates += id to favorite

        return updateFavoriteCharacterResult.onRight {
            val updatedCharacter = charactersByIdState.value[id]?.copy(favorite = favorite)
            if (updatedCharacter != null) {
                charactersByIdState.value = charactersByIdState.value.toMutableMap().apply {
                    this[id] = updatedCharacter
                }
                favoriteCharactersState.value = charactersByIdState.value.values
                    .filter { character -> character.favorite }
            }
        }
    }
}
