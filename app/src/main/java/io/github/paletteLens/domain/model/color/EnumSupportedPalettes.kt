package io.github.paletteLens.domain.model.color

enum class EnumSupportedPalettes(val text: String) {
    MATERIAL("material-3"),
    ;

    companion object {
        infix fun from(text: String): EnumSupportedPalettes = entries.first { it.text == text }
    }
}
