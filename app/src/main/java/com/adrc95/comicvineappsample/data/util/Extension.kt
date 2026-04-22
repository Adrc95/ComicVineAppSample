package com.adrc95.comicvineappsample.data.util

import android.database.SQLException
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteException
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.adrc95.domain.exception.Failure
import java.io.IOException
import retrofit2.HttpException

@Suppress("TooGenericExceptionCaught")
suspend fun <T> tryCall(action: suspend () -> T): Either<Failure, T> = try {
    action().right()
} catch (e: Exception) {
    e.toFailure().left()
}

fun Throwable.toFailure(): Failure = when (this) {
    is IOException -> Failure.Connectivity
    is HttpException -> Failure.Server(code())
    is SQLiteConstraintException -> Failure.Database(
        type = Failure.Database.Type.Constraint,
        message = message.orEmpty()
    )
    is SQLiteException, is SQLException -> Failure.Database(
        type = Failure.Database.Type.Generic,
        message = message.orEmpty()
    )
    else -> Failure.Unknown(message.orEmpty())
}
