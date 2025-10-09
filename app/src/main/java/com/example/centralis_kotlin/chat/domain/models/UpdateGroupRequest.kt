package com.example.centralis_kotlin.chat.domain.models

data class UpdateGroupRequest(
    val name: String? = null,
    val description: String? = null,
    val imageUrl: String? = null
)