package com.example.beautybell.domain.detail

import com.example.beautybell.domain.common.base.BaseResult
import com.example.beautybell.domain.detail.entity.DetailEntity
import kotlinx.coroutines.flow.Flow

interface DetailRepository {
    suspend fun getArtisanById(id: String) : Flow<BaseResult<DetailEntity, String>>
}