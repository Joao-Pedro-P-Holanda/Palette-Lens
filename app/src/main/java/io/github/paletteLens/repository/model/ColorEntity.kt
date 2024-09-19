package io.github.paletteLens.repository.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import io.github.paletteLens.domain.model.color.ColorRepresentation
import io.github.paletteLens.util.DomainMapper
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Entity
data class ColorEntity
    @OptIn(ExperimentalUuidApi::class)
    constructor(
        @PrimaryKey val colorId: Uuid = Uuid.random(),
        val hex: String,
        val rgb: String,
        val hsl: String,
        val hsv: String,
        val name: String?,
    ) {

        companion object Mapper : DomainMapper<ColorEntity, ColorRepresentation> {
            @OptIn(ExperimentalUuidApi::class)
            override fun mapToDomainModel(model: ColorEntity): ColorRepresentation {
                return ColorRepresentation(
                    id = model.colorId.toString(),
                    hex = model.hex,
                    rgb = model.rgb,
                    hsl = model.hsl,
                    hsv = model.hsv,
                    name = model.name,
                )
            }

            @OptIn(ExperimentalUuidApi::class)
            override fun mapFromDomainModel(domainModel: ColorRepresentation): ColorEntity {
                return ColorEntity(
                    colorId = domainModel.id?.let { Uuid.parse(it) } ?: Uuid.random(),
                    hex = domainModel.hex,
                    rgb = domainModel.rgb,
                    hsl = domainModel.hsl,
                    hsv = domainModel.hsv,
                    name = domainModel.name,
                )
            }
        }
    }
