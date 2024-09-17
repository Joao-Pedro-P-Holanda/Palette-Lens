package io.github.paletteLens.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.paletteLens.service.auth.AuthService
import io.github.paletteLens.service.auth.AuthServiceFirebaseImp
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {
    @Provides
    @Singleton
    fun provideAuthRepository(): AuthService {
        return AuthServiceFirebaseImp()
    }
}
