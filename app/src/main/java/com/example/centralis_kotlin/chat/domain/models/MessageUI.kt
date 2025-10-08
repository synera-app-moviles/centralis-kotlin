package com.example.centralis_kotlin.chat.domain.models

data class MessageUI (
    val id: String,
    val author: String,
    val text: String,
    val avatar: String?,
    val isMe: Boolean
)