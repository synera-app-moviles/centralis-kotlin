package com.example.centralis_kotlin.profile.models

data class ProfileRequest(
    val userId: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val avatarUrl: String? = null,
    val position: Position,
    val department: Department
)