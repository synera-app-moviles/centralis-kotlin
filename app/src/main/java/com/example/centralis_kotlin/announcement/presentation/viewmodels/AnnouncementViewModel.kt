package com.example.centralis_kotlin.announcement.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.centralis_kotlin.announcement.model.Announcement
import com.example.centralis_kotlin.announcement.model.Comment
import com.example.centralis_kotlin.announcement.repository.RemoteAnnouncementRepository
import com.example.centralis_kotlin.announcement.service.IAnnouncementRepository
import com.example.centralis_kotlin.common.RetrofitClient
import com.example.centralis_kotlin.profile.models.ProfileResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateMapOf

class AnnouncementViewModel(
    private val repository: IAnnouncementRepository = RemoteAnnouncementRepository()
) : ViewModel() {

    private val profileWs = RetrofitClient.profileWebService
    
    // Lista de anuncios
    private val _announcements = MutableStateFlow<List<Announcement>>(emptyList())
    val announcements: StateFlow<List<Announcement>> = _announcements

    // Anuncio seleccionado (detalle)
    private val _selectedAnnouncement = MutableStateFlow<Announcement?>(null)
    val selectedAnnouncement: StateFlow<Announcement?> = _selectedAnnouncement

    // Cache de perfiles de usuarios
    val userProfiles = mutableStateMapOf<String, ProfileResponse>()

    init {
        loadAnnouncements()
    }

    /**
     * Obtener perfil de usuario por ID, con caché
     */
    fun getUserProfile(userId: String): ProfileResponse? = userProfiles[userId]

    /**
     * Cargar perfil de usuario desde el backend
     */
    private suspend fun ensureProfile(userId: String, token: String) {
        if (userId.isBlank() || userProfiles.containsKey(userId)) return
        try {
            val resp = profileWs.getProfileByUserId(userId, "Bearer $token")
            if (resp.isSuccessful) resp.body()?.let { userProfiles[userId] = it }
        } catch (e: Exception) {
            android.util.Log.w("AnnouncementVM", "Error cargando perfil $userId: ${e.message}")
        }
    }

    //  Cargar todos los anuncios
    fun loadAnnouncements() {
        viewModelScope.launch {
            try {
                _announcements.value = repository.getAnnouncements()
            } catch (e: Exception) {
                e.printStackTrace()
                _announcements.value = emptyList()
            }
        }
    }

    //  Seleccionar anuncio (detalle con comentarios)
    fun selectAnnouncement(id: String, token: String? = null) {
        viewModelScope.launch {
            try {
                // 1. Cargar el anuncio base
                val announcement = repository.getAnnouncementById(id)
                
                // 2. Cargar comentarios explícitamente desde el backend
                val comments = (repository as? RemoteAnnouncementRepository)
                    ?.getComments(id) ?: emptyList()
                
                // 3. Combinar anuncio con comentarios actualizados
                val announcementWithComments = announcement?.copy(comments = comments.toMutableList())
                _selectedAnnouncement.value = announcementWithComments
                
                // 4. Cargar perfiles de todos los usuarios que comentaron
                if (!token.isNullOrBlank() && announcementWithComments != null) {
                    val uniqueUserIds = announcementWithComments.comments.map { it.employeeId }.distinct()
                    uniqueUserIds.forEach { userId ->
                        ensureProfile(userId, token)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _selectedAnnouncement.value = null
            }
        }
    }

    //  Crear nuevo anuncio
    fun createAnnouncement(announcement: Announcement) {
        viewModelScope.launch {
            try {
                repository.save(announcement)
                loadAnnouncements()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Agregar comentario (remoto)
    fun addCommentRemote(announcementId: String, content: String, employeeId: String, token: String? = null) {
        viewModelScope.launch {
            try {
                // 1. Crear el comentario en backend
                val newComment = repository.addComment(announcementId, content, employeeId)

                // 2. Recargar TODOS los comentarios del anuncio desde el backend
                val updatedComments = (repository as? RemoteAnnouncementRepository)
                    ?.getComments(announcementId) ?: emptyList()

                // 3. Actualizar el anuncio en el estado con todos los comentarios
                val current = _selectedAnnouncement.value
                if (current != null && current.id == announcementId) {
                    val updated = current.copy(comments = updatedComments.toMutableList())
                    _selectedAnnouncement.value = updated
                    
                    // 4. Cargar perfiles de todos los usuarios (incluyendo el nuevo comentarista)
                    if (!token.isNullOrBlank()) {
                        val uniqueUserIds = updatedComments.map { it.employeeId }.distinct()
                        uniqueUserIds.forEach { userId ->
                            ensureProfile(userId, token)
                        }
                    }
                }

                // 5. Refrescar lista de anuncios
                loadAnnouncements()

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    //  Marcar como visto por empleado
    fun markAsSeen(announcementId: String, employeeId: String) {
        viewModelScope.launch {
            try {
                val announcement = repository.getById(announcementId)
                if (announcement != null) {
                    announcement.seenBy.add(employeeId)
                    repository.save(announcement)
                    _selectedAnnouncement.value = announcement
                    loadAnnouncements()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    //  Eliminar anuncio
    fun deleteAnnouncement(id: String) {
        viewModelScope.launch {
            try {
                (repository as? RemoteAnnouncementRepository)?.deleteAnnouncement(id)
                loadAnnouncements() // Recargar la lista después de eliminar
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
