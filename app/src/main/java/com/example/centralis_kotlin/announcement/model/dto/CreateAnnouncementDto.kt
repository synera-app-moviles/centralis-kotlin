package com.example.centralis_kotlin.announcement.model.dto

data class CreateAnnouncementDto(
    val title: String,
    val description: String,
    val image: String? = null,
    val priority: String,
    val createdBy: String
)
