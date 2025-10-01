package com.example.centralis_kotlin.announcement.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.centralis_kotlin.announcement.model.Announcement
import com.example.centralis_kotlin.announcement.model.Comment
import com.example.centralis_kotlin.announcement.repository.InMemoryAnnouncementRepository
import com.example.centralis_kotlin.announcement.service.IAnnouncementRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AnnouncementViewModel(
    private val repository: IAnnouncementRepository = InMemoryAnnouncementRepository()
) : ViewModel() {

    // Estado: lista de anuncios
    private val _announcements = MutableStateFlow<List<Announcement>>(emptyList())
    val announcements: StateFlow<List<Announcement>> = _announcements

    // Estado: anuncio seleccionado (para detalle)
    private val _selectedAnnouncement = MutableStateFlow<Announcement?>(null)
    val selectedAnnouncement: StateFlow<Announcement?> = _selectedAnnouncement

    init {
        loadAnnouncements()
    }

    // Cargar todos los anuncios
    fun loadAnnouncements() {
        viewModelScope.launch {
            _announcements.value = repository.getAll()
        }
    }

    // Seleccionar anuncio para ver detalle
    fun selectAnnouncement(id: String) {
        viewModelScope.launch {
            _selectedAnnouncement.value = repository.getById(id)
        }
    }

    // Crear nuevo anuncio (solo managers en dominio real)
    fun createAnnouncement(announcement: Announcement) {
        viewModelScope.launch {
            repository.save(announcement)
            loadAnnouncements()
        }
    }

    // Agregar comentario a un anuncio
    fun addComment(announcementId: String, comment: Comment) {
        viewModelScope.launch {
            val announcement = repository.getById(announcementId)
            if (announcement != null) {
                announcement.comments.add(comment)
                repository.save(announcement)
                _selectedAnnouncement.value = announcement
                loadAnnouncements()
            }
        }
    }

    // Marcar como visto por empleado
    fun markAsSeen(announcementId: String, employeeId: String) {
        viewModelScope.launch {
            val announcement = repository.getById(announcementId)
            if (announcement != null) {
                announcement.seenBy.add(employeeId)
                repository.save(announcement)
                _selectedAnnouncement.value = announcement
                loadAnnouncements()
            }
        }
    }

    // Eliminar anuncio
    fun deleteAnnouncement(id: String) {
        viewModelScope.launch {
            repository.delete(id)
            loadAnnouncements()
        }
    }
}
