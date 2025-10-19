package com.example.centralis_kotlin.common.data.local.daos

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.centralis_kotlin.common.data.local.entities.NotificationEntity

@Dao
interface NotificationDao {
    
    @Query("SELECT * FROM notifications WHERE userId = :userId ORDER BY priority DESC, timestamp DESC")
    fun getNotificationsByUser(userId: String): Flow<List<NotificationEntity>>
    
    @Query("SELECT * FROM notifications WHERE userId = :userId AND isRead = 0 ORDER BY priority DESC, timestamp DESC")
    fun getUnreadNotifications(userId: String): Flow<List<NotificationEntity>>
    
    @Query("SELECT COUNT(*) FROM notifications WHERE userId = :userId AND isRead = 0")
    fun getUnreadCount(userId: String): Flow<Int>
    
    @Query("SELECT * FROM notifications WHERE id = :notificationId")
    suspend fun getNotificationById(notificationId: String): NotificationEntity?
    
    @Query("SELECT * FROM notifications WHERE type = :type AND userId = :userId ORDER BY timestamp DESC")
    fun getNotificationsByType(userId: String, type: String): Flow<List<NotificationEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotifications(notifications: List<NotificationEntity>)
    
    @Update
    suspend fun updateNotification(notification: NotificationEntity)
    
    @Delete
    suspend fun deleteNotification(notification: NotificationEntity)
    
    @Query("DELETE FROM notifications WHERE userId = :userId AND timestamp < :cutoffTimestamp")
    suspend fun deleteOldNotifications(userId: String, cutoffTimestamp: Long)
}