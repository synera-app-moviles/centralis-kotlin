package com.example.centralis_kotlin.announcement.repository

import com.example.centralis_kotlin.announcement.model.Announcement
import com.example.centralis_kotlin.announcement.model.Comment
import com.example.centralis_kotlin.announcement.model.Priority
import com.example.centralis_kotlin.announcement.model.dto.AnnouncementDto
import com.example.centralis_kotlin.announcement.model.dto.CommentDto
import com.example.centralis_kotlin.announcement.service.AnnouncementApi
import com.example.centralis_kotlin.announcement.service.IAnnouncementRepository
import com.example.centralis_kotlin.common.RetrofitClient

class RemoteAnnouncementRepository : IAnnouncementRepository {

    private val api = RetrofitClient.retrofitInstance.create(AnnouncementApi::class.java)

    // Mtodos "no suspend" que pide la interfaz pero no se usan
    override fun getAll(): List<Announcement> {
        throw UnsupportedOperationException("Use suspend getAnnouncements() instead")
    }

    override fun getById(id: String): Announcement? {
        throw UnsupportedOperationException("Use suspend getAnnouncementById() instead")
    }

    override fun save(announcement: Announcement): Announcement {
        throw UnsupportedOperationException("Use suspend createAnnouncement() instead")
    }

    override fun delete(id: String) {
        throw UnsupportedOperationException("Use suspend deleteAnnouncement() instead")
    }

    // Mtodos con Retrofit
    override suspend fun getAnnouncements(): List<Announcement> {
        return api.getAnnouncements().map { it.toDomain() }
    }

    override suspend fun getAnnouncementById(id: String): Announcement? {
        return api.getAnnouncementById(id).toDomain()
    }

    suspend fun getComments(announcementId: String): List<Comment> {
        return api.getComments(announcementId).map { it.toDomain() }
    }

    suspend fun addComment(announcementId: String, comment: Comment): Comment {
        val dto = CommentDto(
            id = comment.id,
            announcementId = announcementId,
            employeeId = comment.employeeId,
            content = comment.content,
            createdAt = comment.createdAt.toString()
        )
        return api.addComment(announcementId, dto).toDomain()
    }
}

// --- Mappers ---
private fun AnnouncementDto.toDomain() = Announcement(
    id = id,
    title = title,
    description = description,
    image = image,
    priority = try {
        Priority.valueOf(priority.uppercase()) // convierte String a Enum
    } catch (e: Exception) {
        Priority.LOW
    },
    createdAt = java.util.Date(), //  parsear ISO
    createdBy = createdBy,
    comments = mutableListOf(),
    seenBy = mutableSetOf()
)

private fun CommentDto.toDomain() = Comment(
    id = id,
    announcementId = announcementId,
    employeeId = employeeId,
    content = content,
    createdAt = java.util.Date() //  parsear ISO
)
