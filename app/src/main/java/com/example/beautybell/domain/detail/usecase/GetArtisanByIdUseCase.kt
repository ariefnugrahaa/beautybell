package com.example.beautybell.domain.detail.usecase

import com.example.beautybell.domain.common.base.BaseResult
import com.example.beautybell.domain.detail.DetailRepository
import com.example.beautybell.domain.detail.entity.DetailEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetArtisanByIdUseCase @Inject constructor(private val detailRepository: DetailRepository) {
    suspend fun invoke(id: String) : Flow<BaseResult<DetailEntity, String>> {
        return detailRepository.getArtisanById(id)
    }
}