package com.example.centralis_kotlin.profile.services

import com.example.centralis_kotlin.profile.models.*
import retrofit2.Response
import retrofit2.http.*

interface ProfileWebService {
    
    @POST("api/v1/profiles")
    suspend fun createProfile(
        @Body request: ProfileRequest,
        @Header("Authorization") token: String
    ): Response<ProfileResponse>
    
    @GET("api/v1/profiles/{profileId}")
    suspend fun getProfileById(
        @Path("profileId") profileId: String,
        @Header("Authorization") token: String
    ): Response<ProfileResponse>
    
    @GET("api/v1/profiles/user/{userId}")
    suspend fun getProfileByUserId(
        @Path("userId") userId: String,
        @Header("Authorization") token: String
    ): Response<ProfileResponse>
    
    @GET("api/v1/profiles")
    suspend fun getAllProfiles(@Header("Authorization") token: String): Response<List<ProfileResponse>>
    
    @PUT("api/v1/profiles/{profileId}")
    suspend fun updateProfile(
        @Path("profileId") profileId: String,
        @Body request: UpdateProfileRequest,
        @Header("Authorization") token: String
    ): Response<ProfileResponse>
}