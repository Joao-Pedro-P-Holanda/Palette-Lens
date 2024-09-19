package io.github.paletteLens.repository.palette

import io.github.paletteLens.domain.model.color.Palette

interface PaletteRepository {
    suspend fun listPalettes(): List<Palette>

    suspend fun <T> getPalette(id: T): Palette

    suspend fun createPalette(palette: Palette): Palette

    suspend fun deletePalette(palette: Palette): Boolean

    suspend fun updatePalette(palette: Palette): Palette
}
