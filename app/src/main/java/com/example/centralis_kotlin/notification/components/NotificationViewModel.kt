package com.example.centralis_kotlin.notification.components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.centralis_kotlin.notification.model.NotificationUiState
import com.example.centralis_kotlin.notification.service.NotificationService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NotificationViewModel(
    private val notificationService: NotificationService = NotificationService()
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationUiState())
    val uiState: StateFlow<NotificationUiState> = _uiState.asStateFlow()

    init {
        loadNotifications()
        observeUnreadCount()
    }

    private fun loadNotifications() {
        viewModelScope.launch {
            try {
                val notifications = notificationService.getAllNotifications()
                _uiState.value = _uiState.value.copy(
                    notifications = notifications,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error al cargar notificaciones"
                )
            }
        }
    }

    private fun observeUnreadCount() {
        viewModelScope.launch {
            notificationService.unreadCount.collect { count ->
                _uiState.value = _uiState.value.copy(unreadCount = count)
            }
        }
    }

    fun markAsRead(notificationId: String) {
        notificationService.markAsRead(notificationId)
        loadNotifications()
    }

    fun deleteNotification(notificationId: String) {
        notificationService.deleteNotification(notificationId)
        loadNotifications()
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
