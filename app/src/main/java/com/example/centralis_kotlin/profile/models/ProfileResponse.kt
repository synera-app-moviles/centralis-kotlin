package com.example.centralis_kotlin.profile.models

data class ProfileResponse(
    val profileId: String,
    val userId: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val fullName: String,
    val avatarUrl: String?,
    val position: String,
    val department: String
)