package com.example.centralis_kotlin.notification.service

import com.example.centralis_kotlin.notification.model.Notification
import com.example.centralis_kotlin.notification.model.NotificationType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Calendar
import java.util.Date

class NotificationService {

    private val _notifications = MutableStateFlow<List<Notification>>(emptyList())
    val notifications: StateFlow<List<Notification>> = _notifications.asStateFlow()

    private val _unreadCount = MutableStateFlow(0)
    val unreadCount: StateFlow<Int> = _unreadCount.asStateFlow()

    init {
        loadNotifications()
    }

    private fun loadNotifications() {
        // Datos exactos de la imagen
        val calendar = Calendar.getInstance()

        val notificationList = listOf(
            Notification(
                id = "1",
                title = "Remote Work Policy Update",
                message = "New remote work policies have been updated. Please review the changes.",
                type = NotificationType.POLICY_UPDATE,
                timestamp = createDate(2024, 5, 20),
                isRead = false
            ),
            Notification(
                id = "2",
                title = "New Employee Wellness",
                message = "Wellness program registration is now open for all employees.",
                type = NotificationType.WELLNESS,
                timestamp = createDate(2024, 5, 15),
                isRead = false
            ),
            Notification(
                id = "3",
                title = "Announcement of the Next",
                message = "Important company announcement regarding upcoming changes.",
                type = NotificationType.ANNOUNCEMENT,
                timestamp = createDate(2024, 5, 10),
                isRead = false
            )
        )

        _notifications.value = notificationList.sortedByDescending { it.timestamp }
        updateUnreadCount()
    }

    private fun createDate(year: Int, month: Int, day: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, day) // Calendar.MONTH is 0-based
        return calendar.time
    }

    fun getAllNotifications(): List<Notification> {
        return _notifications.value
    }

    fun getUnreadCount(): Int {
        return _unreadCount.value
    }

    fun markAsRead(notificationId: String) {
        val currentNotifications = _notifications.value.toMutableList()
        val index = currentNotifications.indexOfFirst { it.id == notificationId }
        if (index != -1) {
            currentNotifications[index] = currentNotifications[index].copy(isRead = true)
            _notifications.value = currentNotifications
            updateUnreadCount()
        }
    }

    fun deleteNotification(notificationId: String) {
        val currentNotifications = _notifications.value.toMutableList()
        currentNotifications.removeIf { it.id == notificationId }
        _notifications.value = currentNotifications
        updateUnreadCount()
    }

    private fun updateUnreadCount() {
        _unreadCount.value = _notifications.value.count { !it.isRead }
    }
}
