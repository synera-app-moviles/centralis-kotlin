package com.example.centralis_kotlin.announcement.repository

import com.example.centralis_kotlin.announcement.model.Announcement
import com.example.centralis_kotlin.announcement.model.Comment
import com.example.centralis_kotlin.announcement.model.Priority
import com.example.centralis_kotlin.announcement.model.dto.AnnouncementDto
import com.example.centralis_kotlin.announcement.model.dto.CommentDto
import com.example.centralis_kotlin.announcement.model.dto.CreateCommentDto
import com.example.centralis_kotlin.announcement.service.AnnouncementApi
import com.example.centralis_kotlin.announcement.service.IAnnouncementRepository
import com.example.centralis_kotlin.common.RetrofitClient
import java.text.SimpleDateFormat
import java.util.*

class RemoteAnnouncementRepository : IAnnouncementRepository {

    private val api = RetrofitClient.retrofitInstance.create(AnnouncementApi::class.java)

    // Interfaz sincrona
    override fun getAll(): List<Announcement> {
        throw UnsupportedOperationException("Use suspend getAnnouncements() instead")
    }

    override fun getById(id: String): Announcement? {
        throw UnsupportedOperationException("Use suspend getAnnouncementById() instead")
    }

    /**
     * Metodo de posteo de anuncio
     * @param announcement Objeto de anuncio a postear
     */
    override suspend fun save(announcement: Announcement): Announcement {
        return if (announcement.id.isBlank()) {
            // Crear nuevo
            api.createAnnouncement(announcement.toDto()).toDomain()
        } else {
            // Editar existente
            api.updateAnnouncement(announcement.id, announcement.toDto()).toDomain()
        }
    }

    override fun delete(id: String) {
        throw UnsupportedOperationException("Use suspend deleteAnnouncement() instead")
    }

    //Implementaciones suspend

    override suspend fun getAnnouncements(): List<Announcement> {
        return api.getAnnouncements().map { it.toDomain() }
    }

    override suspend fun getAnnouncementById(id: String): Announcement? {
        val dto = api.getAnnouncementById(id)

        val comments = try {
            api.getComments(id).map { it.toDomain() }
        } catch (t: Throwable) {
            emptyList()
        }

        val announcement = dto.toDomain()
        announcement.comments.clear()
        announcement.comments.addAll(comments)
        return announcement
    }

    suspend fun getComments(announcementId: String): List<Comment> {
        return api.getComments(announcementId).map { it.toDomain() }
    }

    override suspend fun addComment(announcementId: String, content: String, employeeId: String): Comment {
        val dto = CreateCommentDto(content = content, employeeId = employeeId)
        val response = api.addComment(announcementId, dto)
        return response.toDomain()
    }

    //Mappers y helpers

    private fun AnnouncementDto.toDomain(): Announcement {
        return Announcement(
            id = id,
            title = title ?: "",
            description = description ?: "",
            image = image,
            priority = mapPriority(priority),
            createdAt = parseIso8601(createdAt),
            createdBy = createdBy ?: "",
            comments = mutableListOf(),
            seenBy = mutableSetOf()
        )
    }

    private fun Announcement.toDto(withoutId: Boolean = false): AnnouncementDto {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        return AnnouncementDto(
            id = if (withoutId) "" else id,
            title = title,
            description = description,
            image = image,
            priority = priority.name,
            createdAt = sdf.format(createdAt),
            createdBy = createdBy
        )
    }


    private fun CommentDto.toDomain(): Comment {
        return Comment(
            id = id,
            announcementId = announcementId,
            employeeId = employeeId,
            content = content,
            createdAt = parseIso8601(createdAt)
        )
    }

    private fun mapPriority(p: String?): Priority {
        if (p == null) return Priority.Normal
        return when (p.trim().lowercase(Locale.getDefault())) {
            "urgent", "urgente" -> Priority.Urgent
            "high", "alto" -> Priority.High
            "low" -> Priority.LOW
            "normal" -> Priority.Normal
            else -> Priority.Normal
        }
    }

    private fun parseIso8601(value: String?): Date {
        if (value.isNullOrBlank()) return Date()
        val patterns = listOf(
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            "yyyy-MM-dd'T'HH:mm:ss'Z'",
            "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
            "yyyy-MM-dd'T'HH:mm:ssXXX",
            "yyyy-MM-dd"
        )
        for (pattern in patterns) {
            try {
                val sdf = SimpleDateFormat(pattern, Locale.US)
                sdf.timeZone = TimeZone.getTimeZone("UTC")
                val parsed = sdf.parse(value)
                if (parsed != null) return parsed
            } catch (_: Throwable) { }
        }
        return Date()
    }
}
