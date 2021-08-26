package com.example.beautybell.domain.artisan.entity

import com.example.beautybell.data.artisan.remote.dto.ArtisanResponse

data class ArtisanEntity(
    var id: String,
    var avatar: String?,
    var createdAt: String,
    var description: String,
    var image: String,
    var name: String,
    var rating:String,
    var services: List<ArtisanResponse.Service>,
    var userImage: String
)