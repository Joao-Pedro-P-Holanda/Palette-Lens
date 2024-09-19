package io.github.paletteLens.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.paletteLens.AppDatabase
import io.github.paletteLens.repository.GeminiExtractorRepositoryImp
import io.github.paletteLens.repository.dao.RoomColorDAO
import io.github.paletteLens.repository.dao.RoomPaletteDAO
import io.github.paletteLens.repository.extractor.PaletteExtractorRepository
import io.github.paletteLens.repository.palette.PaletteRepository
import io.github.paletteLens.repository.palette.RoomDBPaletteRepositoryImp
import io.github.paletteLens.service.auth.AuthService
import io.github.paletteLens.util.JSONConverter
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun providePaletteExtractorRepository(
        jsonConverter: JSONConverter,
        authService: AuthService,
    ): PaletteExtractorRepository {
        return GeminiExtractorRepositoryImp(jsonConverter, authService)
    }

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext applicationContext: Context,
    ): AppDatabase {
        return Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "palette_database",
        ).build()
    }

    @Provides
    @Singleton
    fun providePaletteDao(appDatabase: AppDatabase): RoomPaletteDAO {
        return appDatabase.paletteDao()
    }

    @Provides
    @Singleton
    fun provideColorDao(appDatabase: AppDatabase): RoomColorDAO {
        return appDatabase.colorDao()
    }

    @Provides
    fun providePaletteRepository(
        paletteDAO: RoomPaletteDAO,
        colorDAO: RoomColorDAO,
        authService: AuthService,
    ): PaletteRepository {
        return RoomDBPaletteRepositoryImp(paletteDAO, colorDAO, authService)
    }
}
