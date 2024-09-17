package io.github.paletteLens.service.auth

import io.github.paletteLens.domain.model.UserState
import kotlinx.coroutines.flow.MutableStateFlow

abstract class AuthService {
    abstract var user: MutableStateFlow<UserState>

    /**
     * Sign in with email and password.
     */
    abstract suspend fun signIn(
        email: String,
        password: String,
    )

    /**
     * Creates a new account.
     */
    abstract suspend fun signUp(
        email: String,
        password: String,
    )

    /**
     * Sign out the current user.
     */
    abstract suspend fun signOut()

    /**
     * Check if a user is logged in.
     */
    fun isUserLoggedIn(): Boolean {
        return user.value is UserState.Loaded
    }
}
