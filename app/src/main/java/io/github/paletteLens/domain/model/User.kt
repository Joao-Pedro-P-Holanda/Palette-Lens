package io.github.paletteLens.domain.model

data class User(
    val uid: String,
    val name: String,
    val email: String,
    val profilePicture: String?,
)
