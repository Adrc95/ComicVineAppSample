package com.adrc95.core.network.builder

import com.adrc95.core.network.model.Character
import com.adrc95.core.network.model.CharacterDataResponse
import com.adrc95.core.network.model.CharacterDetailResponse
import com.adrc95.core.network.model.Image

class NetworkImageBuilder {
    var originalUrl: String? = "https://example.com/spiderman-original.png"
    var mediumUrl: String? = "https://example.com/spiderman-medium.png"
    var iconUrl: String? = "https://example.com/spiderman-icon.png"

    fun withOriginalUrl(originalUrl: String?) = apply { this.originalUrl = originalUrl }

    fun withMediumUrl(mediumUrl: String?) = apply { this.mediumUrl = mediumUrl }

    fun withIconUrl(iconUrl: String?) = apply { this.iconUrl = iconUrl }

    fun build() = Image(
        originalUrl = originalUrl,
        mediumUrl = mediumUrl,
        iconUrl = iconUrl
    )
}

class NetworkCharacterBuilder {
    var id: Long = 1L
    var name: String = "Spider-Man"
    var deck: String? = " Friendly neighborhood hero. "
    var description: String? = "<p>Ignored on list mapping</p>"
    var siteDetailUrl: String? = "https://comicvine.gamespot.com/spider-man/4005-1443/"
    var apiDetailUrl: String? = "https://comicvine.gamespot.com/api/character/4005-1443/"
    var image: Image? = networkImage()

    fun withId(id: Long) = apply { this.id = id }

    fun withName(name: String) = apply { this.name = name }

    fun withDeck(deck: String?) = apply { this.deck = deck }

    fun withDescription(description: String?) = apply { this.description = description }

    fun withSiteDetailUrl(siteDetailUrl: String?) = apply { this.siteDetailUrl = siteDetailUrl }

    fun withApiDetailUrl(apiDetailUrl: String?) = apply { this.apiDetailUrl = apiDetailUrl }

    fun withImage(image: Image?) = apply { this.image = image }

    fun build() = Character(
        id = id,
        name = name,
        deck = deck,
        description = description,
        siteDetailUrl = siteDetailUrl,
        apiDetailUrl = apiDetailUrl,
        image = image
    )
}

class CharacterDataResponseBuilder {
    var statusCode: Int = 1
    var error: String = "OK"
    var limit: Int = 20
    var offset: Int = 0
    var totalResults: Int = 1
    var pageResults: Int = 1
    var results: List<Character> = listOf(networkCharacter())

    fun withResults(results: List<Character>) = apply { this.results = results }

    fun build() = CharacterDataResponse(
        statusCode = statusCode,
        error = error,
        limit = limit,
        offset = offset,
        totalResults = totalResults,
        pageResults = pageResults,
        results = results
    )
}

class CharacterDetailResponseBuilder {
    var statusCode: Int = 1
    var error: String = "OK"
    var limit: Int = 1
    var offset: Int = 0
    var totalResults: Int = 1
    var pageResults: Int = 1
    var results: Character = networkCharacter()

    fun withResults(results: Character) = apply { this.results = results }

    fun build() = CharacterDetailResponse(
        statusCode = statusCode,
        error = error,
        limit = limit,
        offset = offset,
        totalResults = totalResults,
        pageResults = pageResults,
        results = results
    )
}

fun networkImage(block: NetworkImageBuilder.() -> Unit = {}) =
    NetworkImageBuilder().apply(block).build()

fun networkCharacter(block: NetworkCharacterBuilder.() -> Unit = {}) =
    NetworkCharacterBuilder().apply(block).build()

fun characterDataResponse(block: CharacterDataResponseBuilder.() -> Unit = {}) =
    CharacterDataResponseBuilder().apply(block).build()

fun characterDetailResponse(block: CharacterDetailResponseBuilder.() -> Unit = {}) =
    CharacterDetailResponseBuilder().apply(block).build()
