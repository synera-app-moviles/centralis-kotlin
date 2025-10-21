package com.example.centralis_kotlin.common.data.repository

import kotlinx.coroutines.flow.Flow
import com.example.centralis_kotlin.common.data.local.entities.NotificationEntity
import com.example.centralis_kotlin.common.data.local.daos.NotificationDao

class NotificationRepository(
    private val notificationDao: NotificationDao
) {
    
    fun getNotificationsByUser(userId: String): Flow<List<NotificationEntity>> {
        return notificationDao.getNotificationsByUser(userId)
    }
    
    fun getUnreadNotifications(userId: String): Flow<List<NotificationEntity>> {
        return notificationDao.getUnreadNotifications(userId)
    }
    
    fun getUnreadCount(userId: String): Flow<Int> {
        return notificationDao.getUnreadCount(userId)
    }
    
    suspend fun insertNotification(notification: NotificationEntity) {
        notificationDao.insertNotification(notification)
    }
    
    suspend fun markAsRead(notificationId: String) {
        val notification = notificationDao.getNotificationById(notificationId)
        notification?.let {
            val updatedNotification = it.copy(isRead = true)
            notificationDao.updateNotification(updatedNotification)
        }
    }
    
    suspend fun deleteOldNotifications(userId: String, cutoffTimestamp: Long) {
        notificationDao.deleteOldNotifications(userId, cutoffTimestamp)
    }
    
    suspend fun deleteNotification(notification: NotificationEntity) {
        notificationDao.deleteNotification(notification)
    }
    
    fun getNotificationsByType(userId: String, type: String): Flow<List<NotificationEntity>> {
        return notificationDao.getNotificationsByType(userId, type)
    }
}