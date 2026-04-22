package com.adrc95.domain.exception


sealed class Failure {
    object Connectivity : Failure()
    data class Server(val code: Int) : Failure()
    data class Database(
        val type: Type = Type.Generic,
        val message: String = ""
    ) : Failure() {
        enum class Type {
            Constraint,
            Generic
        }
    }
    data class Unknown(val message: String) : Failure()
}
