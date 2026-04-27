package com.adrc95.core.database.util

import android.database.SQLException
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteException
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.adrc95.domain.exception.Failure

suspend fun <T> tryCall(action: suspend () -> T): Either<Failure, T> = try {
    action().right()
} catch (e: SQLiteConstraintException) {
    e.toFailure().left()
} catch (e: SQLiteException) {
    e.toFailure().left()
} catch (e: SQLException) {
    e.toFailure().left()
} catch (e: NoSuchElementException) {
    e.toFailure().left()
}

private fun Throwable.toFailure(): Failure = when (this) {
    is SQLiteConstraintException -> Failure.Database(
        type = Failure.Database.Type.Constraint,
        message = message.orEmpty()
    )
    is SQLiteException, is SQLException -> Failure.Database(
        type = Failure.Database.Type.Generic,
        message = message.orEmpty()
    )
    is NoSuchElementException -> Failure.Unknown(message.orEmpty())
    else -> Failure.Unknown(message.orEmpty())
}
