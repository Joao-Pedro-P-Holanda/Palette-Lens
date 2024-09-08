package io.github.paletteLens.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.paletteLens.repository.GeminiExtractorRepositoryImp
import io.github.paletteLens.repository.extractor.PaletteExtractorRepository
import io.github.paletteLens.util.JSONConverter
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun providePaletteExtractorRepository(jsonConverter: JSONConverter): PaletteExtractorRepository {
        return GeminiExtractorRepositoryImp(jsonConverter)
    }
}
