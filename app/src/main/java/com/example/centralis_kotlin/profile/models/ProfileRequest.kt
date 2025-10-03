package com.example.centralis_kotlin.profile.models

data class ProfileRequest(
    val userId: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val avatarUrl: String? = null,
    val position: String, // EMPLOYEE, MANAGER, DIRECTOR
    val department: String // IT, HR, FINANCE, OPERATIONS, MARKETING, SALES
)