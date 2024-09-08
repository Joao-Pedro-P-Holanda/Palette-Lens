package io.github.paletteLens.util

interface JSONConverter {
    fun <T> fromJson(
        json: String,
        clazz: Class<T>,
    ): T

    fun <T> toJson(obj: T?): String
}
