package io.github.paletteLens.util

import androidx.room.TypeConverter
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class UuidConverter {
    @OptIn(ExperimentalUuidApi::class)
    @TypeConverter
    fun toUuid(value: String): Uuid {
        return Uuid.parse(value)
    }

    @OptIn(ExperimentalUuidApi::class)
    @TypeConverter
    fun fromUuid(uuid: Uuid): String {
        return uuid.toString()
    }
}
