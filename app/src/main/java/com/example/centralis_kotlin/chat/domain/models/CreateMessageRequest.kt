package com.example.centralis_kotlin.chat.domain.models

data class CreateMessageRequest(
    val senderId: String,
    val body: String
)
