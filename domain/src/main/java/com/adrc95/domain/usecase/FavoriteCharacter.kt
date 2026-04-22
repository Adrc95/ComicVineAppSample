package com.adrc95.domain.usecase

import com.adrc95.domain.repository.CharactersRepository
import javax.inject.Inject

class FavoriteCharacter @Inject constructor(private val charactersRepository: CharactersRepository) {
    suspend operator fun invoke(
        id: Long,
        isFavorite : Boolean
    ) = charactersRepository.updateFavoriteCharacter(id, isFavorite)
}
