package io.github.paletteLens.domain.model

sealed class UserState {
    object Loading : UserState()

    data class Loaded(val user: User) : UserState()

    object None : UserState()
}
