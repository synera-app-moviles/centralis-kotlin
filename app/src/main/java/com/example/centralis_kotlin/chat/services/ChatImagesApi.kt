package com.example.centralis_kotlin.chat.services

import com.example.centralis_kotlin.chat.domain.models.ChatImageRequest
import com.example.centralis_kotlin.chat.domain.models.ChatImageResponse
import retrofit2.Response
import retrofit2.http.*

interface ChatImagesApi {
    
    @POST("api/v1/groups/{groupId}/images")
    suspend fun shareImage(
        @Header("Authorization") token: String,
        @Path("groupId") groupId: String,
        @Body request: ChatImageRequest
    ): Response<ChatImageResponse>
    
    @GET("api/v1/groups/{groupId}/images")
    suspend fun getGroupImages(
        @Path("groupId") groupId: String,
        @Header("Authorization") token: String
    ): Response<List<ChatImageResponse>>
    
    @DELETE("api/v1/groups/{groupId}/images/{imageId}")
    suspend fun deleteImage(
        @Path("groupId") groupId: String,
        @Path("imageId") imageId: String,
        @Header("Authorization") token: String
    ): Response<Unit>
}