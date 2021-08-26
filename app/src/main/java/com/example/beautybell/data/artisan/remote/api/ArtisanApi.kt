package com.example.beautybell.data.artisan.remote.api

import com.example.beautybell.data.artisan.remote.dto.ArtisanResponse
import retrofit2.Response
import retrofit2.http.GET

interface ArtisanApi {
    @GET("list-artisan")
    suspend fun getAllArtisan() : Response<List<ArtisanResponse>>
}