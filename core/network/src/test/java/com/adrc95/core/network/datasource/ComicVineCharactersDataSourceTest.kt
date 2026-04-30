package com.adrc95.core.network.datasource

import com.adrc95.core.network.service.CharacterService
import com.adrc95.domain.exception.Failure
import com.adrc95.domain.model.Character
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class ComicVineCharactersDataSourceTest {
    private val server = MockWebServer()
    private lateinit var remoteDataSource: ComicVineCharactersDataSource

    @Before
    fun setUp() {
        server.start()
        val json = Json {
            ignoreUnknownKeys = true
            isLenient = true
            coerceInputValues = true
        }
        val contentType = "application/json".toMediaType()
        val retrofit = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .client(OkHttpClient())
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
        val api = retrofit.create(CharacterService::class.java)
        remoteDataSource = ComicVineCharactersDataSource(api)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `given empty results response when get characters then returns empty list`() = runTest {
        // Given
        server.enqueue(
            MockResponse()
                .setBody(readResource("characters_empty.json"))
                .setResponseCode(200)
        )

        // When
        val result = remoteDataSource.getCharacters(limit = 20, offset = 0).getOrNull()

        // Then
        assertEquals(emptyList<Character>(), result)
    }

    @Test
    fun `given characters response when get characters then returns mapped characters`() = runTest {
        // Given
        server.enqueue(
            MockResponse()
                .setBody(readResource("characters_page_1.json"))
                .setResponseCode(200)
        )

        // When
        val result = checkNotNull(remoteDataSource.getCharacters(limit = 20, offset = 0).getOrNull())

        // Then
        assertEquals(1, result.size)
        assertEquals(1L, result.first().id)
        assertEquals("Spider-Man", result.first().name)
        assertEquals("Detailed text for list mapping", result.first().shortDescription)
        assertEquals("https://comicvine.gamespot.com/spider-man/4005-1443/", result.first().uri)
        assertEquals("https://example.com/spiderman-medium.png", result.first().thumbnail)
    }

    @Test
    fun `given get characters call when request is executed then sends expected query params`() = runTest {
        // Given
        server.enqueue(
            MockResponse()
                .setBody(readResource("characters_empty.json"))
                .setResponseCode(200)
        )

        // When
        remoteDataSource.getCharacters(limit = 20, offset = 0)
        val request = server.takeRequest()
        val requestUrl = checkNotNull(request.requestUrl)

        // Then
        assertEquals("/characters/", requestUrl.encodedPath)
        assertEquals("20", requestUrl.queryParameter("limit"))
        assertEquals("0", requestUrl.queryParameter("offset"))
        assertEquals("id,name,deck,image", requestUrl.queryParameter("field_list"))
        assertEquals("name:asc", requestUrl.queryParameter("sort"))
    }

    @Test
    fun `given server error response when get characters then returns server failure`() = runTest {
        // Given
        server.enqueue(MockResponse().setResponseCode(500))

        // When
        val result = remoteDataSource.getCharacters(limit = 20, offset = 0)

        // Then
        assertEquals(Failure.Server(500), result.leftOrNull())
    }

    @Test
    fun `given character detail response when get character then returns mapped character`() = runTest {
        // Given
        server.enqueue(
            MockResponse()
                .setBody(readResource("character_7.json"))
                .setResponseCode(200)
        )

        // When
        val result = checkNotNull(remoteDataSource.getCharacter(7L).getOrNull())

        // Then
        assertEquals(7L, result.id)
        assertEquals("Batman", result.name)
        assertEquals("Dark Knight", result.shortDescription)
        assertEquals("Protector of Gotham.", result.longDescription)
        assertEquals("https://comicvine.gamespot.com/api/character/4005-1699/", result.uri)
        assertEquals("https://example.com/batman-original.png", result.thumbnail)
    }

    @Test
    fun `given get character call when request is executed then sends expected path and query params`() = runTest {
        // Given
        server.enqueue(
            MockResponse()
                .setBody(readResource("character_minimal.json"))
                .setResponseCode(200)
        )

        // When
        remoteDataSource.getCharacter(7L)
        val request = server.takeRequest()
        val requestUrl = checkNotNull(request.requestUrl)

        // Then
        assertEquals("/character/4005-7/", requestUrl.encodedPath)
        assertEquals(
            "id,name,deck,description,site_detail_url,api_detail_url,image",
            requestUrl.queryParameter("field_list")
        )
    }

    @Test
    fun `given server error response when get character then returns server failure`() = runTest {
        // Given
        server.enqueue(MockResponse().setResponseCode(500))

        // When
        val result = remoteDataSource.getCharacter(7L)

        // Then
        assertEquals(Failure.Server(500), result.leftOrNull())
    }

    private fun readResource(fileName: String): String =
        checkNotNull(javaClass.classLoader?.getResource(fileName)) {
            "Missing test resource: $fileName"
        }.readText()
}
