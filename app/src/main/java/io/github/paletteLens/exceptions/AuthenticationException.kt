package io.github.paletteLens.exceptions

sealed class AuthenticationException(message: String, cause: Throwable) : Exception(message, cause) {
    data class InvalidEmailException(override val message: String, override val cause: Throwable) : AuthenticationException(message, cause)

    data class InvalidPasswordException(override val message: String, override val cause: Throwable) : AuthenticationException(
        message,
        cause,
    )

    data class WeakPasswordException(override val message: String, override val cause: Throwable) : AuthenticationException(
        message,
        cause,
    )

    data class TooManyRequestsException(override val message: String, override val cause: Throwable) : AuthenticationException(
        message,
        cause,
    )
}
