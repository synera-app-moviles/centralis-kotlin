package com.example.centralis_kotlin.common.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val title: String,
    val message: String,
    val type: String,              // "GENERAL", "ALERT", "EVENT", "SYSTEM"
    val timestamp: Long,
    val isRead: Boolean = false,
    val priority: Int = 0,         // 0=normal, 1=alta, 2=crítica
    val fcmMessageId: String? = null,
    val imageUrl: String? = null,
    val actionUrl: String? = null
)