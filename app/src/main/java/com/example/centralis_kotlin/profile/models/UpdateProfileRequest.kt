package com.example.centralis_kotlin.profile.models

data class UpdateProfileRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val avatarUrl: String?,
    val position: String,
    val department: String
)