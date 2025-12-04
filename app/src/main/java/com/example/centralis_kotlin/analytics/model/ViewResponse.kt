package com.example.centralis_kotlin.analytics.model

data class ViewResponse(
    val viewId: String,
    val userId: String,
    val contentId: String,
    val viewedAt: String,
    val message: String,
    val isNewView: Boolean
)