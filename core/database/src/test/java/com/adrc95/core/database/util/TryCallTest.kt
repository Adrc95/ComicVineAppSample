package com.adrc95.core.database.util

import android.database.SQLException
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteException
import com.adrc95.domain.exception.Failure
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TryCallTest {

    @Test
    fun `given successful action when try call then returns right value`() = runTest {
        // Given
        val expectedValue = 7

        // When
        val result = tryCall { expectedValue }

        // Then
        assertEquals(expectedValue, result.getOrNull())
    }

    @Test
    fun `given successful unit action when try call then returns unit`() = runTest {
        // When
        val result = tryCall {
            Unit
        }

        // Then
        assertEquals(Unit, result.getOrNull())
    }

    @Test
    fun `given sqlite constraint exception when try call then returns constraint database failure`() = runTest {
        // When
        val result = tryCall<Int> {
            throw SQLiteConstraintException("constraint")
        }

        // Then
        val failure = result.leftOrNull() as Failure.Database
        assertEquals(Failure.Database.Type.Constraint, failure.type)
    }

    @Test
    fun `given sqlite exception when try call then returns generic database failure`() = runTest {
        // When
        val result = tryCall<Int> {
            throw SQLiteException("sqlite")
        }

        // Then
        val failure = result.leftOrNull() as Failure.Database
        assertEquals(Failure.Database.Type.Generic, failure.type)
    }

    @Test
    fun `given sql exception when try call then returns generic database failure`() = runTest {
        // When
        val result = tryCall<Int> {
            throw SQLException("sql")
        }

        // Then
        val failure = result.leftOrNull() as Failure.Database
        assertEquals(Failure.Database.Type.Generic, failure.type)
    }

    @Test
    fun `given no such element exception when try call then returns unknown failure`() = runTest {
        // When
        val result = tryCall<Int> {
            throw NoSuchElementException("missing")
        }

        // Then
        assertEquals(Failure.Unknown("missing"), result.leftOrNull())
    }
}
