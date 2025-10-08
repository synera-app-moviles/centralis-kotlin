package com.example.centralis_kotlin.iam.services

import com.example.centralis_kotlin.iam.models.*
import retrofit2.Response
import retrofit2.http.*

interface IAMWebService {
    
    @POST("api/v1/auth/sign-in")
    suspend fun signIn(@Body request: SignInRequest): Response<AuthResponse>
    
    @POST("api/v1/auth/sign-up")
    suspend fun signUp(@Body request: SignUpRequest): Response<AuthResponse>
    
    @GET("api/v1/users")
    suspend fun getAllUsers(@Header("Authorization") token: String): Response<List<User>>
    
    @GET("api/v1/users/{userId}")
    suspend fun getUserById(
        @Path("userId") userId: String,
        @Header("Authorization") token: String
    ): Response<User>
    
    @PUT("api/v1/users/{userId}")
    suspend fun updateUserPassword(
        @Path("userId") userId: String,
        @Body request: UpdatePasswordRequest,
        @Header("Authorization") token: String
    ): Response<User>
    
    @GET("api/v1/roles")
    suspend fun getAllRoles(@Header("Authorization") token: String): Response<List<Role>>
}