package com.adrc95.core.network.mapper

import com.adrc95.core.network.builder.characterDataResponse
import com.adrc95.core.network.builder.characterDetailResponse
import com.adrc95.core.network.builder.networkCharacter
import com.adrc95.core.network.builder.networkImage
import com.adrc95.testing.builder.character
import org.junit.Assert.assertEquals
import org.junit.Test

class ComicVineMapperTest {

    @Test
    fun `given character data response when mapped to domain then returns character list`() {
        // Given
        val response = characterDataResponse {
            withResults(
                listOf(
                    networkCharacter {
                        withId(1L)
                        withName("Spider-Man")
                        withDeck(" Friendly neighborhood hero. ")
                        withDescription("<p>Ignored on list mapping</p>")
                        withSiteDetailUrl("https://comicvine.gamespot.com/spider-man/4005-1443/")
                        withApiDetailUrl("https://comicvine.gamespot.com/api/character/4005-1443/")
                        withImage(
                            networkImage {
                                withOriginalUrl("https://example.com/spiderman-original.png")
                                withMediumUrl("https://example.com/spiderman-medium.png")
                                withIconUrl("https://example.com/spiderman-icon.png")
                            }
                        )
                    }
                )
            )
        }
        val expectedCharacters = listOf(
            character {
                withId(1L)
                withName("Spider-Man")
                withShortDescription("Ignored on list mapping")
                withLongDescription(null)
                withUri("https://comicvine.gamespot.com/spider-man/4005-1443/")
                withThumbnail("https://example.com/spiderman-medium.png")
                withFavorite(false)
            }
        )

        // When
        val result = response.toDomain()

        // Then
        assertEquals(expectedCharacters, result)
    }

    @Test
    fun `given character detail response with fallback values when mapped to domain then returns sanitized character`() {
        // Given
        val response = characterDetailResponse {
            withResults(
                networkCharacter {
                    withId(7L)
                    withName("Batman")
                    withDeck("Dark Knight")
                    withDescription("<p>Protector <b>of</b> Gotham.</p>")
                    withSiteDetailUrl(null)
                    withApiDetailUrl("https://comicvine.gamespot.com/api/character/4005-1699/")
                    withImage(
                        networkImage {
                            withOriginalUrl(null)
                            withMediumUrl(null)
                            withIconUrl("https://example.com/batman-icon.png")
                        }
                    )
                }
            )
        }
        val expectedCharacter = character {
            withId(7L)
            withName("Batman")
            withShortDescription("Dark Knight")
            withLongDescription("Protector of Gotham.")
            withUri("https://comicvine.gamespot.com/api/character/4005-1699/")
            withThumbnail("https://example.com/batman-icon.png")
            withFavorite(false)
        }

        // When
        val result = response.toDomain()

        // Then
        assertEquals(expectedCharacter, result)
    }
}
