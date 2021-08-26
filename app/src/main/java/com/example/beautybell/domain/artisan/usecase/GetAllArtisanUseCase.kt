package com.example.beautybell.domain.artisan.usecase

import com.example.beautybell.domain.artisan.ArtisanRepository
import com.example.beautybell.domain.artisan.entity.ArtisanEntity
import com.example.beautybell.domain.common.base.BaseResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllArtisanUseCase @Inject constructor(private val artisanRepository: ArtisanRepository){
    suspend fun invoke(): Flow<BaseResult<List<ArtisanEntity>, String>> {
        return artisanRepository.getAllArtisan()
    }
}