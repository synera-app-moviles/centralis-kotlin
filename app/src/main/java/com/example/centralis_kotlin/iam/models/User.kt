package com.example.centralis_kotlin.iam.models

data class User(
    val id: String,
    val username: String,
    val roles: List<String>,
    val createdAt: String,
    val updatedAt: String
)