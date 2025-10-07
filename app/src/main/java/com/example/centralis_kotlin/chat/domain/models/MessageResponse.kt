package com.example.centralis_kotlin.chat.domain.models

data class MessageResponse(
    val messageId: String,
    val groupId: String,
    val senderId: String,
    val body: String,
    val status: MessageStatus,
    val sentAt: String,
    val editedAt: String?
)
