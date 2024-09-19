package io.github.paletteLens.domain.model.color

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Palette(var id: String? = null, var userId: String?, val metadata: PaletteMetadata, val colors: List<ColorRepresentation>)
