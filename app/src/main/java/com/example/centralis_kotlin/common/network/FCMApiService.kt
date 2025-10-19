package com.example.centralis_kotlin.common.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

data class FcmTokenRequest(
    val fcmToken: String,
    val deviceType: String = "Android",
    val deviceId: String
)

data class FcmTokenResponse(
    val fcmToken: String,
    val deviceType: String,
    val deviceId: String
)

interface FCMApiService {
    @POST("api/v1/users/{userId}/fcm-token")
    suspend fun registerFCMToken(
        @Path("userId") userId: String,
        @Body body: FcmTokenRequest,
        @retrofit2.http.Header("Authorization") authorization: String
    ): Response<Void>

    @GET("api/v1/users/{userId}/fcm-tokens")
    suspend fun getFCMTokens(
        @Path("userId") userId: String,
        @retrofit2.http.Header("Authorization") authorization: String
    ): Response<List<FcmTokenResponse>>
}
