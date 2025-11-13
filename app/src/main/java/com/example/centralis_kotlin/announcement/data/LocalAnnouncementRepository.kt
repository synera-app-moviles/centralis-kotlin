package com.example.centralis_kotlin.announcement.data

import com.example.centralis_kotlin.announcement.model.Announcement

class LocalAnnouncementRepository(private val dao: AnnouncementDao) {

    suspend fun saveAnnouncement(announcement: Announcement) {
        val entity = AnnouncementEntity(
            id = announcement.id,
            title = announcement.title,
            description = announcement.description,
            image = announcement.image,
            priority = announcement.priority.name,
            createdAt = announcement.createdAt.time,
            createdBy = announcement.createdBy
        )
        dao.insert(entity)
    }

    suspend fun getAll(): List<AnnouncementEntity> = dao.getAll()
}