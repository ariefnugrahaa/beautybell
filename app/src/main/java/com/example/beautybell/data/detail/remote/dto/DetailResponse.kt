package com.example.beautybell.data.detail.remote.dto

import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.google.gson.annotations.Expose

@Keep
data class DetailResponse(
    @SerializedName("avatar")
    @Expose
    val avatar: String,
    @SerializedName("createdAt")
    @Expose
    val createdAt: String,
    @SerializedName("description")
    @Expose
    val description: String,
    @SerializedName("id")
    @Expose
    val id: String,
    @SerializedName("image")
    @Expose
    val image: String,
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("rating")
    @Expose
    val rating: String,
    @SerializedName("services")
    @Expose
    val services: List<Service>,
    @SerializedName("user_image")
    @Expose
    val userImage: String
) {
    @Keep
    data class Service(
        @SerializedName("caption")
        @Expose
        val caption: String,
        @SerializedName("name")
        @Expose
        val name: String,
        @SerializedName("price")
        @Expose
        val price: String
    )
}