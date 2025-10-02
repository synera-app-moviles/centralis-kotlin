package com.example.centralis_kotlin.notification.presentation

import com.example.centralis_kotlin.notification.application.*
import com.example.centralis_kotlin.notification.domain.Notification
import com.example.centralis_kotlin.notification.domain.NotificationStatus
import com.example.centralis_kotlin.notification.domain.NotificationPriority
import com.example.centralis_kotlin.notification.domain.UserId
import com.example.centralis_kotlin.notification.domain.NotificationRepository
import java.util.UUID

// Simulación de un controlador tipo REST (puede adaptarse a Ktor, Spring, etc.)
class NotificationController(
    private val sendNotificationHandler: SendNotificationCommandHandler,
    private val notificationRepository: NotificationRepository
) {
    // POST /notifications/test
    fun sendTestNotification(userId: String): Notification {
        val command = SendNotificationCommand(
            title = "Test Notification",
            message = "Esta es una notificación de prueba.",
            recipients = listOf(UserId(userId)),
            priority = NotificationPriority.NORMAL
        )
        return sendNotificationHandler.handle(command)
    }

    // GET /notifications/{userId}
    fun getUserNotifications(userId: String): List<Notification> {
        return notificationRepository.findByRecipient(UserId(userId))
    }

    // GET /notifications/{id}/status
    fun getNotificationStatus(id: String): NotificationStatus? {
        val notification = notificationRepository.findById(UUID.fromString(id))
        return notification?.status
    }
}
