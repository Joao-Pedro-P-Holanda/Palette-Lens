package io.github.paletteLens.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.paletteLens.util.JSONConverter
import io.github.paletteLens.util.MoshiJSONConverterImp
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UtilModule {
    @Provides
    @Singleton
    fun provideJSONConverter(): JSONConverter {
        return MoshiJSONConverterImp()
    }
}
