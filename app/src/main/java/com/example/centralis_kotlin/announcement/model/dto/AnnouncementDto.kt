package com.example.centralis_kotlin.announcement.model.dto

data class AnnouncementDto(
    val id: String,
    val title: String,
    val description: String,
    val image: String?,
    val priority: String,
    val createdAt: String,
    val createdBy: String
)


