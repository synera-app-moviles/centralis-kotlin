package com.example.centralis_kotlin.events.model

import java.util.UUID

data class Event(
    val id: UUID,
    val title: String,
    val description: String,
    val date: String,
    val location: String?,
    val createdBy: UUID,
    val recipientIds: List<UUID>,
    val createdAt: String,
    val updatedAt: String
)