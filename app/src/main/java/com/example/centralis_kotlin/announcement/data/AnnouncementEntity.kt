package com.example.centralis_kotlin.announcement.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "announcements")
data class AnnouncementEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val image: String?,
    val priority: String,
    val createdAt: Long,
    val createdBy: String
)
