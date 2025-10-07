package com.example.centralis_kotlin.chat.domain.models

data class ChatItem(
    val id: String,
    val name: String,
    val lastMessage: String,
    val avatarUrl: String
)
