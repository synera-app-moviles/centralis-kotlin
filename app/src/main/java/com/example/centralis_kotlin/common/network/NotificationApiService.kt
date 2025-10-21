package com.example.centralis_kotlin.common.network

import retrofit2.Response
import retrofit2.http.*

// Data classes for API requests/responses
data class NotificationResponse(
    val id: String,
    val title: String,
    val message: String,
    val priority: String,
    val status: String,
    val createdAt: String,
    val updatedAt: String
)

data class PaginatedNotificationResponse(
    val content: List<NotificationResponse>,
    val page: PageInfo
)

data class PageInfo(
    val size: Int,
    val number: Int,
    val totalElements: Int,
    val totalPages: Int
)

data class CreateNotificationRequest(
    val title: String,
    val message: String,
    val recipients: List<String>,
    val priority: String // LOW, MEDIUM, HIGH, URGENT
)

interface NotificationApiService {
    
    @GET("api/v1/users/{userId}/notifications")
    suspend fun getUserNotifications(
        @Path("userId") userId: String,
        @Header("Authorization") authorization: String,
        @Query("status") status: String? = null,
        @Query("priority") priority: String? = null,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): Response<PaginatedNotificationResponse>
    
    @PUT("api/v1/notifications/{notificationId}/read")
    suspend fun markNotificationAsRead(
        @Path("notificationId") notificationId: String,
        @Header("Authorization") authorization: String
    ): Response<NotificationResponse>
    
    @DELETE("api/v1/notifications/{notificationId}")
    suspend fun deleteNotification(
        @Path("notificationId") notificationId: String,
        @Header("Authorization") authorization: String
    ): Response<Void>
    
    @POST("api/v1/notifications")
    suspend fun createNotification(
        @Body request: CreateNotificationRequest,
        @Header("Authorization") authorization: String
    ): Response<NotificationResponse>
}