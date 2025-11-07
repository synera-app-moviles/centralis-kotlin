package com.example.centralis_kotlin.announcement.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_announcements")
data class SavedAnnouncementEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val priority: String,
    val createdAt: String
)
