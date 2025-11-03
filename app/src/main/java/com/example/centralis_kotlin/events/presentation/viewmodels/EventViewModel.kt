package com.example.centralis_kotlin.events.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.centralis_kotlin.events.model.EventResponse
import com.example.centralis_kotlin.events.model.CreateEventRequest
import com.example.centralis_kotlin.events.model.UpdateEventRequest
import com.example.centralis_kotlin.events.service.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class EventViewModel(
    private val eventRepository: EventRepository,
    private val currentUserId: UUID
) : ViewModel() {

    private val _uiState = MutableStateFlow<EventUiState>(EventUiState.Idle)
    val uiState: StateFlow<EventUiState> = _uiState.asStateFlow()

    private val _events = MutableStateFlow<List<EventResponse>>(emptyList())
    val events: StateFlow<List<EventResponse>> = _events.asStateFlow()


    fun createEvent(request: CreateEventRequest) {
        viewModelScope.launch {
            _uiState.value = EventUiState.Loading
            eventRepository.createEvent(request)
                .onSuccess {
                    _uiState.value = EventUiState.Success("Evento creado exitosamente")
                    loadVisibleEvents()
                }
                .onFailure { error ->
                    _uiState.value = EventUiState.Error(error.message ?: "Error al crear evento")
                }
        }
    }


    fun loadVisibleEvents() {
        viewModelScope.launch {
            _uiState.value = EventUiState.Loading
            eventRepository.getAllEvents()
                .onSuccess { eventList ->
                    _events.value = eventList.filter { isVisibleToUser(it, currentUserId) }
                    _uiState.value = EventUiState.Success("Eventos cargados")
                }
                .onFailure { error ->
                    _uiState.value = EventUiState.Error(error.message ?: "Error al cargar eventos")
                }
        }
    }


    fun loadEventsByRecipient(userId: UUID) {
        viewModelScope.launch {
            _uiState.value = EventUiState.Loading
            eventRepository.getEventsByRecipient(userId)
                .onSuccess { eventList ->
                    _events.value = eventList
                    _uiState.value = EventUiState.Success("Eventos cargados")
                }
                .onFailure { error ->
                    _uiState.value = EventUiState.Error(error.message ?: "Error al cargar eventos")
                }
        }
    }

    fun updateEvent(eventId: UUID, request: UpdateEventRequest) {
        viewModelScope.launch {
            _uiState.value = EventUiState.Loading
            eventRepository.updateEvent(eventId, request)
                .onSuccess {
                    _uiState.value = EventUiState.Success("Evento actualizado")
                    loadVisibleEvents()
                }
                .onFailure { error ->
                    _uiState.value = EventUiState.Error(error.message ?: "Error al actualizar")
                }
        }
    }

    fun deleteEvent(eventId: UUID) {
        viewModelScope.launch {
            _uiState.value = EventUiState.Loading
            eventRepository.deleteEvent(eventId)
                .onSuccess {
                    _uiState.value = EventUiState.Success("Evento eliminado")
                    loadVisibleEvents()
                }
                .onFailure { error ->
                    _uiState.value = EventUiState.Error(error.message ?: "Error al eliminar")
                }
        }
    }

    fun resetState() {
        _uiState.value = EventUiState.Idle
    }


    private fun isVisibleToUser(event: EventResponse, userId: UUID): Boolean {

        try {
            val createdBy = event.createdBy
            if (createdBy == userId) return true
            if (createdBy.toString() == userId.toString()) return true
        } catch (_: Exception) { /* ignore */ }


        try {
            val recipients = event.recipientIds
            if (recipients.any { it == userId }) return true
            if (recipients.any { it.toString() == userId.toString() }) return true
        } catch (_: Exception) { /* ignore */ }


        try {
            val recipientsObj = try {
                event.javaClass.getMethod("getRecipientIds").invoke(event)
            } catch (e: NoSuchMethodException) {
                null
            }
            val recipientsCol = recipientsObj as? Collection<*>
            recipientsCol?.forEach { r ->
                if (r is UUID && r == userId) return true
                if (r is String) {
                    try {
                        if (UUID.fromString(r) == userId) return true
                    } catch (_: Exception) { /* ignore */ }
                }
            }
        } catch (_: Exception) { /* ignore */ }

        return false
    }
}

sealed class EventUiState {
    object Idle : EventUiState()
    object Loading : EventUiState()
    data class Success(val message: String) : EventUiState()
    data class Error(val message: String) : EventUiState()
}