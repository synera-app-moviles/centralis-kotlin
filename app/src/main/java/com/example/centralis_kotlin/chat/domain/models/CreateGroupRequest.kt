package com.example.centralis_kotlin.chat.domain.models

data class CreateGroupRequest(
    val name: String,
    val description: String? = null,
    val imageUrl: String? = null,
    val visibility: GroupVisibility,
    val memberIds: List<String> = emptyList(),
    val createdBy: String

)