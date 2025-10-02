package com.example.centralis_kotlin.ui.notification

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// Modelo de UI para la notificación
data class NotificationUiModel(
    val id: String,
    val title: String,
    val message: String,
    val date: String, // Usar String para compatibilidad con minSdk 24
    val type: NotificationType
)

enum class NotificationType {
    ANNOUNCEMENT, EVENT, CHAT
}

class NotificationViewModel : ViewModel() {
    private val _notifications = MutableStateFlow(
        listOf(
            NotificationUiModel(
                id = "1",
                title = "Remote Work Policy Update",
                message = "",
                date = "May 20, 2024",
                type = NotificationType.ANNOUNCEMENT
            ),
            NotificationUiModel(
                id = "2",
                title = "New Employee Wellness",
                message = "",
                date = "May 15, 2024",
                type = NotificationType.EVENT
            ),
            NotificationUiModel(
                id = "3",
                title = "Announcement of the Next",
                message = "",
                date = "May 10, 2024",
                type = NotificationType.ANNOUNCEMENT
            )
        )
    )
    val notifications: StateFlow<List<NotificationUiModel>> = _notifications
}
