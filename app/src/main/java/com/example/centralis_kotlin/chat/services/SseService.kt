package com.example.centralis_kotlin.chat.services

import android.util.Log
import com.example.centralis_kotlin.chat.domain.models.SseConnectionState
import com.example.centralis_kotlin.chat.domain.models.SseMessage
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import okhttp3.*
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import okhttp3.sse.EventSources
import org.json.JSONObject
import java.util.concurrent.TimeUnit

/**
 * Service for managing SSE connections for real-time chat
 */
class SseService(
    private val baseUrl: String = "uwu" // Your backend URL
) {
    companion object {
        private const val TAG = "SseService"
        private const val RECONNECT_DELAY = 5000L // 5 seconds
    }
    
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(0, TimeUnit.SECONDS) // No timeout for SSE
        .writeTimeout(0, TimeUnit.SECONDS)
        .callTimeout(0, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .build()
    
    private var eventSource: EventSource? = null
    private var currentGroupId: String? = null
    private var currentUserId: String? = null
    private var currentToken: String? = null
    private var reconnectJob: Job? = null
    
    // Flows for observing SSE events
    private val _messageFlow = MutableSharedFlow<SseMessage>()
    val messageFlow: SharedFlow<SseMessage> = _messageFlow.asSharedFlow()
    
    private val _connectionState = MutableStateFlow<SseConnectionState>(SseConnectionState.Disconnected)
    val connectionState: StateFlow<SseConnectionState> = _connectionState.asStateFlow()
    
    /**
     * Connect to SSE for a specific group chat
     */
    fun connectToGroup(groupId: String, userId: String, token: String) {
        disconnect() // Close any existing connection
        
        currentGroupId = groupId
        currentUserId = userId
        currentToken = token
        
        Log.i(TAG, "Connecting to group: $groupId for user: $userId")
        _connectionState.value = SseConnectionState.Connecting
        
        val url = "$baseUrl/api/v1/sse/chat/$groupId?userId=$userId"
        val request = Request.Builder()
            .url(url)
            .addHeader("Accept", "text/event-stream")
            .addHeader("Cache-Control", "no-cache")
            .addHeader("Authorization", "Bearer $token") // Agregar token JWT
            .build()
        
        val listener = SseEventListener()
        eventSource = EventSources.createFactory(client).newEventSource(request, listener)
    }
    
    /**
     * Disconnect from SSE
     */
    fun disconnect() {
        Log.i(TAG, "Disconnecting SSE")
        
        reconnectJob?.cancel()
        eventSource?.cancel()
        eventSource = null
        currentGroupId = null
        currentUserId = null
        currentToken = null
        
        _connectionState.value = SseConnectionState.Disconnected
    }
    
    /**
     * Check if currently connected
     */
    fun isConnected(): Boolean {
        return connectionState.value is SseConnectionState.Connected
    }
    
    /**
     * Get current connection info
     */
    fun getCurrentConnectionInfo(): Triple<String?, String?, String?> {
        return Triple(currentGroupId, currentUserId, currentToken)
    }
    
    private fun scheduleReconnect() {
        if (currentGroupId != null && currentUserId != null && currentToken != null) {
            Log.i(TAG, "Scheduling reconnect in ${RECONNECT_DELAY}ms")
            _connectionState.value = SseConnectionState.Reconnecting
            
            reconnectJob = CoroutineScope(Dispatchers.IO).launch {
                delay(RECONNECT_DELAY)
                if (isActive) {
                    Log.i(TAG, "Attempting to reconnect...")
                    connectToGroup(currentGroupId!!, currentUserId!!, currentToken!!)
                }
            }
        }
    }
    
    private inner class SseEventListener : EventSourceListener() {
        
        override fun onOpen(eventSource: EventSource, response: Response) {
            Log.i(TAG, "✅ SSE Connected successfully")
            _connectionState.value = SseConnectionState.Connected
            reconnectJob?.cancel() // Cancel any pending reconnection
        }
        
        override fun onEvent(
            eventSource: EventSource,
            id: String?,
            type: String?,
            data: String
        ) {
            Log.d(TAG, "📨 SSE Event received - Type: $type, Data: $data")
            
            try {
                when (type) {
                    "connected" -> {
                        Log.i(TAG, "📡 Connection confirmed: $data")
                    }
                    
                    "message_created" -> {
                        val json = JSONObject(data)
                        val message = SseMessage.fromJson(json)
                        
                        Log.d(TAG, "💬 New message: ${message.body} from ${message.senderUsername}")
                        
                        // Emit message to flow
                        CoroutineScope(Dispatchers.Main).launch {
                            _messageFlow.emit(message)
                        }
                    }
                    
                    else -> {
                        Log.d(TAG, "🔄 Unknown event type: $type")
                    }
                }
                
            } catch (e: Exception) {
                Log.e(TAG, "❌ Error parsing SSE event: ${e.message}", e)
            }
        }
        
        override fun onFailure(
            eventSource: EventSource,
            t: Throwable?,
            response: Response?
        ) {
            val errorMsg = t?.message ?: response?.message ?: "Unknown error"
            Log.e(TAG, "❌ SSE Connection failed: $errorMsg", t)
            
            _connectionState.value = SseConnectionState.Error(
                t ?: Exception("Connection failed: $errorMsg")
            )
            
            // Schedule automatic reconnection
            scheduleReconnect()
        }
        
        override fun onClosed(eventSource: EventSource) {
            Log.i(TAG, "🔌 SSE Connection closed")
            if (_connectionState.value !is SseConnectionState.Disconnected) {
                _connectionState.value = SseConnectionState.Disconnected
            }
        }
    }
}