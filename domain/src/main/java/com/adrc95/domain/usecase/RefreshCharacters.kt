package com.adrc95.domain.usecase

import com.adrc95.domain.repository.CharactersRepository
import javax.inject.Inject

class RefreshCharacters @Inject constructor(private val charactersRepository: CharactersRepository) {
    suspend operator fun invoke(offset: Int) = charactersRepository.refreshCharacters(offset)
}
