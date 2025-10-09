package com.example.centralis_kotlin.announcement.model

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.util.Date
import java.util.UUID

data class Announcement(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String,
    val image: String? = null,
    val priority: Priority,
    val createdAt: Date = Date(),
    val createdBy: String,
    val seenBy: MutableSet<String> = mutableSetOf(),
    val comments: MutableList<Comment> = mutableListOf()
)
