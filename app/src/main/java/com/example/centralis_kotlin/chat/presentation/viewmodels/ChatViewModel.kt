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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class ChatViewModel(context: Context) : ViewModel() {

    private val groupsWs = RetrofitClient.chatGroupsWebService
    private val prefs = SharedPreferencesManager(context)

    var groups: List<GroupResponse> by mutableStateOf(emptyList())
    var isLoading by mutableStateOf(false)
    var error: String? by mutableStateOf(null)

    var createResult: GroupResponse? by mutableStateOf(null)
    var updateResult: GroupResponse? by mutableStateOf(null)

    fun loadMyGroups() {
        viewModelScope.launch {
            isLoading = true
            error = null
            try {
                val uid = prefs.getUserId()
                val tok = prefs.getToken()
                if (uid.isNullOrBlank() || tok.isNullOrBlank()) {
                    error = "No hay sesión (userId/token). Inicia sesión."
                    return@launch
                }
                val resp = groupsWs.getGroupsByUser(
                    userId = uid,
                    token = "Bearer $tok"
                )
                if (resp.isSuccessful) {
                    groups = resp.body() ?: emptyList()
                } else {
                    error = "Error ${resp.code()}: ${resp.message()}"
                }
            } catch (e: Exception) {
                error = "Error de conexión: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }


    fun createGroup(name: String,
                    description: String?,
                    imageUrl: String?,
                    visibility: GroupVisibility,
                    selectedUserIds: List<String>) {
        viewModelScope.launch {
            isLoading = true; error = null
            try {
                val me = prefs.getUserId() ?: ""
                if (me.isBlank()) {
                    error = "UserId vacío (reloguea)."
                    return@launch
                }
                val members = (selectedUserIds + me).distinct()
                val token = "Bearer ${prefs.getToken()}"
                val req = CreateGroupRequest(name, description, null, visibility, members,me)
                val resp = groupsWs.createGroup(req, token)
                withContext(Dispatchers.Main) {
                    if (resp.isSuccessful) {
                        createResult = resp.body()
                        loadMyGroups()
                    } else error = "Error ${resp.code()}: ${resp.message()}"
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) { error = "Error de conexión: ${e.message}" }
            } finally {
                withContext(Dispatchers.Main) { isLoading = false }
            }
        }
    }

    fun updateGroup(groupId: String, name: String?, desc: String?, imageUrl: String?) {
        viewModelScope.launch {
            isLoading = true; error = null
            try {
                val token = "Bearer ${prefs.getToken()}"
                val req = UpdateGroupRequest(name, desc, imageUrl)
                val resp = groupsWs.updateGroup(groupId, req, token)
                withContext(Dispatchers.Main) {
                    if (resp.isSuccessful) {
                        updateResult = resp.body()
                        loadMyGroups()
                    } else error = "Error ${resp.code()}: ${resp.message()}"
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) { error = "Error de conexión: ${e.message}" }
            } finally {
                withContext(Dispatchers.Main) { isLoading = false }
            }
        }
    }
}