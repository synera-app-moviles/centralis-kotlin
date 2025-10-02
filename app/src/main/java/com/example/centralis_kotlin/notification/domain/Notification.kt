package com.example.centralis_kotlin.notification.domain

import java.util.UUID
import java.time.LocalDateTime

@JvmInline
value class UserId(val value: String)

enum class NotificationPriority {
    HIGH, NORMAL
}

enum class NotificationStatus {
    PENDING, SENT, FAILED, READ
}

data class Notification(
    val id: UUID,
    val title: String,
    val message: String,
    val recipients: List<UserId>,
    val priority: NotificationPriority,
    var status: NotificationStatus = NotificationStatus.PENDING,
    val createdAt: LocalDateTime = LocalDateTime.now()
) {
    companion object {
        fun createNotification(
            title: String,
            message: String,
            recipients: List<UserId>,
            priority: NotificationPriority
        ): Notification {
            require(recipients.isNotEmpty()) { "Debe haber al menos un destinatario válido." }
            return Notification(
                id = UUID.randomUUID(),
                title = title,
                message = message,
                recipients = recipients,
                priority = priority
            )
        }
    }

    fun markAsSent() {
        status = NotificationStatus.SENT
    }

    fun markAsFailed() {
        status = NotificationStatus.FAILED
    }

    fun markAsRead() {
        status = NotificationStatus.READ
    }
}

