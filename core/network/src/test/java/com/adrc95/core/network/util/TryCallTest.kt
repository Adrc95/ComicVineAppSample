package com.adrc95.core.network.util

import com.adrc95.domain.exception.Failure
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class TryCallTest {

    @Test
    fun `given successful action when try call then returns right value`() = runTest {
        // Given
        val expectedValue = 42

        // When
        val result = tryCall { expectedValue }

        // Then
        assertEquals(expectedValue, result.getOrNull())
    }

    @Test
    fun `given io exception when try call then returns connectivity failure`() = runTest {
        // When
        val result = tryCall<Int> {
            throw IOException("network")
        }

        // Then
        assertEquals(Failure.Connectivity, result.leftOrNull())
    }

    @Test
    fun `given http exception when try call then returns server failure`() = runTest {
        // Given
        val response = Response.error<Unit>(
            500,
            "{}".toResponseBody("application/json".toMediaType())
        )

        // When
        val result = tryCall<Int> {
            throw HttpException(response)
        }

        // Then
        assertEquals(Failure.Server(500), result.leftOrNull())
    }
}
