package com.example.centralis_kotlin.events.service

import com.example.centralis_kotlin.events.model.CreateEventRequest
import com.example.centralis_kotlin.events.model.EventResponse
import com.example.centralis_kotlin.events.model.UpdateEventRequest
import java.util.UUID
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(
    private val eventApiService: EventApiService,
    private val tokenProvider: () -> String?
) : EventRepository {

    private fun getAuthHeader(): String {
        val token = tokenProvider() ?: throw IllegalStateException("Token no disponible")
        return "Bearer $token"
    }

    override suspend fun createEvent(request: CreateEventRequest): Result<EventResponse> {
        return try {
            val response = eventApiService.createEvent(
                authorization = getAuthHeader(),
                request = request
            )

            if (response.isSuccessful) {
                val eventResponse = response.body()
                if (eventResponse != null) {
                    Result.success(eventResponse)
                } else {
                    Result.failure(Exception("Respuesta vacía del servidor"))
                }
            } else {
                val errorBody = response.errorBody()?.string() ?: "Error desconocido"
                Result.failure(Exception("Error del servidor: ${response.code()} - $errorBody"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getEventById(eventId: UUID): Result<EventResponse> {
        return try {
            val response = eventApiService.getEventById(getAuthHeader(), eventId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAllEvents(): Result<List<EventResponse>> {
        return try {
            val response = eventApiService.getEvents(
                authorization = getAuthHeader(),
                userId = null,
                filterType = null
            )
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getEventsByRecipient(userId: UUID): Result<List<EventResponse>> {
        return try {
            val response = eventApiService.getEvents(
                authorization = getAuthHeader(),
                userId = userId,
                filterType = "recipient"
            )
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getEventsByCreator(userId: UUID): Result<List<EventResponse>> {
        return try {
            val response = eventApiService.getEvents(
                authorization = getAuthHeader(),
                userId = userId,
                filterType = "creator"
            )
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateEvent(eventId: UUID, request: UpdateEventRequest): Result<EventResponse> {
        return try {
            val response = eventApiService.updateEvent(getAuthHeader(), eventId, request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
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