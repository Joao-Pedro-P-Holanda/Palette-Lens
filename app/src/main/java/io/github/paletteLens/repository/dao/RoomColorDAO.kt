package io.github.paletteLens.repository.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import io.github.paletteLens.repository.model.ColorEntity

@Dao
interface RoomColorDAO {
    @Insert
    suspend fun createColorRepresentation(colorEntity: ColorEntity)

    @Insert
    suspend fun createColorRepresentations(colorEntities: List<ColorEntity>)

    @Update
    suspend fun updateColorRepresentation(colorEntity: ColorEntity)

    @Delete
    suspend fun deleteColorRepresentation(colorEntity: ColorEntity)

    @Query("SELECT * FROM ColorEntity WHERE colorId = :colorId")
    suspend fun getColorRepresentation(colorId: String): ColorEntity

    @Query("SELECT * FROM ColorEntity")
    suspend fun getColorRepresentations(): List<ColorEntity>
}
