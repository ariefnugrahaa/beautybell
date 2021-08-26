package com.example.beautybell.data.artisan

import com.example.beautybell.data.artisan.remote.api.ArtisanApi
import com.example.beautybell.data.artisan.repository.ArtisanRepositoryImpl
import com.example.beautybell.data.common.module.NetworkModule
import com.example.beautybell.domain.artisan.ArtisanRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class ArtisanModule {

    @Singleton
    @Provides
    fun provideArtisanApi(retrofit: Retrofit) : ArtisanApi {
        return retrofit.create(ArtisanApi::class.java)
    }

    @Singleton
    @Provides
    fun provideArtisanRepository(artisanApi: ArtisanApi) : ArtisanRepository {
        return ArtisanRepositoryImpl(artisanApi)
    }
}