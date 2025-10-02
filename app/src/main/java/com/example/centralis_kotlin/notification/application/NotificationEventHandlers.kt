package com.example.centralis_kotlin.notification.application

import com.example.centralis_kotlin.notification.domain.*
import java.util.UUID

// Evento: Se crea un anuncio
data class AnnouncementCreatedEvent(val announcementId: UUID, val title: String, val message: String, val recipients: List<UserId>)

class AnnouncementCreatedHandler(private val notificationRepository: NotificationRepository) {
    fun handle(event: AnnouncementCreatedEvent) {
        val notification = Notification.createNotification(
            title = event.title,
            message = event.message,
            recipients = event.recipients,
            priority = NotificationPriority.NORMAL
        )
        notificationRepository.save(notification)
    }
}

// Evento: Se crea un evento
data class EventCreatedEvent(val eventId: UUID, val title: String, val message: String, val guests: List<UserId>)

class EventCreatedHandler(private val notificationRepository: NotificationRepository) {
    fun handle(event: EventCreatedEvent) {
        val notification = Notification.createNotification(
            title = event.title,
            message = event.message,
            recipients = event.guests,
            priority = NotificationPriority.NORMAL
        )
        notificationRepository.save(notification)
    }
}

// Evento: Se envía un mensaje de chat
data class ChatMessageSentEvent(val chatId: UUID, val message: String, val members: List<UserId>)

class ChatMessageSentHandler(private val notificationRepository: NotificationRepository) {
    fun handle(event: ChatMessageSentEvent) {
        val notification = Notification.createNotification(
            title = "Nuevo mensaje en el chat",
            message = event.message,
            recipients = event.members,
            priority = NotificationPriority.NORMAL
        )
        notificationRepository.save(notification)
    }
}

// Evento: Notificación leída
data class NotificationReadEvent(val notificationId: UUID)

class NotificationReadHandler(private val notificationRepository: NotificationRepository) {
    fun handle(event: NotificationReadEvent) {
        val notification = notificationRepository.findById(event.notificationId)
        notification?.markAsRead()
        notification?.let { notificationRepository.update(it) }
    }
}

