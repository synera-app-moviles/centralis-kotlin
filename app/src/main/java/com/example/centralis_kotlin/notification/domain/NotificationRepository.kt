package com.example.centralis_kotlin.notification.domain

import java.util.UUID

interface NotificationRepository {
    fun save(notification: Notification)
    fun update(notification: Notification)
    fun findById(id: UUID): Notification?
    fun findByRecipient(userId: UserId): List<Notification>
}

