package com.adrc95.domain.usecase

import com.adrc95.domain.repository.CharactersRepository
import javax.inject.Inject

class GetCharacter @Inject constructor(private val charactersRepository: CharactersRepository) {
    operator fun invoke(id: Long) = charactersRepository.getCharacter(id)
}
