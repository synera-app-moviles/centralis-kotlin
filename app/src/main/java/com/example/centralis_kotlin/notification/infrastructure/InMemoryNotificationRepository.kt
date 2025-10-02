package com.example.centralis_kotlin.notification.infrastructure

import com.example.centralis_kotlin.notification.domain.*
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

// Implementación simulada de NotificationRepository (para pruebas/desarrollo)
class InMemoryNotificationRepository : NotificationRepository {
    private val notifications = ConcurrentHashMap<UUID, Notification>()

    override fun save(notification: Notification) {
        notifications[notification.id] = notification
    }

    override fun update(notification: Notification) {
        notifications[notification.id] = notification
    }

    override fun findById(id: UUID): Notification? {
        return notifications[id]
    }

    override fun findByRecipient(userId: UserId): List<Notification> {
        return notifications.values.filter { it.recipients.contains(userId) }
    }
}

// Aquí se dejaría preparado el punto de integración real con Supabase y FCM
// class SupabaseNotificationRepository : NotificationRepository { ... }
// class FCMService { ... }

