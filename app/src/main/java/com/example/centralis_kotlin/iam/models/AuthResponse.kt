package com.example.centralis_kotlin.iam.models

data class AuthResponse(
    val id: String,
    val username: String,
    val token: String? = null,
    val roles: List<String>? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)