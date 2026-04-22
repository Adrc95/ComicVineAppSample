package com.adrc95.domain.usecase

import com.adrc95.domain.model.Character
import javax.inject.Inject

class FilterCharacters @Inject constructor() {

    operator fun invoke(characters: List<Character>, query: String?): List<Character> {
        val filter = query?.trim()?.lowercase().orEmpty()
        if (filter.isEmpty()) return characters

        return characters.filter { character ->
            character.name.lowercase().contains(filter)
        }
    }
}
