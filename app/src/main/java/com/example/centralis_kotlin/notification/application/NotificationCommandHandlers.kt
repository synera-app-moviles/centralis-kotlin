package com.example.centralis_kotlin.notification.application

import com.example.centralis_kotlin.notification.domain.*
import java.util.UUID

// Comando para enviar notificación
data class SendNotificationCommand(
    val title: String,
    val message: String,
    val recipients: List<UserId>,
    val priority: NotificationPriority
)

class SendNotificationCommandHandler(private val notificationRepository: NotificationRepository) {
    fun handle(command: SendNotificationCommand): Notification {
        val notification = Notification.createNotification(
            title = command.title,
            message = command.message,
            recipients = command.recipients,
            priority = command.priority
        )
        notificationRepository.save(notification)
        return notification
    }
}

// Comando para reintentar notificación
data class RetryNotificationCommand(val notificationId: UUID)

class RetryNotificationCommandHandler(private val notificationRepository: NotificationRepository) {
    fun handle(command: RetryNotificationCommand): Notification? {
        val notification = notificationRepository.findById(command.notificationId)
        notification?.let {
            if (it.status == NotificationStatus.FAILED) {
                // Aquí se podría reintentar el envío (lógica delegada a infraestructura)
                it.status = NotificationStatus.PENDING
                notificationRepository.update(it)
            }
        }
        return notification
    }
}

