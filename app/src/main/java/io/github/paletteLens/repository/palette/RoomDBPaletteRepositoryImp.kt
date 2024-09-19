package io.github.paletteLens.repository.palette

import android.util.Log
import androidx.room.Transaction
import io.github.paletteLens.domain.model.UserState
import io.github.paletteLens.domain.model.color.Palette
import io.github.paletteLens.repository.dao.RoomColorDAO
import io.github.paletteLens.repository.dao.RoomPaletteDAO
import io.github.paletteLens.repository.model.PaletteColorEntitiesCrossRef
import io.github.paletteLens.repository.model.PaletteWithColors
import io.github.paletteLens.service.auth.AuthService
import javax.inject.Inject
import kotlin.uuid.ExperimentalUuidApi

class RoomDBPaletteRepositoryImp
    @Inject
    constructor(
        private val paletteDAO: RoomPaletteDAO,
        private val colorDAO: RoomColorDAO,
        private val authService: AuthService,
    ) : PaletteRepository {
        override suspend fun listPalettes(): List<Palette> {
            try {
                val currentUserState =
                    authService.user.value as? UserState.Loaded ?: throw Exception("User not loaded")
                val entities = paletteDAO.getPalettes(currentUserState.user.uid)
                return entities.map { PaletteWithColors.mapToDomainModel(it) }
            } catch (e: Exception) {
                Log.e("RoomDBPaletteRepositoryImp", "Error getting palettes", e)
                throw e
            }
        }

        override suspend fun <T> getPalette(id: T): Palette {
            TODO("Not yet implemented")
        }

        @OptIn(ExperimentalUuidApi::class)
        @Transaction
        override suspend fun createPalette(palette: Palette): Palette {
            try {
                val paletteWithColors = PaletteWithColors.mapFromDomainModel(palette)
                paletteDAO.createPalette(paletteWithColors.palette)
                colorDAO.createColorRepresentations(paletteWithColors.colors)
                val crossRefs =
                    paletteWithColors.colors.map {
                        PaletteColorEntitiesCrossRef(
                            paletteId = paletteWithColors.palette.paletteId,
                            colorId = it.colorId,
                        )
                    }
                paletteDAO.createPaletteColors(crossRefs)
                palette.id = paletteWithColors.palette.paletteId.toString()
                return palette
            } catch (e: Exception) {
                Log.e("RoomDBPaletteRepositoryImp", "Error creating palette", e)
                throw e
            }
        }

        override suspend fun deletePalette(palette: Palette): Boolean {
            TODO("Not yet implemented")
        }

        override suspend fun updatePalette(palette: Palette): Palette {
            TODO("Not yet implemented")
        }
    }
