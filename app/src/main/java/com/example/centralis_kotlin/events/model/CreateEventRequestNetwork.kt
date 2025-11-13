package com.example.centralis_kotlin.events.model

data class CreateEventRequestNetwork(
    val title: String,
    val description: String?,
    val date: String?,
    val location: String?,
    val recipientIds: List<String>?,
    val createdBy: String?
)
