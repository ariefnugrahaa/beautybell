package com.example.beautybell.data.detail.remote.api

import com.example.beautybell.data.detail.remote.dto.DetailResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface DetailApi {
    @GET("list-artisan/{id}")
    suspend fun getArtisanById(@Path("id") id: String) : Response<DetailResponse>
}