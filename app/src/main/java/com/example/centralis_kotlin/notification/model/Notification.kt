package com.example.centralis_kotlin.notification.model

import java.util.Date

data class Notification(
    val id: String,
    val title: String,
    val message: String,
    val type: NotificationType,
    val timestamp: Date,
    val isRead: Boolean = false
)

enum class NotificationType {
    POLICY_UPDATE,
    WELLNESS,
    ANNOUNCEMENT,
    SYSTEM,
    REMINDER
}

data class NotificationUiState(
    val notifications: List<Notification> = emptyList(),
    val unreadCount: Int = 0,
    val isLoading: Boolean = true,
    val error: String? = null
)
