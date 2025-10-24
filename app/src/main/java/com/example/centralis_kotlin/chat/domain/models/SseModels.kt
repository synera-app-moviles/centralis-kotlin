package com.example.centralis_kotlin.chat.domain.models

import org.json.JSONObject

/**
 * Data class representing a real-time message received via SSE
 */
data class SseMessage(
    val messageId: String,
    val senderId: String,
    val senderUsername: String,
    val body: String,
    val groupId: String,
    val sentAt: String,
    val type: String = "message_created"
) {
    companion object {
        fun fromJson(json: JSONObject): SseMessage {
            return SseMessage(
                messageId = json.getString("messageId"),
                senderId = json.getString("senderId"),
                senderUsername = json.getString("senderUsername"),
                body = json.getString("body"),
                groupId = json.getString("groupId"),
                sentAt = json.getString("sentAt"),
                type = json.optString("type", "message_created")
            )
        }
    }
}

/**
 * Connection state for SSE
 */
sealed class SseConnectionState {
    object Disconnected : SseConnectionState()
    object Connecting : SseConnectionState()
    object Connected : SseConnectionState()
    data class Error(val error: Throwable) : SseConnectionState()
    object Reconnecting : SseConnectionState()
}

/**
 * SSE Event wrapper
 */
data class SseEvent(
    val type: String,
    val data: String,
    val id: String? = null
)