package com.example.centralis_kotlin.announcement.model

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.util.Date
import java.util.UUID

data class Comment(
    val id: String = UUID.randomUUID().toString(),
    val announcementId: String,
    val employeeId: String,
    val content: String,
    val createdAt: Date = Date()
)