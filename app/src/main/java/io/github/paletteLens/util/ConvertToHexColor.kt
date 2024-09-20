package io.github.paletteLens.util

class ConvertToHexColor {
    companion object {
        fun correctString(string: String): String {
            val converted = if (string.startsWith("#")) string.uppercase() else "#${string.uppercase()}"
            return converted
        }
    }
}
