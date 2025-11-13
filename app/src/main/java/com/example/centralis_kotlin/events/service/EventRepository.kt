package com.example.centralis_kotlin.events.service

    import com.example.centralis_kotlin.events.model.EventResponse
    import com.example.centralis_kotlin.events.model.CreateEventRequest
    import com.example.centralis_kotlin.events.model.UpdateEventRequest
    import java.util.UUID

    interface EventRepository {
        suspend fun createEvent(request: CreateEventRequest): Result<EventResponse>
        suspend fun getEventById(eventId: UUID): Result<EventResponse>
        suspend fun getAllEvents(): Result<List<EventResponse>>
        suspend fun getEventsByRecipient(userId: UUID): Result<List<EventResponse>>
        suspend fun getEventsByCreator(userId: UUID): Result<List<EventResponse>>
        suspend fun updateEvent(eventId: UUID, request: UpdateEventRequest): Result<EventResponse>
        suspend fun deleteEvent(eventId: UUID): Result<Unit>
    }