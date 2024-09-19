package io.github.paletteLens.domain.model.color

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ColorRepresentation(
    val id: String? = null,
    val hex: String,
    val rgb: String,
    val hsl: String,
    val hsv: String,
    val name: String?,
)

// TODO("Create classes for different color visualizations and make custom adapters for them")
