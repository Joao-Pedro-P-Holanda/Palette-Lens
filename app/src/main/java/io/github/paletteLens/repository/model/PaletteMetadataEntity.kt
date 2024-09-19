package io.github.paletteLens.repository.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import io.github.paletteLens.domain.model.color.EnumSupportedPalettes
import io.github.paletteLens.domain.model.color.PaletteMetadata
import io.github.paletteLens.util.DomainMapper
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Entity
data class PaletteMetadataEntity
    @OptIn(ExperimentalUuidApi::class)
    constructor(
        @PrimaryKey val metadataId: Uuid = Uuid.random(),
        val kind: String,
        val numberOfColors: Int,
    ) {
        companion object Mapper : DomainMapper<PaletteMetadataEntity, PaletteMetadata> {
            @OptIn(ExperimentalUuidApi::class)
            override fun mapToDomainModel(model: PaletteMetadataEntity): PaletteMetadata {
                return PaletteMetadata(
                    id = model.metadataId.toString(),
                    kind = EnumSupportedPalettes.from(model.kind),
                    numberOfColors = model.numberOfColors,
                )
            }

            @OptIn(ExperimentalUuidApi::class)
            override fun mapFromDomainModel(domainModel: PaletteMetadata): PaletteMetadataEntity {
                return PaletteMetadataEntity(
                    metadataId = domainModel.id?.let { Uuid.parse(it) } ?: Uuid.random(),
                    kind = domainModel.kind.text,
                    numberOfColors = domainModel.numberOfColors,
                )
            }
        }
    }
