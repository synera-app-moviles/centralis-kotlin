package com.example.centralis_kotlin.announcement.repository

import com.example.centralis_kotlin.announcement.model.Announcement
import com.example.centralis_kotlin.announcement.model.Comment
import com.example.centralis_kotlin.announcement.model.Priority
import com.example.centralis_kotlin.announcement.service.IAnnouncementRepository
import java.util.concurrent.ConcurrentHashMap
/*
class InMemoryAnnouncementRepository : IAnnouncementRepository {




    private val announcements = ConcurrentHashMap<String, Announcement>()



    // Métodos síncronos
    override fun getAll(): List<Announcement> {
        return announcements.values.toList()
    }

    override fun getById(id: String): Announcement? {
        return announcements[id]
    }

    override fun save(announcement: Announcement): Announcement {
        announcements[announcement.id] = announcement
        return announcement
    }

    override fun delete(id: String) {
        announcements.remove(id)
    }

    // Métodos suspendidos
    override suspend fun getAnnouncements(): List<Announcement> {
        return getAll()
    }

    override suspend fun getAnnouncementById(id: String): Announcement? {
        return getById(id)
    }

    override suspend fun addComment(
        announcementId: String,
        content: String,
        employeeId: String
    ): Comment {
        TODO("Not yet implemented")
    }

    // Métodos extra para comentarios y vistos (si los necesitas)
    suspend fun addComment(announcementId: String, comment: Comment) {
        val announcement = announcements[announcementId]
        if (announcement != null) {
            val updatedComments = announcement.comments.toMutableList().apply {
                add(comment)
            }
            announcements[announcementId] = announcement.copy(comments = updatedComments)
        }
    }

    suspend fun markAsSeen(announcementId: String, employeeId: String) {
        val announcement = announcements[announcementId]
        if (announcement != null) {
            val updatedSeen = announcement.seenBy.toMutableSet().apply {
                add(employeeId)
            }
            announcements[announcementId] = announcement.copy(seenBy = updatedSeen)
        }
    }
}

*/
