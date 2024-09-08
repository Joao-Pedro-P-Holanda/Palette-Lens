package io.github.paletteLens.repository

import android.graphics.Bitmap
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.Schema
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import io.github.paletteLens.BuildConfig
import io.github.paletteLens.domain.model.color.EnumSupportedPalettes
import io.github.paletteLens.domain.model.color.Palette
import io.github.paletteLens.domain.model.color.PaletteMetadata
import io.github.paletteLens.repository.extractor.PaletteExtractorRepository
import io.github.paletteLens.util.JSONConverter
import javax.inject.Inject

val schema =
    Schema.obj(
        "palette",
        "palette object",
        Schema.obj(
            "metadata",
            "metadata of the color palette",
            Schema.str("kind", "kind of the palette"),
            Schema.int("numberOfColors", "number of colors in the palette"),
        ),
        Schema.arr(
            "colors",
            "array of each color in the palette",
            Schema.obj(
                "color",
                "color object",
                Schema.str("hex", "hexadecimal representation of the color"),
                Schema.str("rgb", "rgb representation of the color"),
                Schema.str("hsl", "hsl representation of the color"),
                Schema.str("hsv", "hsv representation of the color"),
                Schema.str("name", "name of the color"),
            ),
        ),
    )

class GeminiExtractorRepositoryImp
    @Inject
    constructor(private val jsonConverter: JSONConverter) : PaletteExtractorRepository {
        private val generativeModel =
            GenerativeModel(
                "gemini-1.5-flash",
                apiKey = BuildConfig.geminiApiKey,
                generationConfig =
                    generationConfig {
                        responseMimeType = "application/json"
                        responseSchema = schema
                    },
            )

        override suspend fun extractPalette(
            metadata: PaletteMetadata,
            image: Bitmap,
        ): Palette {
            val response =
                generativeModel.generateContent(
                    content {
                        image(image)
                        text(getTextForPaletteMetadata(metadata))
                    },
                )

            val responseJson = response.text ?: throw Exception("Error extracting palette from image")
            val result = jsonConverter.fromJson(responseJson, Palette::class.java)
            return result
        }

        /**
         * Returns the text to be used for querying the generative model for a color palette
         */
        private fun getTextForPaletteMetadata(metadata: PaletteMetadata): String {
            when (metadata.kind) {
                EnumSupportedPalettes.MATERIAL -> {
                    return """Extract the color palette of the image using the exact ${metadata.kind.name} palette kind," 
                   with ${metadata.numberOfColors} colors
                 """
                }
            }
        }
    }
