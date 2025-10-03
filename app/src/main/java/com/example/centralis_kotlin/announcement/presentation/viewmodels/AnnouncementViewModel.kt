package com.example.centralis_kotlin.announcement.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.centralis_kotlin.announcement.model.Announcement
import com.example.centralis_kotlin.announcement.model.Comment
import com.example.centralis_kotlin.announcement.repository.RemoteAnnouncementRepository
import com.example.centralis_kotlin.announcement.service.IAnnouncementRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AnnouncementViewModel(
    private val repository: IAnnouncementRepository = RemoteAnnouncementRepository()
) : ViewModel() {

    // Lista de anuncios
    private val _announcements = MutableStateFlow<List<Announcement>>(emptyList())
    val announcements: StateFlow<List<Announcement>> = _announcements

    // Anuncio seleccionado (detalle)
    private val _selectedAnnouncement = MutableStateFlow<Announcement?>(null)
    val selectedAnnouncement: StateFlow<Announcement?> = _selectedAnnouncement

    init {
        loadAnnouncements()
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
    fun selectAnnouncement(id: String) {
        viewModelScope.launch {
            try {
                _selectedAnnouncement.value = repository.getAnnouncementById(id)
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
    fun addCommentRemote(announcementId: String, content: String, employeeId: String) {
        viewModelScope.launch {
            try {
                // 1. Crear el comentario en backend
                val newComment = repository.addComment(announcementId, content, employeeId)

                // 2. Obtener la lista completa de comentarios actualizada
                val updatedComments = (repository as? RemoteAnnouncementRepository)
                    ?.getComments(announcementId) ?: emptyList()

                // 3. Actualizar el anuncio en el estado
                val current = _selectedAnnouncement.value
                if (current != null && current.id == announcementId) {
                    val updated = current.copy(comments = updatedComments.toMutableList())
                    _selectedAnnouncement.value = updated
                }

                // 4. Refrescar lista de anuncios
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
                repository.delete(id)
                loadAnnouncements()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
