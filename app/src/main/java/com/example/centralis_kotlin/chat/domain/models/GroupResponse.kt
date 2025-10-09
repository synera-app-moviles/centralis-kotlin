package com.example.centralis_kotlin.chat.domain.models

data class GroupResponse(
    val id: String,
    val name: String,
    val description: String?,
    val imageUrl: String?,
    val visibility: GroupVisibility,
    val createdBy: String?,
    val members: List<String> = emptyList()
)
