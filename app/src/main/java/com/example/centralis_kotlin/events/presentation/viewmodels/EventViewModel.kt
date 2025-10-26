package com.example.centralis_kotlin.events.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.centralis_kotlin.events.model.CreateEventRequest
import com.example.centralis_kotlin.events.model.Event
import com.example.centralis_kotlin.events.model.UpdateEventRequest
import com.example.centralis_kotlin.events.service.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class EventViewModel(
    private val eventRepository: EventRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<EventUiState>(EventUiState.Loading)
    val uiState: StateFlow<EventUiState> = _uiState.asStateFlow()

    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> = _events.asStateFlow()

    fun createEvent(request: CreateEventRequest) {
        viewModelScope.launch {
            _uiState.value = EventUiState.Loading
            eventRepository.createEvent(request)
                .onSuccess { event ->
                    _uiState.value = EventUiState.Success("Evento creado exitosamente")
                    loadAllEvents()
                }
                .onFailure { error ->
                    _uiState.value = EventUiState.Error(error.message ?: "Error al crear evento")
                }
        }
    }

    fun loadAllEvents() {
        viewModelScope.launch {
            _uiState.value = EventUiState.Loading
            eventRepository.getAllEvents()
                .onSuccess { eventList ->
                    _events.value = eventList
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
                    loadAllEvents()
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
                    loadAllEvents()
                }
                .onFailure { error ->
                    _uiState.value = EventUiState.Error(error.message ?: "Error al eliminar")
                }
        }
    }

    fun resetState() {
        _uiState.value = EventUiState.Idle
    }
}

sealed class EventUiState {
    object Idle : EventUiState()
    object Loading : EventUiState()
    data class Success(val message: String) : EventUiState()
    data class Error(val message: String) : EventUiState()
}