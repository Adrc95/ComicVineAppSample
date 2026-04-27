package com.adrc95.core.network.util

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.adrc95.domain.exception.Failure
import retrofit2.HttpException
import java.io.IOException

suspend fun <T> tryCall(action: suspend () -> T): Either<Failure, T> = try {
    action().right()
} catch (e: IOException) {
    e.toFailure().left()
} catch (e: HttpException) {
    e.toFailure().left()
}

private fun Throwable.toFailure(): Failure = when (this) {
    is IOException -> Failure.Connectivity
    is HttpException -> Failure.Server(code())
    else -> Failure.Unknown(message.orEmpty())
}
