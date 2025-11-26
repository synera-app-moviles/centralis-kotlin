package com.example.centralis_kotlin.chat.domain.models

/**
 * Request model for sharing an image in chat
 */
data class ChatImageRequest(
    val senderId: String,
    val imageUrl: String
)

/**
 * Response model when an image is shared
 */
data class ChatImageResponse(
    val imageId: String,
    val groupId: String,
    val senderId: String,
    val imageUrl: String,
    val isVisible: Boolean,
    val sentAt: String
)