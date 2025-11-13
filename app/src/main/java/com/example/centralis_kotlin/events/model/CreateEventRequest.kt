
package com.example.centralis_kotlin.events.model

import java.util.UUID

data class CreateEventRequest(
    val title: String,
    val description: String?,
    val date: String,
    val location: String?,
    val recipientIds: List<UUID>?,
    val createdBy: UUID?
)