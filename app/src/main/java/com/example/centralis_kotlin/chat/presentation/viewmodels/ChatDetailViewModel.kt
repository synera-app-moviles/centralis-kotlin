package com.example.centralis_kotlin.chat.presentation.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.centralis_kotlin.chat.domain.models.*
import com.example.centralis_kotlin.common.RetrofitClient
import com.example.centralis_kotlin.common.SharedPreferencesManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.centralis_kotlin.profile.models.ProfileResponse
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class ChatDetailViewModel(context: Context) : ViewModel() {

    private val msgsWebService = RetrofitClient.chatMessagesWebService
    private val prefs = SharedPreferencesManager(context)
    private val profileWs = RetrofitClient.profileWebService
    val currentUserId: String? = prefs.getUserId()
    val profiles = mutableStateMapOf<String, ProfileResponse>()

    var messages: List<MessageResponse> by mutableStateOf(emptyList())
    var isLoading by mutableStateOf(false)
    var sending by mutableStateOf(false)
    var error: String? by mutableStateOf(null)


    fun profileOf(userId: String): ProfileResponse? = profiles[userId]

    private suspend fun ensureProfile(userId: String, token: String) {
        if (userId.isBlank() || profiles.containsKey(userId)) return
        try {
            val resp = profileWs.getProfileByUserId(userId, "Bearer $token")
            if (resp.isSuccessful) resp.body()?.let { profiles[userId] = it }
        } catch (_: Exception) { }
    }

    fun load(groupId: String) {
        viewModelScope.launch {
            isLoading = true; error = null
            try {
                val token = "Bearer ${prefs.getToken()}"
                val resp = msgsWebService.getMessagesByGroup(groupId, token)
                withContext(Dispatchers.Main) {
                    if (resp.isSuccessful) {
                        messages = resp.body().orEmpty()

                        // Prefetch de los perfiles de todos los autores
                        val tok = prefs.getToken().orEmpty()
                        val uniqueSenders = messages.map { it.senderId }.distinct()
                        coroutineScope {
                            uniqueSenders.map { uid -> async { ensureProfile(uid, tok) } }.forEach { it.await() }
                        }
                    } else {
                        error = "Error ${resp.code()}: ${resp.message()}"
                        if (resp.code() == 401) prefs.clearAll()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) { error = "Error de conexión: ${e.message}" }
            } finally {
                withContext(Dispatchers.Main) { isLoading = false }
            }
        }
    }

    fun send(groupId: String, text: String) {
        if (text.isBlank()) return
        viewModelScope.launch {
            sending = true; error = null
            try {
                val token = "Bearer ${prefs.getToken()}"
                val uid = prefs.getUserId()
                if (token.isNullOrBlank() || uid.isNullOrBlank()) {
                    error = "No hay sesión (token/userId)"; return@launch
                }
                val resp = msgsWebService.createMessage(groupId, CreateMessageRequest(uid,text), token)
                withContext(Dispatchers.Main) {
                    if (resp.isSuccessful) {
                        resp.body()?.let { created ->
                            messages = messages + created
                            val tok = prefs.getToken().orEmpty()
                            ensureProfile(created.senderId, tok)
                        }
                    } else {
                        error = "Error ${resp.code()}: ${resp.message()}"
                        if (resp.code() == 401) prefs.clearAll()
                    }

                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) { error = "Error de conexión: ${e.message}" }
            } finally {
                withContext(Dispatchers.Main) { sending = false }
            }
        }
    }

    fun editMessage(groupId: String, messageId: String, newBody: String) {
        viewModelScope.launch {
            try {
                val token = "Bearer ${prefs.getToken()}"
                val resp = msgsWebService.updateMessageBody(groupId, messageId, UpdateMessageBodyRequest(newBody), token)
                withContext(Dispatchers.Main) {
                    if (resp.isSuccessful) {
                        val updated = resp.body()
                        if (updated != null) {
                            messages = messages.map { if (it.messageId == updated.messageId) updated else it }
                        }
                    } else error = "Error ${resp.code()}: ${resp.message()}"
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) { error = "Error de conexión: ${e.message}" }
            }
        }
    }

    fun deleteMessage(groupId: String, messageId: String) {
        viewModelScope.launch {
            try {
                val token = "Bearer ${prefs.getToken()}"
                val resp = msgsWebService.deleteMessage(groupId, messageId, token)
                withContext(Dispatchers.Main) {
                    if (resp.isSuccessful) {
                        messages = messages.filterNot { it.messageId == messageId }
                    } else error = "Error ${resp.code()}: ${resp.message()}"
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) { error = "Error de conexión: ${e.message}" }
            }
        }
    }

    fun updateStatus(groupId: String, messageId: String, status: MessageStatus) {
        viewModelScope.launch {
            try {
                val token = "Bearer ${prefs.getToken()}"
                val resp = msgsWebService.updateMessageStatus(groupId, messageId, UpdateMessageStatusRequest(status), token)
                withContext(Dispatchers.Main) {
                    if (resp.isSuccessful) {
                        val updated = resp.body()
                        if (updated != null) {
                            messages = messages.map { if (it.messageId == updated.messageId) updated else it }
                        }
                    } else error = "Error ${resp.code()}: ${resp.message()}"
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) { error = "Error de conexión: ${e.message}" }
            }
        }
    }
}