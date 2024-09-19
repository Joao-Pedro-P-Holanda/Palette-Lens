package io.github.paletteLens.repository.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import io.github.paletteLens.domain.model.color.Palette
import io.github.paletteLens.util.DomainMapper
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Entity
data class PaletteEntity
    @OptIn(ExperimentalUuidApi::class)
    constructor(
        @PrimaryKey val paletteId: Uuid = Uuid.random(),
        val userId: String,
        @Embedded val metadata: PaletteMetadataEntity,
    )

@Entity(primaryKeys = ["paletteId", "colorId"])
data class PaletteColorEntitiesCrossRef
    @OptIn(ExperimentalUuidApi::class)
    constructor(
        val paletteId: Uuid,
        val colorId: Uuid,
    )

data class PaletteWithColors(
    @Embedded val palette: PaletteEntity,
    @Relation(
        parentColumn = "paletteId",
        entityColumn = "colorId",
        associateBy = Junction(PaletteColorEntitiesCrossRef::class),
    )
    val colors: List<ColorEntity>,
) {
    companion object Mapper : DomainMapper<PaletteWithColors, Palette> {
        @OptIn(ExperimentalUuidApi::class)
        override fun mapToDomainModel(model: PaletteWithColors): Palette {
            return Palette(
                id = model.palette.paletteId.toString(),
                userId = model.palette.userId,
                metadata = PaletteMetadataEntity.mapToDomainModel(model.palette.metadata),
                colors = model.colors.map { ColorEntity.mapToDomainModel(it) },
            )
        }

        @OptIn(ExperimentalUuidApi::class)
        override fun mapFromDomainModel(domainModel: Palette): PaletteWithColors {
            return PaletteWithColors(
                palette =
                    PaletteEntity(
                        paletteId = domainModel.id?.let { Uuid.parse(it) } ?: Uuid.random(),
                        userId = domainModel.userId!!,
                        metadata = PaletteMetadataEntity.mapFromDomainModel(domainModel.metadata),
                    ),
                colors = domainModel.colors.map { ColorEntity.mapFromDomainModel(it) },
            )
        }
    }
}
