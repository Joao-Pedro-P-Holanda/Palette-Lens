package io.github.paletteLens.repository.extractor

import android.graphics.Bitmap
import io.github.paletteLens.domain.model.color.Palette
import io.github.paletteLens.domain.model.color.PaletteMetadata

interface PaletteExtractorRepository {
    suspend fun extractPalette(
        metadata: PaletteMetadata,
        image: Bitmap,
    ): Palette
}
