package com.example.centralis_kotlin.notification.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.example.centralis_kotlin.common.data.repository.NotificationRepository
import com.example.centralis_kotlin.common.data.local.entities.NotificationEntity
import com.example.centralis_kotlin.common.RetrofitClient
import com.example.centralis_kotlin.common.SharedPreferencesManager
import android.content.Context

data class NotificationUiState(
    val notifications: List<NotificationEntity> = emptyList(),
    val unreadCount: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null
)

class NotificationViewModel(
    private val notificationRepository: NotificationRepository,
    private val currentUserId: String,
    private val context: Context
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(NotificationUiState())
    val uiState: StateFlow<NotificationUiState> = _uiState.asStateFlow()
    
    private val sharedPrefsManager = SharedPreferencesManager(context)
    
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
                // Marcar como leída localmente
                notificationRepository.markAsRead(notificationId)
                
                // Sincronizar con backend
                val authToken = sharedPrefsManager.getToken()
                if (!authToken.isNullOrEmpty()) {
                    try {
                        RetrofitClient.notificationApiService.markNotificationAsRead(
                            notificationId = notificationId,
                            authorization = "Bearer $authToken"
                        )
                    } catch (e: Exception) {
                        // El backend falló, pero la notificación local ya está marcada

                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Error al marcar como leída: ${e.message}"
                )
            }
        }
    }
    
    fun deleteNotification(notification: NotificationEntity) {
        viewModelScope.launch {
            try {
                // Eliminar localmente
                notificationRepository.deleteNotification(notification)
                
                // Sincronizar con backend si la notificación tiene un ID remoto
                val authToken = sharedPrefsManager.getToken()
                if (!authToken.isNullOrEmpty() && !notification.id.startsWith("test-")) {
                    try {
                        RetrofitClient.notificationApiService.deleteNotification(
                            notificationId = notification.id,
                            authorization = "Bearer $authToken"
                        )
                    } catch (e: Exception) {

                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Error al eliminar notificación: ${e.message}"
                )
            }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
    
    fun refreshNotifications() {
        loadNotifications()
        syncWithBackend()
    }
    
    private fun syncWithBackend() {
        viewModelScope.launch {
            val authToken = sharedPrefsManager.getToken()
            if (authToken.isNullOrEmpty()) return@launch
            
            try {
                val response = RetrofitClient.notificationApiService.getUserNotifications(
                    userId = currentUserId,
                    authorization = "Bearer $authToken",
                    page = 0,
                    size = 50 // Sincronizar las últimas 50 notificaciones
                )
                
                if (response.isSuccessful) {
                    val backendNotifications = response.body()?.content ?: emptyList()
                    
                    // Convertir notificaciones del backend a entidades locales
                    backendNotifications.forEach { backendNotification ->
                        val localNotification = NotificationEntity(
                            id = backendNotification.id,
                            userId = currentUserId,
                            title = backendNotification.title,
                            message = backendNotification.message,
                            type = "BACKEND",
                            timestamp = parseTimestamp(backendNotification.createdAt),
                            isRead = backendNotification.status == "READ",
                            priority = when (backendNotification.priority) {
                                "HIGH", "URGENT" -> 2
                                "MEDIUM" -> 1
                                else -> 0
                            }
                        )
                        
                        // Insertar si no existe ya
                        try {
                            notificationRepository.insertNotification(localNotification)
                        } catch (e: Exception) {
                            // La notificación ya existe, ignorar
                        }
                    }
                }
            } catch (e: Exception) {
                // Error de sincronización, continuar con notificaciones locales
            }
        }
    }
    
    private fun parseTimestamp(dateString: String): Long {
        return try {
            // Intentar parsear el formato ISO del backend
            val formatter = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", java.util.Locale.getDefault())
            formatter.parse(dateString)?.time ?: System.currentTimeMillis()
        } catch (e: Exception) {
            System.currentTimeMillis()
        }
    }
    

}