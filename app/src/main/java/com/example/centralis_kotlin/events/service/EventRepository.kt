package com.example.centralis_kotlin.events.service

    import com.example.centralis_kotlin.events.model.Event
    import com.example.centralis_kotlin.events.model.CreateEventRequest
    import com.example.centralis_kotlin.events.model.UpdateEventRequest
    import java.util.UUID

    interface EventRepository {
        suspend fun createEvent(request: CreateEventRequest): Result<Event>
        suspend fun getEventById(eventId: UUID): Result<Event>
        suspend fun getAllEvents(): Result<List<Event>>
        suspend fun getEventsByRecipient(userId: UUID): Result<List<Event>>
        suspend fun getEventsByCreator(userId: UUID): Result<List<Event>>
        suspend fun updateEvent(eventId: UUID, request: UpdateEventRequest): Result<Event>
        suspend fun deleteEvent(eventId: UUID): Result<Unit>
    }