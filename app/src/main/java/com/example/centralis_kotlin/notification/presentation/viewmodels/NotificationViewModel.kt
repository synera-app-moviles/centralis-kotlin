package com.example.centralis_kotlin.notification.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.example.centralis_kotlin.common.data.repository.NotificationRepository
import com.example.centralis_kotlin.common.data.local.entities.NotificationEntity

data class NotificationUiState(
    val notifications: List<NotificationEntity> = emptyList(),
    val unreadCount: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null
)

class NotificationViewModel(
    private val notificationRepository: NotificationRepository
) : ViewModel() {
    
    private val currentUserId = "test-user-123" // Usuario temporal para testing
    
    private val _uiState = MutableStateFlow(NotificationUiState())
    val uiState: StateFlow<NotificationUiState> = _uiState.asStateFlow()
    
    init {
        loadNotifications()
    }
    
    private fun loadNotifications() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            try {
                // Combinar flujos de notificaciones y conteo no leídas
                combine(
                    notificationRepository.getNotificationsByUser(currentUserId),
                    notificationRepository.getUnreadCount(currentUserId)
                ) { notifications, unreadCount ->
                    NotificationUiState(
                        notifications = notifications,
                        unreadCount = unreadCount,
                        isLoading = false,
                        error = null
                    )
                }.collect { newState ->
                    _uiState.value = newState
                }
                
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error cargando notificaciones: ${e.message}"
                )
            }
        }
    }
    
    fun markAsRead(notificationId: String) {
        viewModelScope.launch {
            try {
                notificationRepository.markAsRead(notificationId)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Error marcando como leída: ${e.message}"
                )
            }
        }
    }
    
    fun deleteNotification(notification: NotificationEntity) {
        viewModelScope.launch {
            try {
                notificationRepository.deleteNotification(notification)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Error eliminando notificación: ${e.message}"
                )
            }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
    
    fun refreshNotifications() {
        loadNotifications()
    }
    
    // Crear notificación de prueba para testing
    fun createTestNotification() {
        viewModelScope.launch {
            try {
                val testNotification = NotificationEntity(
                    id = "test-${System.currentTimeMillis()}",
                    userId = currentUserId,
                    title = "🎉 Notificación de Prueba",
                    message = "Esta es una notificación creada para probar Room Database y la UI.",
                    type = "GENERAL",
                    timestamp = System.currentTimeMillis(),
                    isRead = false,
                    priority = 1
                )
                
                notificationRepository.insertNotification(testNotification)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Error creando notificación de prueba: ${e.message}"
                )
            }
        }
    }
}