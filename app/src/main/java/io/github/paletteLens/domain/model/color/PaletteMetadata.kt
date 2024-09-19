package io.github.paletteLens.domain.model.color

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PaletteMetadata(
    var id: String? = null,
    var name: String? = null,
    val kind: EnumSupportedPalettes,
    val numberOfColors: Int,
)
