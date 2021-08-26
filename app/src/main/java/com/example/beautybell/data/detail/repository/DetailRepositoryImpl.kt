package com.example.beautybell.data.detail.repository

import com.example.beautybell.data.detail.remote.api.DetailApi
import com.example.beautybell.domain.common.base.BaseResult
import com.example.beautybell.domain.detail.DetailRepository
import com.example.beautybell.domain.detail.entity.DetailEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DetailRepositoryImpl @Inject constructor(private val detailApi: DetailApi): DetailRepository {
    override suspend fun getArtisanById(id: String): Flow<BaseResult<DetailEntity, String>> {
        return flow {
            val response = detailApi.getArtisanById(id)
            if (response.isSuccessful) {
                val body = response.body()!!
                val detailArtisan = DetailEntity(
                    body.id,
                    body.createdAt,
                    body.name,
                    body.avatar,
                    body.image,
                    body.userImage,
                    body.rating,
                    body.description,
                    body.services
                )
                emit(BaseResult.Success(detailArtisan))
            } else {
                val errorMessage = response.errorBody()?.toString()
                emit(BaseResult.Error(errorMessage!!))
            }
        }
    }
}
