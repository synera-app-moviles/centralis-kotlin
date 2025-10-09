package com.example.centralis_kotlin.announcement.model.dto

data class CommentDto(
    val id: String,
    val announcementId: String,
    val employeeId: String,
    val content: String,
    val createdAt: String
)