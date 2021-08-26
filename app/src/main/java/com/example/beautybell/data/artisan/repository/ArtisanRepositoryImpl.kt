package com.example.beautybell.data.artisan.repository

import com.example.beautybell.data.artisan.remote.api.ArtisanApi
import com.example.beautybell.domain.artisan.ArtisanRepository
import com.example.beautybell.domain.artisan.entity.ArtisanEntity
import com.example.beautybell.domain.common.base.BaseResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ArtisanRepositoryImpl @Inject constructor(private val artisanApi: ArtisanApi): ArtisanRepository {
    override suspend fun getAllArtisan(): Flow<BaseResult<List<ArtisanEntity>, String>> {
        return flow {
            val response = artisanApi.getAllArtisan()
            if (response.isSuccessful){
                val body = response.body()!!
                val artisans = mutableListOf<ArtisanEntity>()
                body.forEach { artisanResponse ->
                    artisans.add(
                        ArtisanEntity(
                        artisanResponse.id,
                        artisanResponse.avatar,
                            artisanResponse.createdAt,
                        artisanResponse.description,
                        artisanResponse.image,
                        artisanResponse.name,
                        artisanResponse.rating,
                            artisanResponse.services,
                            artisanResponse.userImage,
                    )
                    )
                }
                emit(BaseResult.Success(artisans))
            }else{
                val errorMessage = response.errorBody()?.toString()
                emit(BaseResult.Error(errorMessage!!))
            }
        }
    }
}
