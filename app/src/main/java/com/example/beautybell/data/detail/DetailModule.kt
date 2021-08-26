package com.example.beautybell.data.detail

import com.example.beautybell.data.common.module.NetworkModule
import com.example.beautybell.data.detail.remote.api.DetailApi
import com.example.beautybell.data.detail.repository.DetailRepositoryImpl
import com.example.beautybell.domain.detail.DetailRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class DetailModule {

    @Singleton
    @Provides
    fun provideDetailApi(retrofit: Retrofit) : DetailApi {
        return retrofit.create(DetailApi::class.java)
    }

    @Singleton
    @Provides
    fun provideDetailRepository(detailApi: DetailApi) : DetailRepository {
        return DetailRepositoryImpl(detailApi)
    }
}