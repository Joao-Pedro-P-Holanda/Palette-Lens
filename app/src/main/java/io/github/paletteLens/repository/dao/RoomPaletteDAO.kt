package io.github.paletteLens.repository.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import io.github.paletteLens.repository.model.PaletteColorEntitiesCrossRef
import io.github.paletteLens.repository.model.PaletteEntity
import io.github.paletteLens.repository.model.PaletteWithColors

@Dao
interface RoomPaletteDAO {
    @Insert
    suspend fun createPalette(palette: PaletteEntity)

    @Insert
    suspend fun createPaletteColors(paletteColorEntitiesCrossRef: List<PaletteColorEntitiesCrossRef>)

    @Update
    suspend fun updatePalette(palette: PaletteEntity)

    @Delete
    suspend fun deletePalette(palette: PaletteEntity)

    @Transaction
    @Query("SELECT * FROM PaletteEntity WHERE paletteId = :paletteId")
    suspend fun getPalette(paletteId: String): PaletteWithColors

    @Transaction
    @Query("SELECT * FROM PaletteEntity WHERE userId = :userId")
    suspend fun getPalettes(userId: String): List<PaletteWithColors>
}
