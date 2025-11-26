package com.example.centralis_kotlin.analytics.service

import com.example.centralis_kotlin.analytics.model.ViewRequest
import com.example.centralis_kotlin.analytics.model.ViewResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface AnalyticsApi {
    
    @POST("api/v1/analytics/announcements/{announcementId}/views")
    suspend fun registerAnnouncementView(
        @Header("Authorization") authorization: String,
        @Path("announcementId") announcementId: String,
        @Body viewRequest: ViewRequest
    ): Response<ViewResponse>
    
    @POST("api/v1/analytics/events/{eventId}/views")
    suspend fun registerEventView(
        @Header("Authorization") authorization: String,
        @Path("eventId") eventId: String,
        @Body viewRequest: ViewRequest
    ): Response<ViewResponse>
}