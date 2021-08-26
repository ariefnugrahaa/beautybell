package com.example.beautybell.domain.artisan

import com.example.beautybell.domain.artisan.entity.ArtisanEntity
import com.example.beautybell.domain.common.base.BaseResult
import kotlinx.coroutines.flow.Flow

interface ArtisanRepository {
    suspend fun getAllArtisan() : Flow<BaseResult<List<ArtisanEntity>, String>>
}