package io.github.paletteLens.domain.model.color

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PaletteMetadata(
    val kind: EnumSupportedPalettes,
    val numberOfColors: Int,
)
