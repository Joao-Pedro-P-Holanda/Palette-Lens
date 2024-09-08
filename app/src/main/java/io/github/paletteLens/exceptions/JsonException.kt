package io.github.paletteLens.exceptions

enum class JsonErrorType { LOAD, READ, CONVERT }

sealed class JsonException(type: JsonErrorType, message: String, cause: Throwable) : Exception(message, cause) {
    data class LoadError(override val message: String, override val cause: Throwable) : JsonException(
        type = JsonErrorType.LOAD,
        message = message,
        cause = cause,
    )

    data class ReadError(override val message: String, override val cause: Throwable) : JsonException(
        type = JsonErrorType.READ,
        message = message,
        cause = cause,
    )

    data class ConvertError(override val message: String, override val cause: Throwable) : JsonException(
        type = JsonErrorType.CONVERT,
        message = message,
        cause = cause,
    )
}
