package com.example.centralis_kotlin.iam.models

data class SignUpRequest(
    val username: String,
    val password: String,
    val name: String,
    val lastname: String,
    val email: String,
    val roles: List<String> = listOf("ROLE_USER")
)