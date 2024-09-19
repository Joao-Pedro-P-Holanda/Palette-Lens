package io.github.paletteLens

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.github.paletteLens.repository.dao.RoomColorDAO
import io.github.paletteLens.repository.dao.RoomPaletteDAO
import io.github.paletteLens.repository.model.ColorEntity
import io.github.paletteLens.repository.model.PaletteColorEntitiesCrossRef
import io.github.paletteLens.repository.model.PaletteEntity
import io.github.paletteLens.repository.model.UserEntity
import io.github.paletteLens.util.UuidConverter

@Database(
    entities = [UserEntity::class, PaletteEntity::class, ColorEntity::class, PaletteColorEntitiesCrossRef::class],
    version = 1,
    exportSchema = false,
)
@TypeConverters(UuidConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun colorDao(): RoomColorDAO

    abstract fun paletteDao(): RoomPaletteDAO
}
