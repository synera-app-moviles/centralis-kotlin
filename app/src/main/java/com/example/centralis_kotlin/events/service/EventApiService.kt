package com.example.centralis_kotlin.events.service

import com.example.centralis_kotlin.events.model.CreateEventRequest
import com.example.centralis_kotlin.events.model.EventResponse
import com.example.centralis_kotlin.events.model.UpdateEventRequest
import retrofit2.Response
import retrofit2.http.*
import java.util.UUID

interface EventApiService {
    @GET("api/v1/events")
    suspend fun getEvents(
        @Header("Authorization") authorization: String,
        @Query("userId") userId: UUID? = null,
        @Query("filterType") filterType: String? = null
    ): Response<List<EventResponse>>

    @GET("api/v1/events/{eventId}")
    suspend fun getEventById(
        @Header("Authorization") authorization: String,
        @Path("eventId") eventId: UUID
    ): Response<EventResponse>

    @POST("api/v1/events")
    suspend fun createEvent(
        @Header("Authorization") authorization: String,
        @Body request: CreateEventRequest
    ): Response<EventResponse>

    @PUT("api/v1/events/{eventId}")
    suspend fun updateEvent(
        @Header("Authorization") authorization: String,
        @Path("eventId") eventId: UUID,
        @Body request: UpdateEventRequest
    ): Response<EventResponse>

    @DELETE("api/v1/events/{eventId}")
    suspend fun deleteEvent(
        @Header("Authorization") authorization: String,
        @Path("eventId") eventId: UUID
    ): Response<Unit>
}
