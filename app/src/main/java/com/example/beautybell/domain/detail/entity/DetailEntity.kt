package com.example.beautybell.domain.detail.entity

import com.example.beautybell.data.detail.remote.dto.DetailResponse

data class DetailEntity(
    var id: String,
    var createdAt: String,
    var name: String,
    var avatar: String,
    var image: String,
    var user_image: String,
    var rating: String,
    var description: String,
    val services: List<DetailResponse.Service>

    )
