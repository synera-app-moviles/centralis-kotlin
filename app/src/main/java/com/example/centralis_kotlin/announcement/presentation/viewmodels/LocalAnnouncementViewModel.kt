package com.example.centralis_kotlin.announcement.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.centralis_kotlin.announcement.local.LocalAnnouncementRepository
import com.example.centralis_kotlin.announcement.local.SavedAnnouncementEntity
import com.example.centralis_kotlin.announcement.model.Announcement
import com.example.centralis_kotlin.announcement.model.Priority
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

/**
 * ViewModel para manejar los anuncios guardados localmente (Room).
 * Usa AndroidViewModel para acceder al Application context y construir el repositorio.
 */
class LocalAnnouncementViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = LocalAnnouncementRepository(application.applicationContext)

    private val _savedAnnouncements = MutableStateFlow<List<Announcement>>(emptyList())
    fun getAllSavedAnnouncements(): StateFlow<List<Announcement>> = _savedAnnouncements.asStateFlow()

    init {
        // Cargar al inicio
        viewModelScope.launch {
            loadSavedAnnouncements()
        }
    }

    private suspend fun loadSavedAnnouncements() {
        try {
            val entities = repo.getAll()
            val domain = entities.map { it.toDomain() }
            _savedAnnouncements.value = domain
        } catch (t: Throwable) {
            t.printStackTrace()
            _savedAnnouncements.value = emptyList()
        }
    }

    /**
     * Publica/recarga la lista (útil para Composables)
     */
    fun reload() {
        viewModelScope.launch { loadSavedAnnouncements() }
    }

    /**
     * Guarda un anuncio localmente (inserta o reemplaza).
     */
    fun saveAnnouncement(announcement: Announcement) {
        viewModelScope.launch {
            try {
                val entity = announcement.toEntity()
                repo.save(entity)
                loadSavedAnnouncements()
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        }
    }

    /**
     * Elimina un anuncio local por id.
     */
    fun deleteAnnouncement(announcement: Announcement) {
        viewModelScope.launch {
            try {
                repo.deleteById(announcement.id)
                loadSavedAnnouncements()
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        }
    }

    // -------------------------
    // Mapeos entre Entity <-> Domain
    // -------------------------
    private fun SavedAnnouncementEntity.toDomain(): Announcement {
        // Intentar parsear createdAt; si falla, usar Date()
        val parsedDate: Date = try {
            // Soporte tanto timestamps numéricos como ISO strings
            when {
                createdAt.matches(Regex("^\\d+$")) -> {
                    Date(createdAt.toLong())
                }
                else -> {
                    // Intentamos ISO
                    val patterns = listOf(
                        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                        "yyyy-MM-dd'T'HH:mm:ss'Z'",
                        "yyyy-MM-dd'T'HH:mm:ssXXX",
                        "yyyy-MM-dd"
                    )
                    var dt: Date? = null
                    for (pattern in patterns) {
                        try {
                            val sdf = SimpleDateFormat(pattern, Locale.US)
                            sdf.timeZone = TimeZone.getTimeZone("UTC")
                            val p = sdf.parse(createdAt)
                            if (p != null) { dt = p; break }
                        } catch (_: Exception) { /* seguir probando */ }
                    }
                    dt ?: Date()
                }
            }
        } catch (_: Exception) {
            Date()
        }

        val priorityEnum = try {
            Priority.valueOf(priority.uppercase(Locale.getDefault()))
        } catch (_: Exception) {
            Priority.Normal
        }

        return Announcement(
            id = id,
            title = title,
            description = description,
            image = null,
            priority = priorityEnum,
            createdAt = parsedDate,
            createdBy = "",
            comments = mutableListOf(),
            seenBy = mutableSetOf()
        )
    }

    private fun Announcement.toEntity(): SavedAnnouncementEntity {
        // Guardamos createdAt como timestamp en millis (string) para compatibilidad
        val createdAtStr = try {
            this.createdAt.time.toString()
        } catch (_: Exception) {
            Date().time.toString()
        }

        return SavedAnnouncementEntity(
            id = this.id.ifBlank { UUID.randomUUID().toString() },
            title = this.title,
            description = this.description,
            priority = this.priority.name,
            createdAt = createdAtStr
        )
    }
}
