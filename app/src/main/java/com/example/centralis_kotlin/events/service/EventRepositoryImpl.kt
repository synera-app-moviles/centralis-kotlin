package com.example.centralis_kotlin.events.service

import android.util.Log
import com.example.centralis_kotlin.events.model.CreateEventRequest
import com.example.centralis_kotlin.events.model.Event
import com.example.centralis_kotlin.events.model.UpdateEventRequest
import java.util.UUID
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(
    private val eventApiService: EventApiService,
    private val tokenProvider: () -> String?
) : EventRepository {

    private companion object {
        const val TAG = "EventRepositoryImpl"
    }

    private fun getAuthHeader(): String {
        val token = tokenProvider() ?: throw IllegalStateException("Token no disponible")
        return "Bearer $token"
    }

    override suspend fun createEvent(request: CreateEventRequest): Result<Event> {
        return try {
            Log.d(TAG, "Creating event: ${request.title}")
            val response = eventApiService.createEvent(
                authorization = getAuthHeader(),
                request = request
            )

            if (response.isSuccessful) {
                val eventResponse = response.body()
                if (eventResponse != null) {
                    val event = Event(
                        id = eventResponse.id,
                        title = eventResponse.title,
                        description = eventResponse.description,
                        date = eventResponse.date,
                        location = eventResponse.location,
                        createdBy = eventResponse.createdBy,
                        recipientIds = eventResponse.recipientIds,
                        createdAt = eventResponse.createdAt,
                        updatedAt = eventResponse.updatedAt
                    )
                    Result.success(event)
                } else {
                    Result.failure(Exception("Respuesta vacía del servidor"))
                }
            } else {
                val errorBody = response.errorBody()?.string() ?: "Error desconocido"
                Log.e(TAG, "Error creating event: ${response.code()} - $errorBody")
                Result.failure(Exception("Error del servidor: ${response.code()} - $errorBody"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception creating event", e)
            Result.failure(e)
        }
    }

    override suspend fun getEventById(eventId: UUID): Result<Event> {
        return try {
            val response = eventApiService.getEventById(getAuthHeader(), eventId)
            if (response.isSuccessful && response.body() != null) {
                val eventResponse = response.body()!!
                Result.success(Event(
                    id = eventResponse.id,
                    title = eventResponse.title,
                    description = eventResponse.description,
                    date = eventResponse.date,
                    location = eventResponse.location,
                    createdBy = eventResponse.createdBy,
                    recipientIds = eventResponse.recipientIds,
                    createdAt = eventResponse.createdAt,
                    updatedAt = eventResponse.updatedAt
                ))
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAllEvents(): Result<List<Event>> {
        return try {
            val response = eventApiService.getAllEvents(getAuthHeader())
            if (response.isSuccessful && response.body() != null) {
                val events = response.body()!!.map { eventResponse ->
                    Event(
                        id = eventResponse.id,
                        title = eventResponse.title,
                        description = eventResponse.description,
                        date = eventResponse.date,
                        location = eventResponse.location,
                        createdBy = eventResponse.createdBy,
                        recipientIds = eventResponse.recipientIds,
                        createdAt = eventResponse.createdAt,
                        updatedAt = eventResponse.updatedAt
                    )
                }
                Result.success(events)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getEventsByRecipient(userId: UUID): Result<List<Event>> {
        return try {
            val response = eventApiService.getEvents(
                authorization = getAuthHeader(),
                userId = userId,
                filterType = "recipient"
            )
            if (response.isSuccessful && response.body() != null) {
                val events = response.body()!!.map { eventResponse ->
                    Event(
                        id = eventResponse.id,
                        title = eventResponse.title,
                        description = eventResponse.description,
                        date = eventResponse.date,
                        location = eventResponse.location,
                        createdBy = eventResponse.createdBy,
                        recipientIds = eventResponse.recipientIds,
                        createdAt = eventResponse.createdAt,
                        updatedAt = eventResponse.updatedAt
                    )
                }
                Result.success(events)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getEventsByCreator(userId: UUID): Result<List<Event>> {
        return try {
            val response = eventApiService.getEvents(
                authorization = getAuthHeader(),
                userId = userId,
                filterType = "creator"
            )
            if (response.isSuccessful && response.body() != null) {
                val events = response.body()!!.map { eventResponse ->
                    Event(
                        id = eventResponse.id,
                        title = eventResponse.title,
                        description = eventResponse.description,
                        date = eventResponse.date,
                        location = eventResponse.location,
                        createdBy = eventResponse.createdBy,
                        recipientIds = eventResponse.recipientIds,
                        createdAt = eventResponse.createdAt,
                        updatedAt = eventResponse.updatedAt
                    )
                }
                Result.success(events)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateEvent(eventId: UUID, request: UpdateEventRequest): Result<Event> {
        return try {
            val response = eventApiService.updateEvent(getAuthHeader(), eventId, request)
            if (response.isSuccessful && response.body() != null) {
                val eventResponse = response.body()!!
                Result.success(Event(
                    id = eventResponse.id,
                    title = eventResponse.title,
                    description = eventResponse.description,
                    date = eventResponse.date,
                    location = eventResponse.location,
                    createdBy = eventResponse.createdBy,
                    recipientIds = eventResponse.recipientIds,
                    createdAt = eventResponse.createdAt,
                    updatedAt = eventResponse.updatedAt
                ))
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteEvent(eventId: UUID): Result<Unit> {
        return try {
            val response = eventApiService.deleteEvent(getAuthHeader(), eventId)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}