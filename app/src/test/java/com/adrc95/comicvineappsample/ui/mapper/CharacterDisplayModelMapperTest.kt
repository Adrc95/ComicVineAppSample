package com.adrc95.comicvineappsample.ui.mapper

import com.adrc95.comicvineappsample.ui.model.CharacterDisplayModel
import com.adrc95.testing.builder.character
import org.junit.Assert.assertEquals
import org.junit.Test

class CharacterDisplayModelMapperTest {

    @Test
    fun `given character when mapped then returns character display model`() {
        // Given
        val character = character {
            withId(7L)
            withName("Batman")
            withShortDescription("Dark Knight")
            withLongDescription("Protector of Gotham City.")
            withThumbnail("https://example.com/batman.png")
            withFavorite(true)
        }
        val expectedDisplayModel = CharacterDisplayModel(
            id = 7L,
            name = "Batman",
            shortDescription = "Dark Knight",
            longDescription = "Protector of Gotham City.",
            thumbnail = "https://example.com/batman.png",
            favorite = true
        )

        // When
        val result = character.toDisplayModel()

        // Then
        assertEquals(expectedDisplayModel, result)
    }
}
